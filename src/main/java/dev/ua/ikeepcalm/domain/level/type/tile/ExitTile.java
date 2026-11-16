package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.DoorState;
import dev.ua.ikeepcalm.domain.level.type.state.LockedDoorState;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;
import lombok.Getter;
import lombok.Setter;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

@Getter
@Setter
public class ExitTile extends Tile {
    private DoorState state = new LockedDoorState();

    @Override
    public boolean canEnter() {
        return state.canEnter();
    }

    @Override
    public void onEnter(Level level) {
        state.onEnter(level);
    }

    public void open(Level level) {
        state.open(this, level);
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size, double time) {
        Painter.shadow(g, x + size * 0.5, y + size * 0.92, size * 0.95, size * 0.22);
        Shape frame = arch(x + size * 0.1, y + size * 0.04, size * 0.8, size * 0.9);
        g.setPaint(new GradientPaint(x, y, Palette.STONE, x, y + size, Palette.STONE_DARK));
        g.fill(frame);
        g.setColor(Palette.STONE_DARK);
        g.setStroke(new BasicStroke(size * 0.025f));
        g.draw(frame);
        state.paint(g, arch(x + size * 0.23, y + size * 0.17, size * 0.54, size * 0.77), time);
    }

    private static Shape arch(double left, double top, double width, double height) {
        Path2D.Double shape = new Path2D.Double();
        shape.moveTo(left, top + height);
        shape.append(new Arc2D.Double(left, top, width, width, 180, -180, Arc2D.OPEN), true);
        shape.lineTo(left + width, top + height);
        shape.closePath();
        return shape;
    }
}
