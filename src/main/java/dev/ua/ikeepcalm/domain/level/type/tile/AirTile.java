package dev.ua.ikeepcalm.domain.level.type.tile;

import java.awt.Graphics2D;

public class AirTile extends Tile {

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        paintGrass(g, x, y, size);
    }
}
