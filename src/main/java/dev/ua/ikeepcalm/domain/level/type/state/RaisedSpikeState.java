package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class RaisedSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
        level.fire(GameEvent.PLAYER_DIED);
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
    }

    @Override
    public void paint(Graphics2D g, Shape plate, double time) {
        Rectangle2D box = plate.getBounds2D();
        double cell = box.getWidth() / 3;
        g.setColor(Palette.alpha(Palette.DANGER, 0.45 + 0.3 * Math.sin(time * 4)));
        g.setStroke(new BasicStroke((float) cell * 0.15f));
        g.draw(plate);

        g.setStroke(new BasicStroke((float) cell * 0.04f));
        for (int i = 0; i < 9; i++) {
            double cx = box.getX() + cell * (i % 3 + 0.5);
            double base = box.getY() + cell * (i / 3 + 0.5) + cell * 0.18;
            Path2D.Double cone = new Path2D.Double();
            cone.moveTo(cx - cell * 0.26, base);
            cone.lineTo(cx, base - cell * 0.85);
            cone.lineTo(cx + cell * 0.26, base);
            cone.closePath();
            g.setPaint(new GradientPaint((float) (cx - cell * 0.26), 0, Palette.STEEL_LIGHT, (float) (cx + cell * 0.26), 0, Palette.STEEL_DARK));
            g.fill(cone);
            g.setColor(Palette.PLATE_EDGE);
            g.draw(cone);
        }
    }
}
