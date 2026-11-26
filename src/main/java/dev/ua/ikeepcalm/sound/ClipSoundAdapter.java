package dev.ua.ikeepcalm.sound;

import dev.ua.ikeepcalm.level.source.GameEvent;
import dev.ua.ikeepcalm.sound.type.SoundPlayer;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class ClipSoundAdapter implements SoundPlayer {

    private static final System.Logger LOGGER = System.getLogger(ClipSoundAdapter.class.getName());

    private static final Map<GameEvent, String> FILES = Map.of(
            GameEvent.PLAYER_MOVED, "step",
            GameEvent.PLAYER_BLOCKED, "bump",
            GameEvent.FLOWER_COLLECTED, "flower",
            GameEvent.EXIT_OPENED, "door",
            GameEvent.PLAYER_DIED, "death",
            GameEvent.LEVEL_COMPLETED, "win"
    );

    private final Map<GameEvent, Clip> clips = new EnumMap<>(GameEvent.class);

    public ClipSoundAdapter() {
        try {
            for (var entry : FILES.entrySet()) {
                clips.put(entry.getKey(), load(entry.getValue()));
            }
        } catch (LineUnavailableException | IllegalArgumentException e) {
            clips.clear();
            LOGGER.log(System.Logger.Level.WARNING, "No audio device, sound disabled", e);
        }
    }

    private static Clip load(String name) throws LineUnavailableException {
        String path = "/sounds/" + name + ".wav";
        URL resource = ClipSoundAdapter.class.getResource(path);
        if (resource == null) {
            throw new IllegalStateException("Missing sound resource " + path);
        }
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(resource)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (IOException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("Cannot load sound " + path, e);
        }
    }

    @Override
    public void play(GameEvent event) {
        Clip clip = clips.get(event);
        if (clip == null) {
            return;
        }
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
