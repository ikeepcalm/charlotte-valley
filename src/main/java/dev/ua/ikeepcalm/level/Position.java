package dev.ua.ikeepcalm.level;

import dev.ua.ikeepcalm.level.source.Direction;
import lombok.Data;

@Data
public class Position {

    private final int row;
    private final int col;

    public Position plus(Direction direction) {
        return new Position(row + direction.getRowDelta(), col + direction.getColDelta());
    }
}
