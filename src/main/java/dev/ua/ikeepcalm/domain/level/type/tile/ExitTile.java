package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.type.state.DoorState;
import dev.ua.ikeepcalm.domain.level.type.state.LockedDoorState;
import lombok.Getter;
import lombok.Setter;

import java.awt.Graphics2D;

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
    public void paint(Graphics2D g, int x, int y, int size) {
        paintGrass(g, x, y, size);
        state.paint(g, x, y, size);
    }
}
