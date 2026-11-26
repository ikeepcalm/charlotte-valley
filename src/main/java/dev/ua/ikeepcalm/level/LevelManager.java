package dev.ua.ikeepcalm.level;

import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.level.type.LevelListener;
import dev.ua.ikeepcalm.move.type.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LevelManager implements LevelListener {

    private final List<String[]> levels;
    private final List<LevelListener> subscribers = new ArrayList<>();

    @Getter
    private Level current;

    @Getter
    private int index;

    private Integer pendingLevel;

    public void subscribe(LevelListener listener) {
        subscribers.add(listener);
    }

    public void start() {
        load(0);
    }

    public int levelCount() {
        return levels.size();
    }

    public int maxRows() {
        return levels.stream().mapToInt(rows -> rows.length).max().orElseThrow();
    }

    public int maxCols() {
        return levels.stream().mapToInt(rows -> rows[0].length()).max().orElseThrow();
    }

    public void restart() {
        load(index);
    }

    public void execute(Command command) {
        command.execute();
        if (pendingLevel != null) {
            int next = pendingLevel;
            pendingLevel = null;
            load(next);
        }
    }

    @Override
    public void onEvent(GameEvent event, Level level) {
        subscribers.forEach(subscriber -> subscriber.onEvent(event, level));
        switch (event) {
            case FLOWER_COLLECTED -> {
                if (level.getFlowersLeft() == 0) {
                    level.getExit().open(level);
                }
            }
            case PLAYER_DIED -> pendingLevel = index;
            case LEVEL_COMPLETED -> {
                if (index == levels.size() - 1) {
                    level.fire(GameEvent.GAME_WON);
                }
                pendingLevel = (index + 1) % levels.size();
            }
            default -> {}
        }
    }

    private void load(int levelIndex) {
        index = levelIndex;
        current = LevelFactory.create(levels.get(levelIndex));
        current.setListener(this);
        current.fire(GameEvent.LEVEL_STARTED);
    }
}
