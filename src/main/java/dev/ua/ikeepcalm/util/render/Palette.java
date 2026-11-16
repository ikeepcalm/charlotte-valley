package dev.ua.ikeepcalm.util.render;

import java.awt.Color;

public class Palette {

    public static final Color GRASS_LIGHT = new Color(0xA5D46A);
    public static final Color GRASS_DARK = new Color(0x97C95E);

    public static final Color HEDGE_DARK = new Color(0x2F6B34);
    public static final Color HEDGE = new Color(0x3F8A42);
    public static final Color HEDGE_LIGHT = new Color(0x6DB862);
    public static final Color BERRY = new Color(0xE0475B);

    public static final Color STEM = new Color(0x4E8A33);
    public static final Color BLOSSOM_CENTER = new Color(0xFFD54F);
    public static final Color[] PETALS = {new Color(0xF27BA5), new Color(0xB98AE0), new Color(0xFF9466), new Color(0x7FB8F0)};

    public static final Color PLATE_TOP = new Color(0x969CA3);
    public static final Color PLATE = new Color(0x7A7F86);
    public static final Color PLATE_EDGE = new Color(0x4B4F55);
    public static final Color STEEL_LIGHT = new Color(0xF3F6F8);
    public static final Color STEEL_DARK = new Color(0x7D8A96);
    public static final Color DANGER = new Color(0xD84343);

    public static final Color STONE = new Color(0xC2B8A8);
    public static final Color STONE_DARK = new Color(0x8C8274);
    public static final Color WOOD = new Color(0xA86E3C);
    public static final Color WOOD_DARK = new Color(0x6F4423);
    public static final Color IRON = new Color(0x4A4950);
    public static final Color GOLD = new Color(0xFFC94D);
    public static final Color GLOW = new Color(0xFFF4C2);
    public static final Color GLOW_EDGE = new Color(0xFFB347);

    public static final Color INK = new Color(0x4A3427);
    public static final Color SHADOW = new Color(0, 0, 0, 55);

    public static Color alpha(Color color, double alpha) {
        int value = (int) Math.round(Math.clamp(alpha, 0, 1) * color.getAlpha());
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), value);
    }
}
