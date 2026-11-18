package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.domain.level.service.LevelManager;
import dev.ua.ikeepcalm.domain.level.service.Levels;
import dev.ua.ikeepcalm.domain.sound.service.ClipSoundAdapter;
import dev.ua.ikeepcalm.domain.sound.type.SoundPlayer;
import dev.ua.ikeepcalm.domain.ui.service.GamePanel;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CharlotteValley {

    static void main() {
        SoundPlayer sound = new ClipSoundAdapter();
        SwingUtilities.invokeLater(() -> {
            LevelManager manager = new LevelManager(Levels.ALL);
            GamePanel panel = new GamePanel(manager);
            manager.subscribe(panel);
            manager.subscribe((event, level) -> sound.play(event));

            JFrame frame = new JFrame("Charlotte Valley");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setIconImage(icon());
            frame.add(panel);
            manager.start();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static BufferedImage icon() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = Painter.smooth(image.createGraphics());
        Painter.blossom(g, 32, 34, 26, Palette.PETALS[0], 0);
        g.dispose();
        return image;
    }
}
