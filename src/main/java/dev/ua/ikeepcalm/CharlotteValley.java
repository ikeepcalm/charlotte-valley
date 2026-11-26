package dev.ua.ikeepcalm;

import dev.ua.ikeepcalm.level.LevelManager;
import dev.ua.ikeepcalm.level.Levels;
import dev.ua.ikeepcalm.render.Sprites;
import dev.ua.ikeepcalm.sound.ClipSoundAdapter;
import dev.ua.ikeepcalm.sound.type.SoundPlayer;
import dev.ua.ikeepcalm.ui.GamePanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
            frame.setIconImage(Sprites.ICON);
            frame.add(panel);
            manager.start();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
