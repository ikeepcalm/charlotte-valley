package dev.ua.ikeepcalm.domain.level.type.tile;

import java.awt.Color;
import java.awt.Graphics2D;

public class WallTile extends Tile {

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        g.setColor(new Color(0x5D6D7E));
        g.fillRect(x, y, size, size);
        g.setColor(new Color(0x45515E));
        g.drawLine(x, y + size / 2, x + size, y + size / 2);
        g.drawLine(x + size / 2, y, x + size / 2, y + size / 2);
        g.drawLine(x + size / 4, y + size / 2, x + size / 4, y + size);
        g.drawRect(x, y, size - 1, size - 1);
    }
}
