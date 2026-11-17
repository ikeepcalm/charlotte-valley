package dev.ua.ikeepcalm.domain.level.model;

import dev.ua.ikeepcalm.domain.level.model.source.GameEvent;
import dev.ua.ikeepcalm.domain.level.type.LevelListener;
import dev.ua.ikeepcalm.domain.level.type.tile.AirTile;
import dev.ua.ikeepcalm.domain.level.type.tile.ExitTile;
import dev.ua.ikeepcalm.domain.level.type.tile.Tile;
import lombok.Getter;
import lombok.Setter;

public class Level {
    private final Tile[][] grid;
    @Setter
    private LevelListener listener = (event, level) -> {
    };
    @Getter
    private final ExitTile exit;
    @Getter
    private final Position exitPosition;
    @Getter
    private final int totalFlowers;
    @Getter
    @Setter
    private Position player;
    @Getter
    private int flowersLeft;

    public Level(Tile[][] grid, Position player, ExitTile exit, Position exitPosition, int totalFlowers) {
        this.grid = grid;
        this.player = player;
        this.exit = exit;
        this.exitPosition = exitPosition;
        this.totalFlowers = totalFlowers;
        this.flowersLeft = totalFlowers;
    }

    public int rows() {
        return grid.length;
    }

    public int cols() {
        return grid[0].length;
    }

    public boolean contains(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows()
                && position.getCol() >= 0 && position.getCol() < cols();
    }

    public Tile tileAt(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    public void collectFlower() {
        grid[player.getRow()][player.getCol()] = new AirTile();
        flowersLeft--;
        fire(GameEvent.FLOWER_COLLECTED);
    }

    public void fire(GameEvent event) {
        listener.onEvent(event, this);
    }
}
