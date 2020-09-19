package io.fxgame.game2048;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.util.Optional;
import java.util.Random;

/**
 * @author Bruno Borges
 */
public class Tile extends Label {

    private Integer value;
    private Location location;
    private Boolean merged;

    @Override
    public void relocate(double x, double y) {
        // Overriding relocate to fix a problem in WebFx where the tile is relocated at the let top corner of the board when created
    }

    public static Tile newRandomTile() {
        int value = new Random().nextDouble() < 0.9 ? 2 : 4;
        return new Tile(value);
    }

    public static Tile newTile(int value) {
        if (value % 2 != 0) {
            throw new IllegalArgumentException("Tile value must be multiple of 2");
        }

        return new Tile(value);
    }

    private Tile(Integer value) {
        final int squareSize = Board.CELL_SIZE - 13;
        setMinSize(squareSize, squareSize);
        setMaxSize(squareSize, squareSize);
        setPrefSize(squareSize, squareSize);
        setAlignment(Pos.CENTER);

        this.value = value;
        this.merged = false;
        setText(value.toString());
        getStyleClass().addAll("game-label", "game-tile-" + value);
        setTextFill(null);
    }

    public void merge(Tile another) {
        getStyleClass().remove("game-tile-" + value);
        this.value += another.getValue();
        setText(value.toString());
        merged = true;
        getStyleClass().add("game-tile-" + value);
    }

    public Integer getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Tile{" + "value=" + value + ", location=" + location + '}';
    }

    public boolean isMerged() {
        return merged;
    }

    public void clearMerge() {
        merged = false;
    }

    public boolean isMergeable(Optional<Tile> anotherTile) {
        return anotherTile.filter(t -> t.getValue().equals(getValue())).isPresent();
    }
}
