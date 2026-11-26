package dev.ua.ikeepcalm.level.state;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.level.tile.SpikeTile;
import dev.ua.ikeepcalm.level.type.SpikeState;
import dev.ua.ikeepcalm.render.Sprites;

import java.awt.image.BufferedImage;

public class RaisedSpikeState implements SpikeState {

    @Override
    public void onEnter(Level level) {
        level.fire(GameEvent.PLAYER_DIED);
    }

    @Override
    public void onLeave(SpikeTile tile, Level level) {
    }

    @Override
    public BufferedImage sprite(double time) {
        return Sprites.SPIKE_RAISED.at(time);
    }
}
