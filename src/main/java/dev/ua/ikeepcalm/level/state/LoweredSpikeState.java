package dev.ua.ikeepcalm.level.state;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.tile.SpikeTile;
import dev.ua.ikeepcalm.level.type.SpikeState;
import dev.ua.ikeepcalm.render.Sprites;

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
