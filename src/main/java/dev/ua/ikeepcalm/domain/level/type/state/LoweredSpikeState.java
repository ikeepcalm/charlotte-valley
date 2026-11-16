package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class LoweredSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
        tile.setState(new RaisedSpikeState());
    }

    @Override
    public void paint(Graphics2D g, Shape plate, double time) {
        Rectangle2D box = plate.getBounds2D();
        double cell = box.getWidth() / 3;
        for (int i = 0; i < 9; i++) {
            double cx = box.getX() + cell * (i % 3 + 0.5);
            double cy = box.getY() + cell * (i / 3 + 0.5);
            g.setColor(Palette.PLATE_EDGE);
            g.fill(new Ellipse2D.Double(cx - cell * 0.22, cy - cell * 0.13, cell * 0.44, cell * 0.26));
            Painter.circle(g, Palette.STEEL_DARK, cx, cy - cell * 0.02, cell * 0.07);
        }
    }
}
