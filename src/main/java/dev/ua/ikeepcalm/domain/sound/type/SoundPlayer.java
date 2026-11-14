package dev.ua.ikeepcalm.domain.sound.type;

import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;

public interface SoundPlayer {
    void play(GameEvent event);
}
