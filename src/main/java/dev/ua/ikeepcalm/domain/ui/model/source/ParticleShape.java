package dev.ua.ikeepcalm.domain.ui.model.source;

import dev.ua.ikeepcalm.domain.ui.model.Particle;
import dev.ua.ikeepcalm.util.render.SpriteSheet;
import dev.ua.ikeepcalm.util.render.Sprites;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@RequiredArgsConstructor
public enum ParticleShape {
    PETAL(Sprites.PETAL, 0, 4, 260, 12),
    GOLD_SPARKLE(Sprites.SPARKLE, 0, 3, -20, 11),
    RED_SPARKLE(Sprites.SPARKLE, 3, 2, -20, 11),
    POLLEN(Sprites.POLLEN, 0, 2, 0, 3) {
        @Override
        double opacity(Particle p) {
            return Math.sin(Math.PI * p.progress());
        }
    };

    private final SpriteSheet sheet;
    private final int firstFrame;
    private final int frameCount;
    @Getter
    private final double gravity;
    private final double baseSize;

    public BufferedImage image(int index) {
        return sheet.frame(firstFrame + Math.floorMod(index, frameCount));
    }

    public double scale(double size) {
        return size / baseSize;
    }

    double opacity(Particle p) {
        return 1 - p.progress();
    }

    public void paint(Graphics2D g, Particle p) {
        BufferedImage image = p.getImage();
        double width = image.getWidth() * p.getScale();
        double height = image.getHeight() * p.getScale();
        Graphics2D faded = (Graphics2D) g.create();
        faded.setComposite(AlphaComposite.SrcOver.derive((float) Math.clamp(opacity(p), 0, 1)));
        if (p.getAngle() == 0) {
            faded.drawImage(image, (int) (p.getX() - width / 2), (int) (p.getY() - height / 2), (int) width, (int) height, null);
        } else {
            faded.translate(p.getX(), p.getY());
            faded.rotate(p.getAngle());
            faded.scale(p.getScale(), p.getScale());
            faded.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);
        }
        faded.dispose();
    }
}
