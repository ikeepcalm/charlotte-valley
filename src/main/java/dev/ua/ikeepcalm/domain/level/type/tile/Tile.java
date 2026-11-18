package dev.ua.ikeepcalm.domain.level.type.tile;

import dev.ua.ikeepcalm.domain.level.model.Level;

import java.awt.Graphics2D;

public abstract class Tile {

    public boolean canEnter() {
        return true;
    }

    public void onEnter(Level level) {
    }

    public void onLeave(Level level) {
    }

    public boolean isStatic() {
        return false;
    }

    public abstract void paint(Graphics2D g, int x, int y, int size, double time);
}
