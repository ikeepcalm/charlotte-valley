package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.LoweredSpikeState;
import dev.ua.ikeepcalm.domain.level.type.state.SpikeState;
import lombok.Setter;

import java.awt.Graphics2D;

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
    public void paint(Graphics2D g, int x, int y, int size) {
        paintGrass(g, x, y, size);
        state.paint(g, x, y, size);
    }
}
