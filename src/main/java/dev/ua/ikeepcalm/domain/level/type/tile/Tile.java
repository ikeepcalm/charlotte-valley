package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Tile {
    private static final Color GRASS = new Color(0x8BC34A);
    private static final Color GRASS_EDGE = new Color(0x7CB342);

    public boolean canEnter() {
        return true;
    }

    public void onEnter(Level level) {
    }

    public void onLeave(Level level) {
    }

    public abstract void paint(Graphics2D g, int x, int y, int size);

    public static void paintGrass(Graphics2D g, int x, int y, int size) {
        g.setColor(GRASS);
        g.fillRect(x, y, size, size);
        g.setColor(GRASS_EDGE);
        g.drawRect(x, y, size - 1, size - 1);
    }
}
