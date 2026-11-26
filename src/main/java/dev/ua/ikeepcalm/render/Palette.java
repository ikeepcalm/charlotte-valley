package dev.ua.ikeepcalm.render;

import java.awt.Color;

public class Palette {

    public static final Color INK = new Color(0x4A3427);
    public static final Color GLOW = new Color(0xFFF4C2);

    public static Color alpha(Color color, double alpha) {
        int value = (int) Math.round(Math.clamp(alpha, 0, 1) * color.getAlpha());
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), value);
    }
}
