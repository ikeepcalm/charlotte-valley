package dev.ua.ikeepcalm.level.tile;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.state.LoweredSpikeState;
import dev.ua.ikeepcalm.level.type.SpikeState;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class SpikeTile extends Tile {

    @Getter
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
    public Optional<BufferedImage> sprite(double time, int seed) {
        return Optional.of(state.sprite(time));
    }
}
