package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.util.render.Sprites;

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
