package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;

import java.awt.Color;
import java.awt.Graphics2D;

public class RaisedSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
        level.fire(GameEvent.PLAYER_DIED);
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        int width = size / 4;
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= 3; j++) {
                int left = x + size / 8 + i * width;
                int base = y + j * size / 4 + size / 10;
                int[] xs = {left, left + width / 2, left + width};
                int[] ys = {base, base - size / 5, base};
                g.setColor(new Color(0xBDBDBD));
                g.fillPolygon(xs, ys, 3);
                g.setColor(new Color(0x424242));
                g.drawPolygon(xs, ys, 3);
            }
        }
    }
}
