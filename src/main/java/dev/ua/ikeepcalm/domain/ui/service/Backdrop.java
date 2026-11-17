package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Backdrop {
    private static final BasicStroke TUFT_STROKE = new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private BufferedImage sky;

    public void paintSky(Graphics2D g, int width, int height, double time) {
        if (sky == null || sky.getWidth() != width || sky.getHeight() != height) {
            sky = renderSky(width, height);
        }
        g.drawImage(sky, 0, 0, null);

        for (int i = 0; i < 4; i++) {
            double drift = (time * (6 + i * 2) + i * width / 3.0) % (width + 240) - 120;
            cloud(g, drift, height * (0.12 + 0.07 * (i % 2)), 1 + 0.25 * (i % 3));
        }
        hills(g, width, height, height * 0.42, 34, 0.011, time * 0.02, Palette.HILL_FAR);
        hills(g, width, height, height * 0.55, 26, 0.017, 1.7, Palette.HILL_NEAR);
    }

    private static BufferedImage renderSky(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, 0, Palette.SKY_TOP, 0, height * 0.6f, Palette.SKY_BOTTOM));
        g.fillRect(0, 0, width, height);
        float sunX = width * 0.82f;
        float sunY = height * 0.2f;
        Painter.circle(g, new RadialGradientPaint(sunX, sunY, width * 0.35f, new float[]{0f, 0.25f, 1f},
                new Color[]{Palette.alpha(Color.WHITE, 0.95), Palette.alpha(Palette.GLOW, 0.6), Palette.alpha(Palette.GLOW, 0)}), sunX, sunY, width * 0.35);
        g.dispose();
        return image;
    }

    public void paintIsland(Graphics2D g, int x, int y, int cols, int rows, int tile) {
        int width = cols * tile;
        int height = rows * tile;
        int rim = tile / 6;
        Painter.shadow(g, x + width / 2.0, y + height + tile * 0.75, width * 1.05, tile * 0.7);

        var soil = new RoundRectangle2D.Double(x - rim, y - rim, width + rim * 2, height + rim * 2 + tile * 0.4, tile * 0.6, tile * 0.6);
        g.setPaint(new GradientPaint(0, y + height, Palette.SOIL, 0, y + height + tile * 0.5f, Palette.SOIL_DARK));
        g.fill(soil);

        var lawn = new RoundRectangle2D.Double(x - rim, y - rim, width + rim * 2, height + rim * 2, tile * 0.6, tile * 0.6);
        Graphics2D clipped = (Graphics2D) g.create();
        clipped.clip(lawn);
        clipped.setColor(Palette.GRASS_DARK);
        clipped.fill(lawn);
        Path2D.Double tufts = new Path2D.Double();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                paintGrass(clipped, tufts, x + c * tile, y + r * tile, tile, (r + c) % 2 == 0);
            }
        }
        clipped.setColor(Palette.GRASS_TUFT);
        clipped.setStroke(TUFT_STROKE);
        clipped.draw(tufts);
        clipped.dispose();
        g.setColor(Palette.alpha(Color.WHITE, 0.35));
        g.setStroke(new BasicStroke(2f));
        g.draw(lawn);
    }

    private void paintGrass(Graphics2D g, Path2D.Double tufts, int x, int y, int tile, boolean light) {
        g.setColor(light ? Palette.GRASS_LIGHT : Palette.GRASS_DARK);
        g.fillRect(x, y, tile, tile);
        int seed = Painter.seed(x, y);
        for (int i = 0; i < 2; i++) {
            double tx = x + tile * (0.2 + 0.6 * ((seed >> (i * 4)) % 16) / 15.0);
            double ty = y + tile * (0.25 + 0.6 * ((seed >> (i * 4 + 8)) % 16) / 15.0);
            for (double[] blade : new double[][]{{-0.04, -0.08}, {0.01, -0.1}, {0.05, -0.07}}) {
                tufts.moveTo(tx, ty);
                tufts.lineTo(tx + tile * blade[0], ty + tile * blade[1]);
            }
        }
        if (seed % 5 == 0) {
            double dx = x + tile * (0.25 + 0.5 * ((seed >> 3) % 8) / 7.0);
            double dy = y + tile * (0.3 + 0.4 * ((seed >> 6) % 8) / 7.0);
            Painter.circle(g, Color.WHITE, dx, dy, tile * 0.035);
            Painter.circle(g, Palette.BLOSSOM_CENTER, dx, dy, tile * 0.015);
        }
    }

    private void cloud(Graphics2D g, double x, double y, double scale) {
        Color puff = Palette.alpha(Color.WHITE, 0.75);
        Painter.circle(g, puff, x, y, 22 * scale);
        Painter.circle(g, puff, x + 26 * scale, y - 8 * scale, 28 * scale);
        Painter.circle(g, puff, x + 56 * scale, y, 20 * scale);
        g.fill(new RoundRectangle2D.Double(x - 4 * scale, y - 6 * scale, 66 * scale, 26 * scale, 26 * scale, 26 * scale));
    }

    private void hills(Graphics2D g, int width, int height, double baseY, double amplitude, double frequency, double phase, Color color) {
        Path2D.Double ridge = new Path2D.Double();
        ridge.moveTo(0, height);
        for (int x = 0; x <= width; x += 8) {
            ridge.lineTo(x, baseY - amplitude * Math.sin(x * frequency + phase) - amplitude * 0.5 * Math.sin(x * frequency * 2.3 + phase * 1.7));
        }
        ridge.lineTo(width, height);
        ridge.closePath();
        g.setColor(color);
        g.fill(ridge);
    }
}
