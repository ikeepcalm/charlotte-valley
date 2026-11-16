package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class FlowerTile extends Tile {

    @Override
    public void onEnter(Level level) {
        level.collectFlower();
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size, double time) {
        int seed = Painter.seed(x, y);
        double sway = Math.sin(time * 2.2 + seed % 100) * 0.09;
        double baseX = x + size * 0.5;
        double baseY = y + size * 0.84;
        double headX = baseX + Math.sin(sway) * size * 0.3;
        double headY = y + size * 0.36;

        Painter.shadow(g, baseX, baseY, size * 0.44, size * 0.12);
        g.setColor(Palette.STEM);
        g.setStroke(new BasicStroke(size * 0.05f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new QuadCurve2D.Double(baseX, baseY, baseX, y + size * 0.6, headX, headY));
        leaf(g, baseX - size * 0.09, y + size * 0.68, size, -0.6);
        leaf(g, baseX + size * 0.09, y + size * 0.62, size, 0.6);

        double pulse = 0.25 + 0.15 * Math.sin(time * 3 + seed % 50);
        Painter.circle(g, Palette.alpha(Color.WHITE, pulse), headX, headY, size * 0.27);
        Painter.blossom(g, headX, headY, size * 0.2, Palette.PETALS[seed % Palette.PETALS.length], sway * 2 + seed % 7);
    }

    private static void leaf(Graphics2D g, double cx, double cy, int size, double angle) {
        AffineTransform saved = g.getTransform();
        g.translate(cx, cy);
        g.rotate(angle);
        g.fill(new Ellipse2D.Double(-size * 0.1, -size * 0.04, size * 0.2, size * 0.08));
        g.setTransform(saved);
    }
}
