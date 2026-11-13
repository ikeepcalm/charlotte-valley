package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;

import java.awt.Color;
import java.awt.Graphics2D;

public class FlowerTile extends Tile {

    @Override
    public void onEnter(Level level) {
        level.collectFlower();
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        paintGrass(g, x, y, size);
        int cx = x + size / 2;
        int cy = y + size / 2;
        int petal = size / 5;
        g.setColor(new Color(0xEC407A));
        g.fillOval(cx - petal * 3 / 2, cy - petal / 2, petal, petal);
        g.fillOval(cx + petal / 2, cy - petal / 2, petal, petal);
        g.fillOval(cx - petal / 2, cy - petal * 3 / 2, petal, petal);
        g.fillOval(cx - petal / 2, cy + petal / 2, petal, petal);
        g.setColor(new Color(0xFFEB3B));
        g.fillOval(cx - petal / 2, cy - petal / 2, petal, petal);
    }
}
