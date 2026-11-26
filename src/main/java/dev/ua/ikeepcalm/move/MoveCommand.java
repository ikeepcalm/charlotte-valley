package dev.ua.ikeepcalm.move;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.Position;
import dev.ua.ikeepcalm.level.source.Direction;
import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.move.type.Command;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveCommand implements Command {

    private final Level level;
    private final Direction direction;

    @Override
    public void execute() {
        Position from = level.getPlayer();
        Position to = from.plus(direction);
        if (!level.contains(to) || !level.tileAt(to).canEnter()) {
            level.fire(GameEvent.PLAYER_BLOCKED);
            return;
        }
        level.tileAt(from).onLeave(level);
        level.setPlayer(to);
        level.fire(GameEvent.PLAYER_MOVED);
        level.tileAt(to).onEnter(level);
    }
}
