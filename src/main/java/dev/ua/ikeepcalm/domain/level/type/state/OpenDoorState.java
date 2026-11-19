package dev.ua.ikeepcalm.domain.level.type.state;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.util.render.Sprites;

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
