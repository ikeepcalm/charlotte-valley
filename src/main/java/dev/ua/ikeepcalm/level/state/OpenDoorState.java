package dev.ua.ikeepcalm.level.state;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.level.tile.ExitTile;
import dev.ua.ikeepcalm.level.type.DoorState;
import dev.ua.ikeepcalm.render.Sprites;

import java.awt.image.BufferedImage;

public class OpenDoorState implements DoorState {

    @Override
    public boolean canEnter() {
        return true;
    }

    @Override
    public void onEnter(Level level) {
        level.fire(GameEvent.LEVEL_COMPLETED);
    }

    @Override
    public void open(ExitTile tile, Level level) {
    }

    @Override
    public BufferedImage sprite(double time) {
        return Sprites.DOOR_OPEN.at(time);
    }
}
