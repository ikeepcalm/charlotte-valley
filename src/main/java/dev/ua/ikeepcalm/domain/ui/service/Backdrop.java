package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.util.render.Sprites;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import static dev.ua.ikeepcalm.util.render.Sprites.PAD;
import static dev.ua.ikeepcalm.util.render.Sprites.TILE;

public class Backdrop {
    private final int width;
    private final int height;
    private static final int GRASS_SHADES = 2;
    private static final int GRASS_VARIANTS = 3;

    private final BufferedImage sky;
    private final BufferedImage[] clouds = new BufferedImage[3];
    private BufferedImage island;
    private Level islandLevel;

    public Backdrop(int width, int height) {
        this.width = width;
        this.height = height;
        sky = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = sky.createGraphics();
        g.drawImage(Sprites.SKY, 0, 0, width, height, null);
        g.dispose();
        for (int i = 0; i < clouds.length; i++) {
            double scale = 1 + 0.25 * i;
            clouds[i] = new BufferedImage((int) (Sprites.CLOUD.getWidth() * scale), (int) (Sprites.CLOUD.getHeight() * scale), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D cloud = clouds[i].createGraphics();
            cloud.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            cloud.drawImage(Sprites.CLOUD, 0, 0, clouds[i].getWidth(), clouds[i].getHeight(), null);
            cloud.dispose();
        }
    }

    public void paintSky(Graphics2D g, double time) {
        g.drawImage(sky, 0, 0, null);
        for (int i = 0; i < 4; i++) {
            BufferedImage cloud = clouds[i % clouds.length];
            double scale = (double) cloud.getWidth() / Sprites.CLOUD.getWidth();
            double x = (time * (6 + i * 2) + i * width / 3.0) % (width + 240) - 120;
            double y = height * (0.12 + 0.07 * (i % 2));
            g.drawImage(cloud, (int) (x - 24 * scale), (int) (y - 38 * scale), null);
        }
    }

    public void paintIsland(Graphics2D g, Level level, int x, int y, double time) {
        if (level != islandLevel) {
            island = bakeIsland(level);
            islandLevel = level;
        }
        g.drawImage(island, x - TILE, y - TILE, null);
        paintTiles(g, level, x, y, time, false);
    }

    private static BufferedImage bakeIsland(Level level) {
        BufferedImage image = new BufferedImage((level.cols() + 2) * TILE, (level.rows() + 3) * TILE, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = image.createGraphics();
        Sprites.drawIsland(g, image.getWidth(), image.getHeight());
        for (int r = 0; r < level.rows(); r++) {
            for (int c = 0; c < level.cols(); c++) {
                int variant = (r + c) % GRASS_SHADES * GRASS_VARIANTS + seed(r, c) % GRASS_VARIANTS;
                g.drawImage(Sprites.GRASS.frame(variant), TILE + c * TILE, TILE + r * TILE, null);
            }
        }
        paintTiles(g, level, TILE, TILE, 0, true);
        g.dispose();
        return image;
    }

    private static void paintTiles(Graphics2D g, Level level, int x, int y, double time, boolean statics) {
        for (int r = 0; r < level.rows(); r++) {
            for (int c = 0; c < level.cols(); c++) {
                var tile = level.tileAt(new Position(r, c));
                if (tile.isStatic() == statics) {
                    int left = x + c * TILE - PAD;
                    int top = y + r * TILE - PAD;
                    tile.sprite(time, seed(r, c)).ifPresent(sprite -> g.drawImage(sprite, left, top, null));
                }
            }
        }
    }

    private static int seed(int row, int col) {
        int hash = row * 73856093 ^ col * 19349663;
        return (hash ^ (hash >>> 13)) & 0x7fffffff;
    }
}
