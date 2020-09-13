package io.fxgame.game2048;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jose Pereda
 * @author Bruno Borges
 */
public class GridOperator {

    public static final int DEFAULT_GRID_SIZE = 6;
    public static final int MIN_GRID_SIZE = 4;
    public static final int MAX_GRID_SIZE = 16;

    private final int gridSize;
    private final List<Integer> traversalX;
    private final List<Integer> traversalY;

    public GridOperator() {
        this(DEFAULT_GRID_SIZE);
    }

    public GridOperator(int gridSize) {
        if (gridSize < MIN_GRID_SIZE || gridSize > MAX_GRID_SIZE) {
            throw new IllegalArgumentException(String.format("Grid size must be of range %s and %s.", MIN_GRID_SIZE, MAX_GRID_SIZE));
        }

        this.gridSize = gridSize;
        this.traversalX = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
        this.traversalY = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
    }

    public void sortGrid(Direction direction) {
        Collections.sort(traversalX, direction.equals(Direction.RIGHT) ? Collections.reverseOrder() : Integer::compareTo);
        Collections.sort(traversalY, direction.equals(Direction.DOWN) ? Collections.reverseOrder() : Integer::compareTo);
    }

    public int traverseGrid(IntBinaryOperator func) {
        var at = new AtomicInteger();
        traversalX.forEach(t_x -> {
            traversalY.forEach(t_y -> {
                at.addAndGet(func.applyAsInt(t_x, t_y));
            });
        });

        return at.get();
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean isValidLocation(Location loc) {
        return loc.getX() >= 0 && loc.getX() < gridSize && loc.getY() >= 0 && loc.getY() < gridSize;
    }

}
