package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;

import java.awt.Color;
import java.awt.Graphics2D;

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
    public void paint(Graphics2D g, int x, int y, int size) {
        int pad = size / 6;
        g.setColor(new Color(0x795548));
        g.fillRoundRect(x + pad, y + pad, size - 2 * pad, size - pad, pad, pad);
        g.setColor(new Color(0x4E342E));
        g.drawLine(x + size / 2, y + pad, x + size / 2, y + size);
        g.setColor(new Color(0xFFC107));
        g.fillRect(x + size / 2 - pad / 2, y + size / 2, pad, pad);
    }
}
