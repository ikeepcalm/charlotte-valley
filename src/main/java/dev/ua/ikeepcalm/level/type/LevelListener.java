package dev.ua.ikeepcalm.level.type;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.level.source.GameEvent;

public interface LevelListener {

    void onEvent(GameEvent event, Level level);

}
