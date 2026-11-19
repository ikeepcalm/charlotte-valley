package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.domain.level.model.source.Direction;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.service.LevelManager;
import dev.ua.ikeepcalm.domain.level.service.Levels;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.domain.level.type.tile.Tile;
import dev.ua.ikeepcalm.domain.move.type.MoveCommand;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelsTest {
    private static final String WON = "won";

    static Stream<Arguments> levels() {
        return Levels.ALL.stream().map(rows -> Arguments.of((Object) rows));
    }

    @ParameterizedTest(name = "level {index} can be solved")
    @MethodSource("levels")
    void levelCanBeSolved(String[] rows) {
        assertTrue(solvable(rows), "no route clears this level");
    }

    private static boolean solvable(String[] rows) {
        Deque<String> queue = new ArrayDeque<>(List.of(""));
        Set<String> seen = new HashSet<>(Set.of(play(rows, "")));
        while (!queue.isEmpty()) {
            String path = queue.poll();
            for (Direction direction : Direction.values()) {
                String next = path + direction.ordinal();
                String state = play(rows, next);
                if (WON.equals(state)) {
                    return true;
                }
                if (state != null && seen.add(state)) {
                    queue.add(next);
                }
            }
        }
        return false;
    }

    private static String play(String[] rows, String moves) {
        LevelManager manager = new LevelManager(List.<String[]>of(rows));
        List<GameEvent> events = new ArrayList<>();
        manager.subscribe((event, level) -> events.add(event));
        manager.start();
        for (char move : moves.toCharArray()) {
            manager.execute(new MoveCommand(manager.getCurrent(), Direction.values()[move - '0']));
            if (events.contains(GameEvent.LEVEL_COMPLETED)) {
                return WON;
            }
            if (events.contains(GameEvent.PLAYER_DIED)) {
                return null;
            }
        }
        return stateOf(manager.getCurrent());
    }

    private static String stateOf(Level level) {
        StringBuilder state = new StringBuilder(level.getPlayer().toString());
        for (int r = 0; r < level.rows(); r++) {
            for (int c = 0; c < level.cols(); c++) {
                Tile tile = level.tileAt(new Position(r, c));
                state.append(tile instanceof SpikeTile spike ? spike.getState().getClass().getSimpleName() : tile.getClass().getSimpleName());
            }
        }
        return state.toString();
    }
}
