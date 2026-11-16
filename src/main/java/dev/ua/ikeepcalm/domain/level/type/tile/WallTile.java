package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.Graphics2D;

public class WallTile extends Tile {
    private static final double[][] LUMPS = {{0.27, 0.52, 0.3}, {0.73, 0.52, 0.3}, {0.5, 0.36, 0.33}};

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size, double time) {
        int seed = Painter.seed(x, y);
        Painter.shadow(g, x + size * 0.5, y + size * 0.88, size * 1.05, size * 0.3);
        Painter.circle(g, Palette.HEDGE_DARK, x + size * 0.5, y + size * 0.58, size * 0.54);
        for (double[] lump : LUMPS) {
            Painter.circle(g, Palette.HEDGE, x + size * lump[0], y + size * lump[1], size * lump[2]);
        }
        for (double[] lump : LUMPS) {
            Painter.circle(g, Palette.HEDGE_LIGHT, x + size * (lump[0] - 0.07), y + size * (lump[1] - 0.1), size * lump[2] * 0.42);
        }
        if (seed % 3 == 0) {
            for (int i = 0; i < 3; i++) {
                double bx = 0.25 + ((seed >> (i * 3)) % 6) * 0.09;
                double by = 0.38 + ((seed >> (i * 5 + 2)) % 4) * 0.08;
                Painter.circle(g, Palette.BERRY, x + size * bx, y + size * by, size * 0.04);
            }
        }
    }
}
