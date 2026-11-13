package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;

import java.awt.Graphics2D;

public interface SpikeState {
    void onEnter(Level level);

    void onLeave(SpikeTile tile, Level level);

    void paint(Graphics2D g, int x, int y, int size);
}
