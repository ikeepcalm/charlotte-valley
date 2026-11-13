package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;

import java.awt.Color;
import java.awt.Graphics2D;

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
    public void paint(Graphics2D g, int x, int y, int size) {
        int pad = size / 6;
        g.setColor(new Color(0x212121));
        g.fillRoundRect(x + pad, y + pad, size - 2 * pad, size - pad, pad, pad);
        g.setColor(new Color(0xFFF59D));
        g.fillRect(x + pad * 2, y + pad * 2, size - 4 * pad, size - 2 * pad);
    }
}
