package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;

import java.awt.image.BufferedImage;

public interface DoorState {
    boolean canEnter();

    void onEnter(Level level);

    void open(ExitTile tile, Level level);

    BufferedImage sprite(double time);
}
