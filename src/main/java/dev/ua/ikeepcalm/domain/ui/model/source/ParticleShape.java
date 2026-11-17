package dev.ua.ikeepcalm.domain.ui.model.source;

import dev.ua.ikeepcalm.domain.ui.model.Particle;
import dev.ua.ikeepcalm.util.render.Painter;
import dev.ua.ikeepcalm.util.render.Palette;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

@Getter
@RequiredArgsConstructor
public enum ParticleShape {
    PETAL(260) {
        @Override
        void draw(Graphics2D g, Particle p, double opacity) {
            g.setColor(Palette.alpha(p.getColor(), opacity));
            g.fill(new Ellipse2D.Double(-p.getSize() / 2, -p.getSize() / 4, p.getSize(), p.getSize() / 2));
        }
    },
    SPARKLE(-20) {
        @Override
        void draw(Graphics2D g, Particle p, double opacity) {
            double r = p.getSize();
            Path2D.Double star = new Path2D.Double();
            star.moveTo(0, -r);
            star.quadTo(0, 0, r, 0);
            star.quadTo(0, 0, 0, r);
            star.quadTo(0, 0, -r, 0);
            star.quadTo(0, 0, 0, -r);
            g.setColor(Palette.alpha(p.getColor(), opacity));
            g.fill(star);
        }
    },
    POLLEN(0) {
        @Override
        void draw(Graphics2D g, Particle p, double opacity) {
            Painter.circle(g, Palette.alpha(p.getColor(), opacity * 0.35), 0, 0, p.getSize() * 1.8);
            Painter.circle(g, Palette.alpha(p.getColor(), opacity * 0.9), 0, 0, p.getSize() * 0.6);
        }

        @Override
        double opacity(Particle p) {
            return Math.sin(Math.PI * p.progress());
        }
    };

    private final double gravity;

    abstract void draw(Graphics2D g, Particle p, double opacity);

    double opacity(Particle p) {
        return 1 - p.progress();
    }

    public void paint(Graphics2D g, Particle p) {
        AffineTransform saved = g.getTransform();
        g.translate(p.getX(), p.getY());
        g.rotate(p.getAngle());
        draw(g, p, opacity(p));
        g.setTransform(saved);
    }
}
