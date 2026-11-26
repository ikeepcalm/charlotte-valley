package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.level.Level;
import dev.ua.ikeepcalm.render.Palette;
import dev.ua.ikeepcalm.render.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Hud {

    private static final Font TITLE = new Font("Georgia", Font.BOLD | Font.ITALIC, 26);
    private static final Font LABEL = new Font("Georgia", Font.ITALIC, 16);
    private static final Font BANNER = new Font("Georgia", Font.BOLD, 44);
    private static final Font HINT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final int BAR_HEIGHT = 58;

    private BufferedImage bar;

    public void paintBar(Graphics2D g, int width, Level level, int levelIndex, int levelCount) {
        if (bar == null || bar.getWidth() != width - 32) {
            bar = new BufferedImage(width - 32, BAR_HEIGHT + Sprites.RIBBON_SHADOW, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D canvas = bar.createGraphics();
            Sprites.drawRibbon(canvas, 0, 0, bar.getWidth(), BAR_HEIGHT);
            canvas.dispose();
        }
        g.drawImage(bar, 16, 14, null);
        g.setColor(Palette.INK);
        g.setFont(TITLE);
        g.drawString("Charlotte Valley", 36, 51);
        int titleWidth = g.getFontMetrics().stringWidth("Charlotte Valley");

        g.setFont(LABEL);
        g.setColor(Palette.alpha(Palette.INK, 0.65));
        g.drawString("Level " + (levelIndex + 1) + " of " + levelCount, 36 + titleWidth + 22, 50);

        int total = level.getTotalFlowers();
        int collected = total - level.getFlowersLeft();
        for (int i = 0; i < total; i++) {
            int x = width - 57 - (total - 1 - i) * 32;
            g.drawImage(i < collected ? Sprites.BLOSSOM.frame(i) : Sprites.SLOT, x, 30, null);
        }
    }

    public void paintHint(Graphics2D g, int width, int height) {
        g.setFont(HINT);
        g.setColor(Palette.alpha(Palette.INK, 0.6));
        centered(g, "Arrow keys or WASD to move   ·   R to restart", width / 2, height - 16);
    }

    public void paintBanner(Graphics2D g, int width, int height, String title, String subtitle, double opacity) {
        g.setFont(BANNER);
        int bannerWidth = Math.max(g.getFontMetrics().stringWidth(title), g.getFontMetrics(LABEL).stringWidth(subtitle)) + 80;
        int top = height / 2 - 60;
        Graphics2D faded = (Graphics2D) g.create();
        faded.setComposite(AlphaComposite.SrcOver.derive((float) opacity));
        Sprites.drawRibbon(faded, (width - bannerWidth) / 2, top, bannerWidth, 112);
        faded.dispose();
        g.setColor(Palette.alpha(Palette.INK, opacity));
        centered(g, title, width / 2, top + 58);
        g.setFont(LABEL);
        g.setColor(Palette.alpha(Palette.INK, opacity * 0.7));
        centered(g, subtitle, width / 2, top + 90);
    }

    private void centered(Graphics2D g, String text, int cx, int baseline) {
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, cx - metrics.stringWidth(text) / 2, baseline);
    }
}
