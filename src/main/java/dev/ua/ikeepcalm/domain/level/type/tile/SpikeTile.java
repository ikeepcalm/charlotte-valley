package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.LoweredSpikeState;
import dev.ua.ikeepcalm.domain.level.type.state.SpikeState;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;
import lombok.Setter;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class SpikeTile extends Tile {
    @Setter
    private SpikeState state = new LoweredSpikeState();

    @Override
    public void onEnter(Level level) {
        state.onEnter(level);
    }

    @Override
    public void onLeave(Level level) {
        state.onLeave(this, level);
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size, double time) {
        double inset = size * 0.12;
        var plate = new RoundRectangle2D.Double(x + inset, y + inset, size - 2 * inset, size - 2 * inset, size * 0.2, size * 0.2);
        Painter.shadow(g, x + size * 0.5, y + size * 0.86, size * 0.8, size * 0.16);
        g.setPaint(new GradientPaint(x, y + (float) inset, Palette.PLATE_TOP, x, y + size - (float) inset, Palette.PLATE));
        g.fill(plate);
        g.setColor(Palette.PLATE_EDGE);
        g.setStroke(new BasicStroke(size * 0.03f));
        g.draw(plate);
        state.paint(g, plate, time);
    }
}
