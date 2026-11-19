package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.util.render.Sprites;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class WallTile extends Tile {

    @Override
    public boolean canEnter() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public Optional<BufferedImage> sprite(double time, int seed) {
        return Optional.of(Sprites.HEDGE.frame(seed));
    }
}
