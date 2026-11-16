package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;

import java.awt.Graphics2D;
import java.awt.Shape;

public interface DoorState {
    boolean canEnter();

    void onEnter(Level level);

    void open(ExitTile tile, Level level);

    void paint(Graphics2D g, Shape doorway, double time);
}
