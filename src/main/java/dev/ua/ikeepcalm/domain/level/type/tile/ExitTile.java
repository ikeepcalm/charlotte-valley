package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.DoorState;
import dev.ua.ikeepcalm.domain.level.type.state.LockedDoorState;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.util.Optional;

@Getter
@Setter
public class ExitTile extends Tile {
    private DoorState state = new LockedDoorState();

    @Override
    public boolean canEnter() {
        return state.canEnter();
    }

    @Override
    public void onEnter(Level level) {
        state.onEnter(level);
    }

    public void open(Level level) {
        state.open(this, level);
    }

    @Override
    public Optional<BufferedImage> sprite(double time, int seed) {
        return Optional.of(state.sprite(time));
    }
}
