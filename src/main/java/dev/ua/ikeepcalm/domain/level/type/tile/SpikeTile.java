package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.LoweredSpikeState;
import dev.ua.ikeepcalm.domain.level.type.state.SpikeState;
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
