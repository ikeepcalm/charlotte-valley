package dev.ua.ikeepcalm.domain.level.type.tile;

import java.awt.image.BufferedImage;
import java.util.Optional;

public class AirTile extends Tile {

    @Override
    public Optional<BufferedImage> sprite(double time, int seed) {
        return Optional.empty();
    }
}
