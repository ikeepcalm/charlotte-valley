package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerSprite {
    private static final double HOP_SECONDS = 0.16;
    private static final double LAND_SECONDS = 0.12;
    private static final double BUMP_SECONDS = 0.25;
    private static final double BLINK_SECONDS = 0.12;

    private Position target = new Position(0, 0);
    private double row;
    private double col;
    private int lookX;
    private int lookY = 1;
    private double sinceHop = 1;
    private double sinceBump = 1;
    private double sinceBlink = 1;
    private double untilBlink = 2;

    public void snap(Position position) {
        target = position;
        row = position.getRow();
        col = position.getCol();
        lookX = 0;
        lookY = 1;
        sinceHop = 1;
    }

    public void moveTo(Position position) {
        lookX = Integer.signum(position.getCol() - target.getCol());
        lookY = Integer.signum(position.getRow() - target.getRow());
        target = position;
        sinceHop = 0;
    }

    public void bump() {
        sinceBump = 0;
    }

    public int shownRow() {
        return (int) Math.round(row);
    }

    private double centerX(int originX, int tile) {
        return originX + (col + 0.5) * tile;
    }

    private double centerY(int originY, int tile) {
        return originY + (row + 0.5) * tile;
    }

    public void update(double dt) {
        double ease = Math.min(1, dt * 18);
        row += (target.getRow() - row) * ease;
        col += (target.getCol() - col) * ease;
        sinceHop += dt;
        sinceBump += dt;
        sinceBlink += dt;
        untilBlink -= dt;
        if (untilBlink <= 0) {
            sinceBlink = 0;
            untilBlink = 2.2 + ThreadLocalRandom.current().nextDouble() * 2.5;
        }
    }

    public void paint(Graphics2D g, int originX, int originY, int tile) {
        double hop = sinceHop < HOP_SECONDS ? Math.sin(Math.PI * sinceHop / HOP_SECONDS) : 0;
        double landing = sinceHop - HOP_SECONDS;
        double squash = landing > 0 && landing < LAND_SECONDS ? 1 + 0.14 * Math.sin(Math.PI * landing / LAND_SECONDS) : 1;
        double wiggle = sinceBump < BUMP_SECONDS ? Math.sin(sinceBump * 55) * tile * 0.05 * (1 - sinceBump / BUMP_SECONDS) : 0;
        double feetX = centerX(originX, tile) + wiggle;
        double feetY = centerY(originY, tile) + tile * 0.3;

        Painter.shadow(g, feetX, feetY, tile * 0.5 * (1 - hop * 0.3), tile * 0.14 * (1 - hop * 0.3));
        AffineTransform saved = g.getTransform();
        g.translate(feetX, feetY - hop * tile * 0.22);
        g.scale(tile * squash, tile / squash);
        paintBody(g);
        if (lookY >= 0) {
            paintFace(g);
        }
        g.setTransform(saved);
    }

    private void paintBody(Graphics2D g) {
        Path2D.Double cloak = new Path2D.Double();
        cloak.moveTo(-0.26, 0);
        cloak.curveTo(-0.26, -0.2, -0.18, -0.42, 0, -0.42);
        cloak.curveTo(0.18, -0.42, 0.26, -0.2, 0.26, 0);
        cloak.closePath();
        g.setPaint(new GradientPaint(0, -0.42f, Palette.HOOD, 0, 0, Palette.HOOD_DARK));
        g.fill(cloak);
        g.setColor(Palette.HOOD_DARK);
        g.fill(new Ellipse2D.Double(-0.13, -0.05, 0.1, 0.07));
        g.fill(new Ellipse2D.Double(0.03, -0.05, 0.1, 0.07));

        Path2D.Double peak = new Path2D.Double();
        peak.moveTo(-0.12, -0.7);
        peak.quadTo(0.02, -0.86, 0.08, -0.74);
        peak.lineTo(0.1, -0.66);
        peak.closePath();
        g.setColor(Palette.HOOD);
        g.fill(peak);
        Painter.circle(g, Palette.HOOD, 0, -0.5, 0.25);
    }

    private void paintFace(Graphics2D g) {
        double fx = lookX * 0.05;
        double fy = -0.47 + lookY * 0.015;
        Painter.circle(g, Palette.SKIN, fx, fy, 0.18);
        double eyeHeight = sinceBlink < BLINK_SECONDS ? 0.012 : 0.07;
        g.setColor(Palette.INK);
        for (int side = -1; side <= 1; side += 2) {
            double ex = fx + side * 0.07 + lookX * 0.025;
            g.fill(new Ellipse2D.Double(ex - 0.025, fy - 0.02 - eyeHeight / 2, 0.05, eyeHeight));
            Painter.circle(g, Palette.alpha(Palette.BLUSH, 0.7), fx + side * 0.11, fy + 0.07, 0.035);
        }
    }
}
