package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.util.render.Sprites;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Optional;

@RequiredArgsConstructor
public class FlowerTile extends Tile {
    private final int ordinal;

    @Override
    public void onEnter(Level level) {
        level.collectFlower();
    }

    @Override
    public Optional<BufferedImage> sprite(double time, int seed) {
        var sheet = Sprites.FLOWERS[ordinal % Sprites.FLOWERS.length];
        return Optional.of(sheet.at(time + ordinal * 0.37));
    }
}
