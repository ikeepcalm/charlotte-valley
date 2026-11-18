package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.domain.level.model.source.Direction;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.service.LevelManager;
import dev.ua.ikeepcalm.domain.level.type.LevelListener;
import dev.ua.ikeepcalm.domain.move.type.MoveCommand;
import dev.ua.ikeepcalm.domain.ui.model.source.ParticleShape;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements LevelListener {
    private static final int TILE = 72;
    private static final int TOP = 88;
    private static final int MARGIN = 80;
    private static final int FOOTER = 40;
    private static final double BANNER_SECONDS = 2.0;
    private static final double FLASH_SECONDS = 0.7;
    private static final double SHAKE_SECONDS = 0.35;
    private static final double WIN_SECONDS = 4.0;
    private static final Map<Integer, Direction> KEYS = Map.of(
            KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_W, Direction.UP,
            KeyEvent.VK_DOWN, Direction.DOWN, KeyEvent.VK_S, Direction.DOWN,
            KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_A, Direction.LEFT,
            KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_D, Direction.RIGHT
    );

    private final LevelManager manager;
    private final Hud hud = new Hud();
    private final PlayerSprite player = new PlayerSprite();
    private final Map<GameEvent, Double> since = new EnumMap<>(GameEvent.class);
    private final Timer animation = new Timer(15, e -> tick());
    private final Dimension size;
    private final ParticleField particles;
    private final Backdrop backdrop;
    private long lastTick = System.nanoTime();
    private double time;

    public GamePanel(LevelManager manager) {
        this.manager = manager;
        size = new Dimension(manager.maxCols() * TILE + MARGIN * 2, TOP + manager.maxRows() * TILE + TILE / 2 + FOOTER);
        particles = new ParticleField(size.width, size.height);
        backdrop = new Backdrop(size.width, size.height);
        for (GameEvent event : GameEvent.values()) {
            since.put(event, Double.MAX_VALUE);
        }
        since.put(GameEvent.LEVEL_COMPLETED, 0.0);
        bindKeys();
    }

    private void bindKeys() {
        KEYS.forEach((key, direction) -> getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0), direction));
        for (Direction direction : Direction.values()) {
            getActionMap().put(direction, action(() -> manager.execute(new MoveCommand(manager.getCurrent(), direction))));
        }
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        getActionMap().put("restart", action(manager::restart));
    }

    private static AbstractAction action(Runnable runnable) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }
        };
    }

    @Override
    public void addNotify() {
        super.addNotify();
        lastTick = System.nanoTime();
        animation.start();
    }

    @Override
    public void removeNotify() {
        animation.stop();
        super.removeNotify();
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    private void tick() {
        long now = System.nanoTime();
        double dt = Math.min(0.05, (now - lastTick) / 1e9);
        lastTick = now;
        time += dt;
        since.replaceAll((event, seconds) -> seconds + dt);
        player.update(dt);
        particles.update(dt);
        repaint();
    }

    @Override
    public void onEvent(GameEvent event, Level level) {
        since.put(event, 0.0);
        switch (event) {
            case LEVEL_STARTED -> player.snap(level.getPlayer());
            case PLAYER_MOVED -> player.moveTo(level.getPlayer());
            case PLAYER_BLOCKED -> player.bump();
            case FLOWER_COLLECTED -> burstAt(level, level.getPlayer(), ParticleShape.PETAL, 22, Palette.PETALS);
            case EXIT_OPENED -> burstAt(level, level.getExitPosition(), ParticleShape.SPARKLE, 26, Palette.GLOW, Palette.GOLD, Color.WHITE);
            case PLAYER_DIED -> burstAt(level, level.getPlayer(), ParticleShape.SPARKLE, 16, Palette.DANGER, Palette.HOOD);
            case GAME_WON -> {
                for (int i = 1; i <= 5; i++) {
                    particles.burst(ParticleShape.PETAL, size.width * i / 6.0, size.height * 0.35, 24, Palette.PETALS);
                }
            }
        }
    }

    private void burstAt(Level level, Position cell, ParticleShape shape, int count, Color... colors) {
        particles.burst(shape, originX(level) + (cell.getCol() + 0.5) * TILE, originY(level) + (cell.getRow() + 0.5) * TILE, count, colors);
    }

    private int originX(Level level) {
        return (size.width - level.cols() * TILE) / 2;
    }

    private int originY(Level level) {
        return TOP + (size.height - TOP - FOOTER - TILE / 2 - level.rows() * TILE) / 2;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = Painter.smooth((Graphics2D) graphics.create());
        Level level = manager.getCurrent();

        backdrop.paintSky(g, time);
        paintBoard((Graphics2D) g.create(), level);
        particles.paint(g);
        paintOverlays(g);
        hud.paintBar(g, size.width, level, manager.getIndex(), manager.levelCount());
        hud.paintHint(g, size.width, size.height);
        paintBanners(g);
        g.dispose();
    }

    private void paintBoard(Graphics2D g, Level level) {
        double sinceDeath = since.get(GameEvent.PLAYER_DIED);
        if (sinceDeath < SHAKE_SECONDS) {
            int amplitude = (int) Math.ceil(8 * (1 - sinceDeath / SHAKE_SECONDS));
            ThreadLocalRandom random = ThreadLocalRandom.current();
            g.translate(random.nextInt(-amplitude, amplitude + 1), random.nextInt(-amplitude, amplitude + 1));
        }
        int ox = originX(level);
        int oy = originY(level);
        backdrop.paintIsland(g, level, ox, oy, TILE, time);
        player.paint(g, ox, oy, TILE);
        g.dispose();
    }

    private void paintOverlays(Graphics2D g) {
        double sinceDeath = since.get(GameEvent.PLAYER_DIED);
        double sinceComplete = since.get(GameEvent.LEVEL_COMPLETED);
        if (sinceDeath < FLASH_SECONDS) {
            double strength = 0.55 * (1 - sinceDeath / FLASH_SECONDS);
            g.setPaint(new RadialGradientPaint(size.width / 2f, size.height / 2f, size.width * 0.75f, new float[]{0.3f, 1f},
                    new Color[]{Palette.alpha(Palette.DANGER, 0), Palette.alpha(Palette.DANGER, strength)}));
            g.fillRect(0, 0, size.width, size.height);
        }
        if (sinceComplete < FLASH_SECONDS) {
            g.setColor(Palette.alpha(Palette.GLOW, 1 - sinceComplete / FLASH_SECONDS));
            g.fillRect(0, 0, size.width, size.height);
        }
    }

    private void paintBanners(Graphics2D g) {
        double sinceWin = since.get(GameEvent.GAME_WON);
        double sinceBanner = since.get(GameEvent.LEVEL_COMPLETED);
        if (sinceWin < WIN_SECONDS) {
            hud.paintBanner(g, size.width, size.height, "The valley is in bloom!",
                    "Every gate is open. Starting again from the first meadow.", fade(sinceWin, WIN_SECONDS));
        } else if (sinceBanner < BANNER_SECONDS) {
            hud.paintBanner(g, size.width, size.height, "Level " + (manager.getIndex() + 1),
                    "Gather every flower to wake the gate", fade(sinceBanner, BANNER_SECONDS));
        }
    }

    private static double fade(double elapsed, double duration) {
        return Math.clamp(Math.min(elapsed / 0.25, (duration - elapsed) / 0.6), 0, 1);
    }
}
