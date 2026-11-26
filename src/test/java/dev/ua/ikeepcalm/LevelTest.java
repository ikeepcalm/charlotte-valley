package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.LevelFactory;
import dev.ua.ikeepcalm.level.LevelManager;
import dev.ua.ikeepcalm.level.Levels;
import dev.ua.ikeepcalm.level.Position;
import dev.ua.ikeepcalm.level.source.Direction;
import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.level.state.OpenDoorState;
import dev.ua.ikeepcalm.move.MoveCommand;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private final List<GameEvent> events = new ArrayList<>();

    private LevelManager start(String... rows) {
        LevelManager manager = new LevelManager(List.of(rows, Levels.ALL.getFirst()));
        manager.subscribe((event, level) -> events.add(event));
        manager.start();
        return manager;
    }

    private void move(LevelManager manager, Direction... directions) {
        for (Direction direction : directions) {
            manager.execute(new MoveCommand(manager.getCurrent(), direction));
        }
    }

    @Test
    void factoryBuildsLevelFromMatrix() {
        Level level = LevelFactory.create(new String[]{"WFW", "PSE"});
        assertEquals(1, level.getTotalFlowers());
        assertEquals(new Position(1, 0), level.getPlayer());
        assertThrows(IllegalArgumentException.class, () -> LevelFactory.create(new String[]{"PFEX"}));
        assertThrows(IllegalArgumentException.class, () -> LevelFactory.create(new String[]{"PFE", "OO"}));
    }

    @Test
    void wallBlocksMovement() {
        LevelManager manager = start("WWW", "WPF", "WWE");
        move(manager, Direction.UP);
        assertEquals(new Position(1, 1), manager.getCurrent().getPlayer());
        assertTrue(events.contains(GameEvent.PLAYER_BLOCKED));
    }

    @Test
    void lockedExitBlocksUntilLastFlowerCollected() {
        LevelManager manager = start("PEF");
        move(manager, Direction.RIGHT);
        assertEquals(new Position(0, 0), manager.getCurrent().getPlayer());

        manager = start("POF", "OEO");
        move(manager, Direction.RIGHT, Direction.RIGHT);
        assertInstanceOf(OpenDoorState.class, manager.getCurrent().getExit().getState());
        assertTrue(events.contains(GameEvent.EXIT_OPENED));
    }

    @Test
    void returningToUsedSpikeKillsAndRestarts() {
        LevelManager manager = start("PSOF", "OOOE");
        move(manager, Direction.RIGHT, Direction.RIGHT, Direction.LEFT);
        assertTrue(events.contains(GameEvent.PLAYER_DIED));
        assertEquals(new Position(0, 0), manager.getCurrent().getPlayer());
    }

    @Test
    void openExitCompletesLevel() {
        LevelManager manager = start("PFE");
        move(manager, Direction.RIGHT, Direction.RIGHT);
        assertTrue(events.contains(GameEvent.LEVEL_COMPLETED));
        assertEquals(1, manager.getIndex());
    }
}
