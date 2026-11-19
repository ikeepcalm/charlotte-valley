package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.util.render.Sprites;

import java.awt.image.BufferedImage;

public class LoweredSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
        tile.setState(new RaisedSpikeState());
    }

    @Override
    public BufferedImage sprite(double time) {
        return Sprites.SPIKE_LOWERED;
    }
}
