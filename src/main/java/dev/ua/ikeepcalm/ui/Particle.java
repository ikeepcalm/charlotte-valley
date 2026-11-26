package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.ui.source.ParticleShape;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.image.BufferedImage;

@Getter
@AllArgsConstructor
public class Particle {

    private final ParticleShape shape;
    private final BufferedImage image;
    private final double scale;
    private final double spin;
    private final double life;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double angle;
    private double age;

    public void step(double dt) {
        age += dt;
        x += vx * dt;
        y += vy * dt;
        vy += shape.getGravity() * dt;
        vx *= 1 - dt * 1.5;
        angle += spin * dt;
    }

    public boolean expired() {
        return age >= life;
    }

    public double progress() {
        return age / life;
    }
}
