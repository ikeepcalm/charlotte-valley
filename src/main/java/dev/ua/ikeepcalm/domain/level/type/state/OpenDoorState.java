package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class OpenDoorState implements DoorState {

    @Override
    public boolean canEnter() {
        return true;
    }

    @Override
    public void onEnter(Level level) {
        level.fire(GameEvent.LEVEL_COMPLETED);
    }

    @Override
    public void open(ExitTile tile, Level level) {
    }

    @Override
    public void paint(Graphics2D g, Shape doorway, double time) {
        Rectangle2D box = doorway.getBounds2D();
        double w = box.getWidth();
        float cx = (float) box.getCenterX();
        float cy = (float) box.getCenterY();
        double pulse = 0.5 + 0.5 * Math.sin(time * 3);

        double glow = w * (0.8 + 0.1 * pulse);
        Painter.circle(g, new RadialGradientPaint(cx, cy, (float) glow, new float[]{0f, 1f},
                new Color[]{Palette.alpha(Palette.GLOW, 0.8), Palette.alpha(Palette.GLOW, 0)}), cx, cy, glow);

        g.setPaint(new RadialGradientPaint(cx, (float) (box.getMaxY() - w * 0.3), (float) box.getHeight(),
                new float[]{0f, 1f}, new Color[]{Palette.GLOW, Palette.GLOW_EDGE}));
        g.fill(doorway);

        Path2D.Double leaf = new Path2D.Double();
        leaf.moveTo(box.getX(), box.getY() + w * 0.5);
        leaf.lineTo(box.getX() + w * 0.22, box.getY() + w * 0.62);
        leaf.lineTo(box.getX() + w * 0.22, box.getMaxY() - w * 0.08);
        leaf.lineTo(box.getX(), box.getMaxY());
        leaf.closePath();
        g.setColor(Palette.WOOD_DARK);
        g.fill(leaf);

        for (int i = 0; i < 4; i++) {
            double twinkle = Math.max(0, Math.sin(time * 2.5 + i * 1.7));
            double sx = box.getX() + w * (0.35 + 0.18 * (i % 3));
            double sy = box.getY() + box.getHeight() * (0.3 + 0.15 * i);
            Painter.circle(g, Palette.alpha(Color.WHITE, twinkle), sx, sy, w * 0.04 * (0.5 + twinkle));
        }
    }
}
