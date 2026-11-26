package dev.ua.ikeepcalm.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class Sprites {

    public static final int TILE = 72;
    public static final int PAD = 18;
    public static final int RIBBON_SHADOW = 4;
    public static final SpriteSheet HEDGE = sheet("hedge", 0);
    public static final SpriteSheet[] FLOWERS = {sheet("flower_0", 8), sheet("flower_1", 8), sheet("flower_2", 8), sheet("flower_3", 8)};
    public static final SpriteSheet SPIKE_RAISED = sheet("spike_raised", 5);
    public static final SpriteSheet DOOR_OPEN = sheet("door_open", 8);
    public static final SpriteSheet CHARLOTTE = sheet("charlotte", 0);
    public static final SpriteSheet GRASS = sheet("grass", 0);
    public static final SpriteSheet BLOSSOM = sheet("blossom", 0);
    public static final SpriteSheet PETAL = sheet("petal", 0);
    public static final SpriteSheet SPARKLE = sheet("sparkle", 0);
    public static final SpriteSheet POLLEN = sheet("pollen", 0);
    public static final BufferedImage SPIKE_LOWERED = image("spike_lowered");
    public static final BufferedImage DOOR_LOCKED = image("door_locked");
    public static final BufferedImage SHADOW = image("shadow");
    public static final BufferedImage SKY = image("sky");
    public static final BufferedImage CLOUD = image("cloud");
    public static final BufferedImage SLOT = image("slot");
    public static final BufferedImage VIGNETTE = image("vignette");
    public static final BufferedImage ICON = image("icon");
    private static final int ISLAND_EDGE = 84;
    private static final int RIBBON_EDGE = 26;
    private static final BufferedImage ISLAND = image("island");
    private static final BufferedImage RIBBON = image("ribbon");

    public static void drawIsland(Graphics2D g, int width, int height) {
        nineSlice(g, ISLAND, 0, 0, width, height, ISLAND_EDGE, ISLAND_EDGE, ISLAND_EDGE, ISLAND_EDGE + TILE);
    }

    public static void drawRibbon(Graphics2D g, int x, int y, int width, int height) {
        nineSlice(g, RIBBON, x, y, width, height + RIBBON_SHADOW, RIBBON_EDGE, RIBBON_EDGE, RIBBON_EDGE, RIBBON_EDGE + RIBBON_SHADOW);
    }

    private static void nineSlice(Graphics2D g, BufferedImage image, int x, int y, int width, int height,
                                  int left, int top, int right, int bottom) {
        int[] dx = {x, x + left, x + width - right, x + width};
        int[] dy = {y, y + top, y + height - bottom, y + height};
        int[] sx = {0, left, image.getWidth() - right, image.getWidth()};
        int[] sy = {0, top, image.getHeight() - bottom, image.getHeight()};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g.drawImage(image, dx[i], dy[j], dx[i + 1], dy[j + 1], sx[i], sy[j], sx[i + 1], sy[j + 1], null);
            }
        }
    }

    private static SpriteSheet sheet(String name, double fps) {
        return new SpriteSheet(image(name), fps);
    }

    private static BufferedImage image(String name) {
        String path = "/sprites/" + name + ".png";
        try (InputStream in = Sprites.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing sprite " + path);
            }
            BufferedImage source = ImageIO.read(in);
            BufferedImage image = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D g = image.createGraphics();
            g.drawImage(source, 0, 0, null);
            g.dispose();
            return image;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read sprite " + path, e);
        }
    }
}
