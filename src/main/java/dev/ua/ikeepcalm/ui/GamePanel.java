package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.LevelManager;
import dev.ua.ikeepcalm.level.Position;
import dev.ua.ikeepcalm.level.source.Direction;
import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.level.type.LevelListener;
import dev.ua.ikeepcalm.move.MoveCommand;
import dev.ua.ikeepcalm.render.Palette;
import dev.ua.ikeepcalm.render.Sprites;
import dev.ua.ikeepcalm.ui.source.ParticleShape;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static dev.ua.ikeepcalm.render.Sprites.TILE;

public class GamePanel extends JPanel implements LevelListener {

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
            case FLOWER_COLLECTED -> burstAt(level, level.getPlayer(), ParticleShape.PETAL, 22);
            case EXIT_OPENED -> burstAt(level, level.getExitPosition(), ParticleShape.GOLD_SPARKLE, 26);
            case PLAYER_DIED -> burstAt(level, level.getPlayer(), ParticleShape.RED_SPARKLE, 16);
            case GAME_WON -> {
                for (int i = 1; i <= 5; i++) {
                    particles.burst(ParticleShape.PETAL, size.width * i / 6.0, size.height * 0.35, 24);
                }
            }
        }
    }

    private void burstAt(Level level, Position cell, ParticleShape shape, int count) {
        particles.burst(shape, originX(level) + (cell.getCol() + 0.5) * TILE, originY(level) + (cell.getRow() + 0.5) * TILE, count);
    }

    private int originX(Level level) {
        return (size.width - level.cols() * TILE) / 2;
    }

    private int originY(Level level) {
        return TOP + (size.height - TOP - FOOTER - TILE / 2 - level.rows() * TILE) / 2;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
        backdrop.paintIsland(g, level, ox, oy, time);
        player.paint(g, ox, oy);
        g.dispose();
    }

    private void paintOverlays(Graphics2D g) {
        double sinceDeath = since.get(GameEvent.PLAYER_DIED);
        double sinceComplete = since.get(GameEvent.LEVEL_COMPLETED);
        if (sinceDeath < FLASH_SECONDS) {
            Graphics2D faded = (Graphics2D) g.create();
            faded.setComposite(AlphaComposite.SrcOver.derive((float) (1 - sinceDeath / FLASH_SECONDS)));
            faded.drawImage(Sprites.VIGNETTE, 0, 0, size.width, size.height, null);
            faded.dispose();
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
