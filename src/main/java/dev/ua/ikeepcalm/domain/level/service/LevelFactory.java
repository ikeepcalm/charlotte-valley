package dev.ua.ikeepcalm.domain.level.service;

import dev.ua.ikeepcalm.domain.level.model.Level;
import dev.ua.ikeepcalm.domain.level.model.Position;
import dev.ua.ikeepcalm.domain.level.type.tile.AirTile;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.domain.level.type.tile.FlowerTile;
import dev.ua.ikeepcalm.domain.level.type.tile.SpikeTile;
import dev.ua.ikeepcalm.domain.level.type.tile.Tile;
import dev.ua.ikeepcalm.domain.level.type.tile.WallTile;

public class LevelFactory {

    public static Level create(String[] rows) {
        int cols = rows[0].length();
        Tile[][] grid = new Tile[rows.length][cols];
        Position player = null;
        ExitTile exit = null;
        int flowers = 0;

        for (int r = 0; r < rows.length; r++) {
            if (rows[r].length() != cols) {
                throw new IllegalArgumentException("Row " + r + " has " + rows[r].length() + " tiles, expected " + cols);
            }
            for (int c = 0; c < cols; c++) {
                char symbol = rows[r].charAt(c);
                switch (symbol) {
                    case 'O' -> grid[r][c] = new AirTile();
                    case 'W' -> grid[r][c] = new WallTile();
                    case 'S' -> grid[r][c] = new SpikeTile();
                    case 'F' -> {
                        grid[r][c] = new FlowerTile();
                        flowers++;
                    }
                    case 'E' -> {
                        if (exit != null) {
                            throw new IllegalArgumentException("Second exit at " + r + "," + c);
                        }
                        exit = new ExitTile();
                        grid[r][c] = exit;
                    }
                    case 'P' -> {
                        if (player != null) {
                            throw new IllegalArgumentException("Second player at " + r + "," + c);
                        }
                        player = new Position(r, c);
                        grid[r][c] = new AirTile();
                    }
                    default -> throw new IllegalArgumentException("Unknown tile '" + symbol + "' at " + r + "," + c);
                }
            }
        }

        if (player == null || exit == null || flowers == 0) {
            throw new IllegalArgumentException("Level needs a player, an exit and at least one flower");
        }
        return new Level(grid, player, exit, flowers);
    }
}
