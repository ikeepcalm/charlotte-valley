package dev.ua.ikeepcalm.ui;

import dev.ua.ikeepcalm.level.Position;
import dev.ua.ikeepcalm.render.Sprites;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static dev.ua.ikeepcalm.render.Sprites.PAD;
import static dev.ua.ikeepcalm.render.Sprites.TILE;

public class PlayerSprite {

    private static final double HOP_SECONDS = 0.16;
    private static final double LAND_SECONDS = 0.12;
    private static final double BUMP_SECONDS = 0.25;
    private static final double BLINK_SECONDS = 0.12;
    private static final int FACING_DOWN = 0;
    private static final int FACING_LEFT = 1;
    private static final int FACING_RIGHT = 2;
    private static final int FACING_UP = 3;
    private static final int BLINK_FRAMES = 4;
    private static final double FEET_X = 0.5;
    private static final double FEET_Y = 0.8;

    private Position target = new Position(0, 0);
    private double row;
    private double col;
    private int facing = FACING_DOWN;
    private double sinceHop = 1;
    private double sinceBump = 1;
    private double sinceBlink = 1;
    private double untilBlink = 2;

    public void snap(Position position) {
        target = position;
        row = position.getRow();
        col = position.getCol();
        facing = FACING_DOWN;
        sinceHop = 1;
    }

    public void moveTo(Position position) {
        if (position.getRow() > target.getRow()) {
            facing = FACING_DOWN;
        } else if (position.getRow() < target.getRow()) {
            facing = FACING_UP;
        } else {
            facing = position.getCol() < target.getCol() ? FACING_LEFT : FACING_RIGHT;
        }
        target = position;
        sinceHop = 0;
    }

    public void bump() {
        sinceBump = 0;
    }

    public void update(double dt) {
        double ease = Math.min(1, dt * 18);
        row += (target.getRow() - row) * ease;
        col += (target.getCol() - col) * ease;
        sinceHop += dt;
        sinceBump += dt;
        sinceBlink += dt;
        untilBlink -= dt;
        if (untilBlink <= 0) {
            sinceBlink = 0;
            untilBlink = 2.2 + ThreadLocalRandom.current().nextDouble() * 2.5;
        }
    }

    public void paint(Graphics2D g, int originX, int originY) {
        double hop = sinceHop < HOP_SECONDS ? Math.sin(Math.PI * sinceHop / HOP_SECONDS) : 0;
        double landing = sinceHop - HOP_SECONDS;
        double squash = landing > 0 && landing < LAND_SECONDS ? 1 + 0.14 * Math.sin(Math.PI * landing / LAND_SECONDS) : 1;
        double wiggle = sinceBump < BUMP_SECONDS ? Math.sin(sinceBump * 55) * TILE * 0.05 * (1 - sinceBump / BUMP_SECONDS) : 0;
        double feetX = originX + (col + FEET_X) * TILE + wiggle;
        double feetY = originY + (row + FEET_Y) * TILE;

        int shadow = (int) (TILE * (1 - hop * 0.3));
        g.drawImage(Sprites.SHADOW, (int) feetX - shadow / 2, (int) feetY - shadow / 2, shadow, shadow, null);

        BufferedImage sprite = Sprites.CHARLOTTE.frame(facing + (sinceBlink < BLINK_SECONDS ? BLINK_FRAMES : 0));
        double anchorX = PAD + FEET_X * TILE;
        double anchorY = PAD + FEET_Y * TILE;
        double lift = hop * TILE * 0.22;
        if (squash == 1) {
            g.drawImage(sprite, (int) Math.round(feetX - anchorX), (int) Math.round(feetY - lift - anchorY), null);
            return;
        }
        Graphics2D squashed = (Graphics2D) g.create();
        squashed.translate(feetX, feetY - lift);
        squashed.scale(squash, 1 / squash);
        squashed.translate(-anchorX, -anchorY);
        squashed.drawImage(sprite, 0, 0, null);
        squashed.dispose();
    }
}
