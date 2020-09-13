package io.fxgame.game2048;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @author Bruno Borges
 */
public class GamePane extends StackPane {

    private GameManager gameManager;
    private Bounds gameBounds;

    static {
        // Downloaded from https://01.org/clear-sans/blogs
        // The font may be used and redistributed under the terms of the Apache License 2.0
        Font.loadFont(Game2048.class.getResource("ClearSans-Bold.ttf").toExternalForm(), 10.0);
    }

    public GamePane() {
        gameManager = new GameManager(UserSettings.LOCAL.getGridSize());
        gameBounds = gameManager.getLayoutBounds();

        getChildren().add(gameManager);

        getStyleClass().addAll("game-root");
        ChangeListener<Number> resize = (ov, v, v1) -> {
            double scale = Math.min((getWidth() - UserSettings.MARGIN) / gameBounds.getWidth(),
                    (getHeight() - UserSettings.MARGIN) / gameBounds.getHeight());
            gameManager.setScale(scale);
            gameManager.setLayoutX((getWidth() - gameBounds.getWidth()) / 2d);
            gameManager.setLayoutY((getHeight() - gameBounds.getHeight()) / 2d);
        };
        widthProperty().addListener(resize);
        heightProperty().addListener(resize);

        addKeyHandlers();
        addSwipeHandlers();
        setFocusTraversable(true);
        setOnMouseClicked(e -> requestFocus());
    }

    private BooleanProperty cmdCtrlKeyPressed = new SimpleBooleanProperty(false);

    private void addKeyHandlers() {
        setOnKeyPressed(ke -> {
            var keyCode = ke.getCode();
            switch (keyCode) {
                case CONTROL:
                case COMMAND: cmdCtrlKeyPressed.set(true); break;
                case S: gameManager.saveSession(); break;
                case R: gameManager.restoreSession(); break;
                case P: gameManager.pauseGame(); break;
                case Q: if(!cmdCtrlKeyPressed.get()) gameManager.quitGame(); break;
                case F: {
                    var stage = ((Stage) getScene().getWindow());
                    stage.setFullScreen(!stage.isFullScreen()); 
                    break;
                }
                default: if(keyCode.isArrowKey()) move(Direction.valueFor(keyCode)); break;
            }
        });

        setOnKeyReleased(ke -> {
            var keyCode = ke.getCode();

            if (keyCode.equals(KeyCode.CONTROL) || keyCode.equals(KeyCode.COMMAND)) {
                cmdCtrlKeyPressed.set(false);
                return;
            }
        });
    }

    private void addSwipeHandlers() {
        setOnSwipeUp(e -> move(Direction.UP));
        setOnSwipeRight(e -> move(Direction.RIGHT));
        setOnSwipeLeft(e -> move(Direction.LEFT));
        setOnSwipeDown(e -> move(Direction.DOWN));
    }

    private void move(Direction direction) {
        gameManager.move(direction);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

}
