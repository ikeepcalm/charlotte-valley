package dev.ua.ikeepcalm.domain.level.type;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;

public interface LevelListener {
    void onEvent(GameEvent event, Level level);
}
