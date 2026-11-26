package dev.ua.ikeepcalm.level.type;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.tile.ExitTile;

import java.awt.image.BufferedImage;

public interface DoorState {

    boolean canEnter();

    void onEnter(Level level);

    void open(ExitTile tile, Level level);

    BufferedImage sprite(double time);
}
