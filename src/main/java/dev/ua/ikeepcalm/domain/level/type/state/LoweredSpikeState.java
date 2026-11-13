package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;

import java.awt.Color;
import java.awt.Graphics2D;

public class LoweredSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
        tile.setState(new RaisedSpikeState());
    }

    @Override
    public void paint(Graphics2D g, int x, int y, int size) {
        int hole = size / 8;
        g.setColor(new Color(0x616161));
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                g.fillOval(x + i * size / 4 - hole / 2, y + j * size / 4 - hole / 2, hole, hole);
            }
        }
    }
}
