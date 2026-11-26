package dev.ua.ikeepcalm.level.type;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.tile.SpikeTile;

import java.awt.image.BufferedImage;

public interface SpikeState {

    void onEnter(Level level);

    void onLeave(SpikeTile tile, Level level);

    BufferedImage sprite(double time);
}
