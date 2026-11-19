package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;

import java.awt.image.BufferedImage;

public interface SpikeState {
    void onEnter(Level level);

    void onLeave(SpikeTile tile, Level level);

    BufferedImage sprite(double time);
}
