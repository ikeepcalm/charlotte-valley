package dev.ua.ikeepcalm.util.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Painter {

    public static void shadow(Graphics2D g, double cx, double cy, double width, double height) {
        g.setColor(Palette.SHADOW);
        g.fill(new Ellipse2D.Double(cx - width / 2, cy - height / 2, width, height));
    }

    public static void blossom(Graphics2D g, double cx, double cy, double radius, Color petal, double angle) {
        AffineTransform saved = g.getTransform();
        g.translate(cx, cy);
        g.rotate(angle);
        g.setColor(petal);
        for (int i = 0; i < 5; i++) {
            g.fill(new Ellipse2D.Double(-radius * 0.38, -radius * 1.05, radius * 0.76, radius * 0.95));
            g.rotate(Math.PI * 2 / 5);
        }
        circle(g, Palette.BLOSSOM_CENTER, 0, 0, radius * 0.36);
        circle(g, Palette.alpha(Color.WHITE, 0.7), -radius * 0.09, -radius * 0.13, radius * 0.11);
        g.setTransform(saved);
    }

    public static void circle(Graphics2D g, Paint paint, double cx, double cy, double radius) {
        g.setPaint(paint);
        g.fill(new Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2));
    }

    public static Graphics2D smooth(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g;
    }

    public static int seed(int x, int y) {
        int hash = x * 73856093 ^ y * 19349663;
        return (hash ^ (hash >>> 13)) & 0x7fffffff;
    }
}
