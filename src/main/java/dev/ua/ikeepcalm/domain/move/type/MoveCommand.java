package dev.ua.ikeepcalm.domain.move.type;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.domain.level.model.source.Direction;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
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
