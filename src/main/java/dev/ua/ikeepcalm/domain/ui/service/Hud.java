package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Hud {
    private static final Font TITLE = new Font("Georgia", Font.BOLD | Font.ITALIC, 26);
    private static final Font LABEL = new Font("Georgia", Font.ITALIC, 16);
    private static final Font BANNER = new Font("Georgia", Font.BOLD, 44);
    private static final Font HINT = new Font("Segoe UI", Font.PLAIN, 13);

    public void paintBar(Graphics2D g, int width, Level level, int levelIndex, int levelCount) {
        ribbon(g, 16, 14, width - 32, 58, 1);
        g.setColor(Palette.INK);
        g.setFont(TITLE);
        g.drawString("Charlotte Valley", 36, 51);
        int titleWidth = g.getFontMetrics().stringWidth("Charlotte Valley");
        g.setFont(LABEL);
        g.setColor(Palette.alpha(Palette.INK, 0.65));
        g.drawString("Level " + (levelIndex + 1) + " of " + levelCount, 36 + titleWidth + 22, 50);

        int total = level.getTotalFlowers();
        int collected = total - level.getFlowersLeft();
        g.setStroke(new BasicStroke(2f));
        for (int i = 0; i < total; i++) {
            double cx = width - 44 - (total - 1 - i) * 32;
            if (i < collected) {
                Painter.blossom(g, cx, 43, 11, Palette.PETALS[i % Palette.PETALS.length], 0);
            } else {
                g.setColor(Palette.alpha(Palette.INK, 0.25));
                g.draw(new Ellipse2D.Double(cx - 10, 33, 20, 20));
            }
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
        ribbon(g, (width - bannerWidth) / 2, top, bannerWidth, 112, opacity);
        g.setColor(Palette.alpha(Palette.INK, opacity));
        centered(g, title, width / 2, top + 58);
        g.setFont(LABEL);
        g.setColor(Palette.alpha(Palette.INK, opacity * 0.7));
        centered(g, subtitle, width / 2, top + 90);
    }

    private void ribbon(Graphics2D g, int x, int y, int width, int height, double opacity) {
        g.setColor(Palette.alpha(Palette.SHADOW, opacity));
        g.fill(new RoundRectangle2D.Double(x, y + 4, width, height, 22, 22));
        var body = new RoundRectangle2D.Double(x, y, width, height, 22, 22);
        g.setColor(Palette.alpha(Palette.PARCHMENT, opacity * 0.94));
        g.fill(body);
        g.setColor(Palette.alpha(Palette.GOLD, opacity * 0.8));
        g.setStroke(new BasicStroke(2f));
        g.draw(new RoundRectangle2D.Double(x + 5, y + 5, width - 10, height - 10, 16, 16));
    }

    private void centered(Graphics2D g, String text, int cx, int baseline) {
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, cx - metrics.stringWidth(text) / 2, baseline);
    }
}
