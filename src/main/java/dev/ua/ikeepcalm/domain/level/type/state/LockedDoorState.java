package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.util.render.Sprites;

import java.awt.image.BufferedImage;

public class LockedDoorState implements DoorState {

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public void onEnter(Level level) {
    }

    @Override
    public void open(ExitTile tile, Level level) {
        tile.setState(new OpenDoorState());
        level.fire(GameEvent.EXIT_OPENED);
    }

    @Override
    public BufferedImage sprite(double time) {
        return Sprites.DOOR_LOCKED;
    }
}
