package io.fxgame.game2048;

import javafx.scene.input.KeyCode;

/**
 * @author Bruno Borges
 */
public enum Direction {

    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    private final int y;
    private final int x;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Direction{" + "y=" + y + ", x=" + x + '}' + name();
    }

    protected static Direction valueFor(KeyCode keyCode) {
        return valueOf(keyCode.name());
    }
}
