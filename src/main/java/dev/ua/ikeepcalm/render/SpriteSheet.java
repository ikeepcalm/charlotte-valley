package dev.ua.ikeepcalm.render;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final BufferedImage[] frames;
    private final double fps;

    public SpriteSheet(BufferedImage sheet, double fps) {
        int size = sheet.getHeight();
        frames = new BufferedImage[sheet.getWidth() / size];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = sheet.getSubimage(i * size, 0, size, size);
        }
        this.fps = fps;
    }

    public BufferedImage frame(int index) {
        return frames[Math.floorMod(index, frames.length)];
    }

    public BufferedImage at(double time) {
        return frame((int) Math.floor(time * fps));
    }
}
