package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.domain.level.model.source.Direction;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.service.LevelManager;
import dev.ua.ikeepcalm.domain.level.type.LevelListener;
import dev.ua.ikeepcalm.domain.move.type.MoveCommand;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

public class GamePanel extends JPanel implements LevelListener {
    private static final int TILE = 64;
    private static final int HUD = 40;
    private static final Map<Integer, Direction> KEYS = Map.of(
            KeyEvent.VK_UP, Direction.UP, KeyEvent.VK_W, Direction.UP,
            KeyEvent.VK_DOWN, Direction.DOWN, KeyEvent.VK_S, Direction.DOWN,
            KeyEvent.VK_LEFT, Direction.LEFT, KeyEvent.VK_A, Direction.LEFT,
            KeyEvent.VK_RIGHT, Direction.RIGHT, KeyEvent.VK_D, Direction.RIGHT
    );

    private final LevelManager manager;

    public GamePanel(LevelManager manager) {
        this.manager = manager;
        setBackground(new Color(0x33691E));
        KEYS.forEach((key, direction) -> getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0), direction));
        for (Direction direction : Direction.values()) {
            getActionMap().put(direction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    manager.execute(new MoveCommand(manager.getCurrent(), direction));
                }
            });
        }
    }

    @Override
    public void onEvent(GameEvent event, Level level) {
        switch (event) {
            case LEVEL_STARTED -> {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null && !getSize().equals(getPreferredSize())) {
                    window.pack();
                    window.setLocationRelativeTo(null);
                }
            }
            case GAME_WON -> JOptionPane.showMessageDialog(this, "You cleared every level of Charlotte Valley.");
            default -> {
            }
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Level level = manager.getCurrent();
        return new Dimension(level.cols() * TILE, level.rows() * TILE + HUD);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Level level = manager.getCurrent();

        for (int r = 0; r < level.rows(); r++) {
            for (int c = 0; c < level.cols(); c++) {
                level.tileAt(new Position(r, c)).paint(g, c * TILE, HUD + r * TILE, TILE);
            }
        }
        paintPlayer(g, level.getPlayer());

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        int collected = level.getTotalFlowers() - level.getFlowersLeft();
        g.drawString("Level " + (manager.getIndex() + 1) + "/" + manager.levelCount()
                + "    Flowers " + collected + "/" + level.getTotalFlowers(), 12, 27);
    }

    private void paintPlayer(Graphics2D g, Position position) {
        int x = position.getCol() * TILE;
        int y = HUD + position.getRow() * TILE;
        int pad = TILE / 6;
        g.setColor(new Color(0x1E88E5));
        g.fillOval(x + pad, y + pad, TILE - 2 * pad, TILE - 2 * pad);
        g.setColor(new Color(0x0D47A1));
        g.drawOval(x + pad, y + pad, TILE - 2 * pad, TILE - 2 * pad);
        g.setColor(Color.WHITE);
        g.fillOval(x + TILE / 2 - 10, y + TILE / 2 - 8, 7, 7);
        g.fillOval(x + TILE / 2 + 3, y + TILE / 2 - 8, 7, 7);
    }
}
