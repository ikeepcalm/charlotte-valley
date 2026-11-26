package dev.ua.ikeepcalm.level.tile;

import dev.ua.ikeepcalm.level.Level;

import java.awt.image.BufferedImage;
import java.util.Optional;

public abstract class Tile {

    public boolean canEnter() {
        return true;
    }

    public void onEnter(Level level) {
    }

    public void onLeave(Level level) {
    }

    public boolean isStatic() {
        return false;
    }

    public abstract Optional<BufferedImage> sprite(double time, int seed);
}
