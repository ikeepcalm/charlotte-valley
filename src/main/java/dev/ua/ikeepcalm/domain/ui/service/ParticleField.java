package dev.ua.ikeepcalm.domain.ui.service;

import dev.ua.ikeepcalm.domain.ui.model.Particle;
import dev.ua.ikeepcalm.domain.ui.model.source.ParticleShape;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleField {
    private static final int POLLEN_COUNT = 28;

    private final List<Particle> bursts = new ArrayList<>();
    private final Particle[] pollen = new Particle[POLLEN_COUNT];
    private final Random random = new Random();
    private final int width;
    private final int height;

    public ParticleField(int width, int height) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < POLLEN_COUNT; i++) {
            pollen[i] = pollen(random.nextDouble() * 5);
        }
    }

    public void burst(ParticleShape shape, double x, double y, int count) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 70 + random.nextDouble() * 150;
            bursts.add(new Particle(shape, shape.image(i), x, y,
                    Math.cos(angle) * speed, Math.sin(angle) * speed - 110,
                    shape.scale(5 + random.nextDouble() * 6), angle, (random.nextDouble() - 0.5) * 12,
                    0.7 + random.nextDouble() * 0.6, 0));
        }
    }

    public void update(double dt) {
        bursts.forEach(p -> p.step(dt));
        bursts.removeIf(Particle::expired);
        for (int i = 0; i < POLLEN_COUNT; i++) {
            pollen[i].step(dt);
            if (pollen[i].expired()) {
                pollen[i] = pollen(0);
            }
        }
    }

    public void paint(Graphics2D g) {
        for (Particle p : pollen) {
            p.getShape().paint(g, p);
        }
        bursts.forEach(p -> p.getShape().paint(g, p));
    }

    private Particle pollen(double age) {
        ParticleShape shape = ParticleShape.POLLEN;
        return new Particle(shape, shape.image(random.nextInt()),
                random.nextDouble() * width, random.nextDouble() * height,
                8 + random.nextDouble() * 14, -4 - random.nextDouble() * 8,
                shape.scale(1.5 + random.nextDouble() * 2), 0, 0, 5 + random.nextDouble() * 6, age);
    }
}
