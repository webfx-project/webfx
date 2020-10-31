package io.fxgame.game2048;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Bruno Borges
 */
public class GamePane extends BorderPane {

    private GameManager gameManager;
    private Bounds gameBounds;

    public GamePane() {
        gameManager = new GameManager(UserSettings.LOCAL.getGridSize());
        gameBounds = gameManager.getLayoutBounds();

        //getChildren().add(gameManager);
        setCenter(gameManager);

        getStyleClass().addAll("game-root");
        setBackground(null);

        addKeyHandlers();
        addSwipeHandlers();
        setFocusTraversable(true);
        setOnMouseClicked(e -> requestFocus());
    }

    private BooleanProperty cmdCtrlKeyPressed = new SimpleBooleanProperty(false);

    private void addKeyHandlers() {
        sceneProperty().addListener((observable, oldValue, scene) -> {
            scene.
                    setOnKeyPressed(ke -> {
                        var keyCode = ke.getCode();
                        switch (keyCode) {
                            case CONTROL:
                            case COMMAND:
                                cmdCtrlKeyPressed.set(true);
                                break;
                            case S:
                                gameManager.saveSession();
                                break;
                            case R:
                                gameManager.restoreSession();
                                break;
                            case P:
                                gameManager.pauseGame();
                                break;
                            case Q:
                                if (!cmdCtrlKeyPressed.get()) gameManager.quitGame();
                                break;
                            case F: {
                                var stage = ((Stage) getScene().getWindow());
                                stage.setFullScreen(!stage.isFullScreen());
                                break;
                            }
                            default:
                                if (keyCode.isArrowKey()) move(Direction.valueFor(keyCode));
                                break;
                        }
                    });
            scene.
                    setOnKeyReleased(ke -> {
                        var keyCode = ke.getCode();

                        if (keyCode.equals(KeyCode.CONTROL) || keyCode.equals(KeyCode.COMMAND)) {
                            cmdCtrlKeyPressed.set(false);
                            return;
                        }
                    });
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
