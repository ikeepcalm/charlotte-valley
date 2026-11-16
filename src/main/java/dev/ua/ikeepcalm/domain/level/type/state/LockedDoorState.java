package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class LockedDoorState implements DoorState {

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public void onEnter(Level level) {
    }

    @Override
    public void open(ExitTile tile, Level level) {
        tile.setState(new OpenDoorState());
        level.fire(GameEvent.EXIT_OPENED);
    }

    @Override
    public void paint(Graphics2D g, Shape doorway, double time) {
        Rectangle2D box = doorway.getBounds2D();
        double w = box.getWidth();
        g.setPaint(new GradientPaint((float) box.getX(), 0, Palette.WOOD, (float) box.getMaxX(), 0, Palette.WOOD_DARK));
        g.fill(doorway);

        Shape savedClip = g.getClip();
        g.clip(doorway);
        g.setColor(Palette.WOOD_DARK);
        g.setStroke(new BasicStroke((float) w * 0.03f));
        for (int i = 1; i < 3; i++) {
            double px = box.getX() + w * i / 3;
            g.draw(new Line2D.Double(px, box.getY(), px, box.getMaxY()));
        }
        g.setColor(Palette.IRON);
        for (double band : new double[]{0.38, 0.78}) {
            g.fill(new Rectangle2D.Double(box.getX(), box.getY() + box.getHeight() * band, w, w * 0.08));
        }
        g.setClip(savedClip);

        double cx = box.getCenterX();
        double lockTop = box.getY() + box.getHeight() * 0.52;
        g.setStroke(new BasicStroke((float) w * 0.06f));
        g.draw(new Arc2D.Double(cx - w * 0.12, lockTop - w * 0.14, w * 0.24, w * 0.28, 0, 180, Arc2D.OPEN));
        g.setColor(Palette.GOLD);
        g.fill(new RoundRectangle2D.Double(cx - w * 0.17, lockTop, w * 0.34, w * 0.28, w * 0.08, w * 0.08));
        Painter.circle(g, Palette.WOOD_DARK, cx, lockTop + w * 0.12, w * 0.04);
    }
}
