package io.fxgame.game2048;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import webfx.extras.webtext.controls.HtmlText;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Jose Pereda
 * @author Bruno Borges
 */
public class Board extends VBox {

    public static final int CELL_SIZE = 128;
    private static final int BORDER_WIDTH = (14 + 2) / 2;
    private static final int TOP_HEIGHT = 92;
    private static final int GAP_HEIGHT = 50;
    private static final int TOOLBAR_HEIGHT = 80;

    private final IntegerProperty gameScoreProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty gameBestProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty gameMovePoints = new SimpleIntegerProperty(0);
    private final BooleanProperty gameWonProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameAboutProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gamePauseProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameTryAgainProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameSaveProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameRestoreProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameQuitProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty layerOnProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty resetGame = new SimpleBooleanProperty(false);
    private final BooleanProperty clearGame = new SimpleBooleanProperty(false);
    private final BooleanProperty restoreGame = new SimpleBooleanProperty(false);
    private final BooleanProperty saveGame = new SimpleBooleanProperty(false);

    private LocalTime time;
    private Timeline timer;
    private final StringProperty clock = new SimpleStringProperty("00:00:00");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    // User Interface controls
    private final VBox vGame = new VBox(0);
    private final Pane gridGroup = new Pane();

    private final HBox hTop = new HBox(0);
    private final VBox vScore = new VBox(3);
    private final Label lblScore = new Label("0");
    private final Label lblBest = new Label("0");
    private final Label lblPoints = new Label();

    private final HBox overlay = new HBox();
    private final VBox txtOverlay = new VBox(30);
    private final Label lOvrText = new Label();
    private final Label lOvrSubText = new Label();
    private final HBox buttonsOverlay = new HBox();

    // Overlay Buttons
    private final Button bTry = new Button("Try again");
    private final Button bContinue = new Button("Keep going");
    private final Button bContinueNo = new Button("No, keep going");
    private final Button bSave = new Button("Save");
    private final Button bRestore = new Button("Restore");
    private final Button bQuit = new Button("Quit");

    private final HBox hToolbar = new HBox();

    private final Label lblTime = new Label();

    private final int gridWidth;
    private final GridOperator gridOperator;
    private final SessionManager sessionManager;

    public Board(GridOperator grid) {
        this.gridOperator = grid;
        gridWidth = CELL_SIZE * grid.getGridSize() + BORDER_WIDTH * 2;
        sessionManager = new SessionManager(gridOperator);
        lblPoints.setManaged(false);
        lblPoints.setTextFill(null);
        lblTime.setTextFill(null);

        createScore();
        createGrid();
        createToolBar();
        initGameProperties();
    }

    private void createScore() {
        var lblTitle = new Label("2048");
        lblTitle.getStyleClass().addAll("game-label", "game-title");
        lblTitle.setTextFill(null);

        var lblSubtitle = new Label("FX");
        lblSubtitle.getStyleClass().addAll("game-label", "game-subtitle");
        lblSubtitle.setTextFill(null);

        var hFill = new HBox();
        HBox.setHgrow(hFill, Priority.ALWAYS);
        hFill.setAlignment(Pos.CENTER);

        var vScores = new VBox();
        var hScores = new HBox(5);

        vScore.setAlignment(Pos.CENTER);
        vScore.getStyleClass().add("game-vbox");

        var lblTit = new Label("SCORE");
        lblTit.getStyleClass().addAll("game-label", "game-titScore");
        lblTit.setTextFill(null);

        lblScore.getStyleClass().addAll("game-label", "game-score");
        lblScore.setTextFill(null);
        lblScore.textProperty().bind(gameScoreProperty.asString());
        //lblScore.setPadding(new Insets(5));
        vScore.getChildren().addAll(lblTit, lblScore);
        vScore.setPadding(new Insets(10, 15, 10, 15));

        var vRecord = new VBox(vScore.getSpacing());
        vRecord.setAlignment(Pos.CENTER);
        vRecord.getStyleClass().add("game-vbox");
        vRecord.setPadding(vScore.getPadding());

        var lblTitBest = new Label("BEST");
        lblTitBest.getStyleClass().addAll("game-label", "game-titScore");
        lblTitBest.setTextFill(null);
        lblBest.getStyleClass().addAll("game-label", "game-score");
        lblBest.setTextFill(null);
        //lblBest.setPadding(new Insets(5));
        lblBest.textProperty().bind(gameBestProperty.asString());
        vRecord.getChildren().addAll(lblTitBest, lblBest);
        hScores.getChildren().addAll(vScore, vRecord);

        var vFill = new VBox();
        VBox.setVgrow(vFill, Priority.ALWAYS);
        vScores.getChildren().addAll(hScores, vFill);

        hTop.getChildren().addAll(lblTitle, lblSubtitle, hFill, vScores);
        hTop.setMinSize(gridWidth, TOP_HEIGHT);
        hTop.setPrefSize(gridWidth, TOP_HEIGHT);
        hTop.setMaxSize(gridWidth, TOP_HEIGHT);

        vGame.getChildren().add(hTop);

        var hTime = new HBox();
        hTime.setMinSize(gridWidth, GAP_HEIGHT);
        hTime.setAlignment(Pos.BOTTOM_RIGHT);
        lblTime.getStyleClass().addAll("game-label", "game-time");
        lblTime.setPadding(new Insets(3));
        lblTime.textProperty().bind(clock);
        timer = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            clock.set(LocalTime.now().minusNanos(time.toNanoOfDay()).format(fmt));
        }), new KeyFrame(Duration.seconds(1)));
        timer.setCycleCount(Animation.INDEFINITE);
        hTime.getChildren().add(lblTime);

        vGame.getChildren().add(hTime);
        getChildren().add(vGame);

        lblPoints.getStyleClass().addAll("game-label", "game-points");
        //lblPoints.setAlignment(Pos.CENTER);
        lblPoints.setMinWidth(100);
        getChildren().add(lblPoints);
    }

    private Rectangle createCell(int i, int j) {
        double gap = 7;
        var cell = new Rectangle(i * CELL_SIZE + gap , j * CELL_SIZE + gap, CELL_SIZE - 2 * gap, CELL_SIZE - 2 * gap);
        // provide default style in case css are not loaded
/*
        cell.setFill(Color.web("#cdc1b4"));
        cell.setStroke(Color.web("#BBADA0"));
        cell.setStrokeType(StrokeType.CENTERED);
        cell.setStrokeWidth(14);
        final double arcSize = CELL_SIZE / 6d;
        cell.setArcHeight(arcSize);
        cell.setArcWidth(arcSize);
*/
        cell.getStyleClass().add("game-grid-cell");
        cell.setFill(null);
        return cell;
    }

    private void createGrid() {
        gridOperator.traverseGrid((i, j) -> {
            gridGroup.getChildren().add(createCell(i, j));
            return 0;
        });

        gridGroup.getStyleClass().add("game-grid");
        gridGroup.setManaged(false);
        gridGroup.setLayoutX(BORDER_WIDTH);
        gridGroup.setLayoutY(BORDER_WIDTH);

        var hBottom = new HBox();
        hBottom.getStyleClass().add("game-backGrid");
        hBottom.setMinSize(gridWidth, gridWidth);
        hBottom.setPrefSize(gridWidth, gridWidth);
        hBottom.setMaxSize(gridWidth, gridWidth);

        // Clip hBottom to keep the dropshadow effects within the hBottom
        var rect = new Rectangle(gridWidth, gridWidth);
        hBottom.setClip(rect);
        hBottom.getChildren().add(gridGroup);

        vGame.getChildren().add(hBottom);
    }

    private void createToolBar() {
        // toolbar
        var hPadding = new HBox();
        hPadding.setMinSize(gridWidth, TOOLBAR_HEIGHT);
        hPadding.setPrefSize(gridWidth, TOOLBAR_HEIGHT);
        hPadding.setMaxSize(gridWidth, TOOLBAR_HEIGHT);

        hToolbar.setAlignment(Pos.CENTER);
        hToolbar.getStyleClass().add("game-backGrid");
        hToolbar.setMinSize(gridWidth, TOOLBAR_HEIGHT);
        hToolbar.setPrefSize(gridWidth, TOOLBAR_HEIGHT);
        hToolbar.setMaxSize(gridWidth, TOOLBAR_HEIGHT);

        vGame.getChildren().add(hPadding);
        vGame.getChildren().add(hToolbar);
    }

    protected void setToolBar(HBox toolbar) {
        toolbar.disableProperty().bind(layerOnProperty);
        toolbar.spacingProperty().bind(Bindings.divide(vGame.widthProperty(), 10));
        hToolbar.getChildren().add(toolbar);
    }

    protected void tryAgain() {
        if (!gameTryAgainProperty.get()) {
            gameTryAgainProperty.set(true);
        }
    }

    private void btnTryAgain() {
        layerOnProperty.set(false);
        doResetGame();
    }

    private void keepGoing() {
        layerOnProperty.set(false);
        gamePauseProperty.set(false);
        gameTryAgainProperty.set(false);
        gameSaveProperty.set(false);
        gameRestoreProperty.set(false);
        gameAboutProperty.set(false);
        gameQuitProperty.set(false);
        timer.play();
    }

    private void exitGame() {
        Platform.exit();
    }

    private final Overlay wonListener = new Overlay("You win!", "", bContinue, bTry, "game-overlay-won", "game-lblWon");

    private class Overlay implements ChangeListener<Boolean> {

        private final Button btn1, btn2;
        private final String message, warning;
        private final String style1, style2;

        public Overlay(String message, String warning, Button btn1, Button btn2, String style1, String style2) {
            this.message = message;
            this.warning = warning;
            this.btn1 = btn1; // left
            this.btn2 = btn2; // right
            this.style1 = style1;
            this.style2 = style2;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                return;
            }

            timer.stop();

            overlay.getStyleClass().setAll("game-overlay", style1);
            lOvrText.setText(message);
            lOvrText.getStyleClass().setAll("game-label", style2);
            lOvrText.setTextFill(null);
            lOvrSubText.setText(warning);
            lOvrSubText.getStyleClass().setAll("game-label", "game-lblWarning");
            lOvrSubText.setTextFill(null);
            txtOverlay.getChildren().setAll(lOvrText, lOvrSubText);
            buttonsOverlay.getChildren().setAll(btn1);
            prepareButtonForCss(btn1);

            if (btn2 != null) {
                buttonsOverlay.getChildren().add(btn2);
                prepareButtonForCss(btn2);
            }

            if (!layerOnProperty.get()) {
                var defaultBtn = btn2 == null ? btn1 : btn2;
                defaultBtn.requestFocus();
                defaultBtn.setDefaultButton(true);

                showOverlay();
                layerOnProperty.set(true);
            }
        }
    }

    private static void prepareButtonForCss(Button button) {
        button.setBackground(null);
        button.setBorder(null);
        button.setTextFill(null);
        button.setPadding(new Insets(8, 25, 18, 25));
    }

    private final VBox overlayContainer = new VBox(overlay, buttonsOverlay);

    private void showOverlay() {
        overlayContainer.setManaged(false);
        getChildren().add(overlayContainer);
        var gridParent = gridGroup.getParent();
        layoutInArea(overlayContainer, gridParent.getLayoutX(), gridParent.getLayoutY(), gridWidth, gridWidth, 0, HPos.LEFT, VPos.TOP);
    }

    private void removeOverlay() {
        getChildren().remove(overlayContainer);
    }

    private void initGameProperties() {
        //overlay.setMinSize(gridWidth, gridWidth);
        overlay.setMinHeight(gridWidth);
        overlay.setAlignment(Pos.CENTER);
        //overlay.setTranslateY(TOP_HEIGHT + GAP_HEIGHT);

        overlay.getChildren().setAll(txtOverlay);
        txtOverlay.setAlignment(Pos.CENTER);

        buttonsOverlay.setAlignment(Pos.CENTER);
        //buttonsOverlay.setTranslateY(TOP_HEIGHT + GAP_HEIGHT + gridWidth / 2);
        //buttonsOverlay.setMinSize(gridWidth, gridWidth / 2);
        buttonsOverlay.setTranslateY(- gridWidth / 3.5);
        buttonsOverlay.setSpacing(10);

        bTry.getStyleClass().add("game-button");
        bTry.setOnAction(e -> btnTryAgain());

        bContinue.getStyleClass().add("game-button");
        bContinue.setOnAction(e -> keepGoing());

        bContinueNo.getStyleClass().add("game-button");
        bContinueNo.setOnAction(e -> keepGoing());

        bSave.getStyleClass().add("game-button");
        bSave.setOnAction(e -> saveGame.set(true));

        bRestore.getStyleClass().add("game-button");
        bRestore.setOnAction(e -> restoreGame.set(true));

        bQuit.getStyleClass().add("game-button");
        bQuit.setOnAction(e -> exitGame());

        gameWonProperty.addListener(wonListener);
        gameOverProperty
                .addListener(new Overlay("Game over!", "", bTry, null, "game-overlay-over", "game-lblOver"));
        gamePauseProperty.addListener(
                new Overlay("Game Paused", "", bContinue, null, "game-overlay-pause", "game-lblPause"));
        gameTryAgainProperty.addListener(new Overlay("Try Again?", "Current game will be deleted", bTry, bContinueNo,
                "game-overlay-pause", "game-lblPause"));
        gameSaveProperty.addListener(new Overlay("Save?", "Previous saved data will be overwritten", bSave, bContinueNo,
                "game-overlay-pause", "game-lblPause"));
        gameRestoreProperty.addListener(new Overlay("Restore?", "Current game will be deleted", bRestore, bContinueNo,
                "game-overlay-pause", "game-lblPause"));
        gameAboutProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timer.stop();
                overlay.getStyleClass().setAll("game-overlay", "game-overlay-quit");
/*
                TextFlow flow = new TextFlow();
                flow.setTextAlignment(TextAlignment.CENTER);
                flow.setPadding(new Insets(10, 0, 0, 0));
                flow.setMinSize(gridWidth, gridWidth);
                flow.setPrefSize(gridWidth, gridWidth);
                flow.setMaxSize(gridWidth, gridWidth);
                flow.setPrefSize(BASELINE_OFFSET_SAME_AS_HEIGHT, BASELINE_OFFSET_SAME_AS_HEIGHT);

                var t00 = new Text("2048");
                t00.getStyleClass().setAll("game-label", "game-lblAbout");

                var t01 = new Text("FX");
                t01.getStyleClass().setAll("game-label", "game-lblAbout2");

                var t02 = new Text(" Game\n");
                t02.getStyleClass().setAll("game-label", "game-lblAbout");

                var t1 = new Text("JavaFX game - Desktop version\n\n");
                t1.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var t20 = new Text("Powered by ");
                t20.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var link1 = new Hyperlink();
                link1.setText("OpenJFX");
                link1.setOnAction(e -> Game2048.urlOpener().open("https://openjfx.io/"));
                link1.getStyleClass().setAll("game-label", "game-lblAboutSub2");

                var t21 = new Text(" Project \n\n");
                t21.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var t23 = new Text("\u00A9 ");
                t23.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var link2 = new Hyperlink();
                link2.setText("@JPeredaDnr");
                link2.setOnAction(e -> Game2048.urlOpener().open("https://twitter.com/JPeredaDnr"));
                link2.getStyleClass().setAll("game-label", "game-lblAboutSub2");

                var t22 = new Text(" & ");
                t22.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var link3 = new Hyperlink();
                link3.setText("@brunoborges");
                link3.setOnAction(e -> Game2048.urlOpener().open("https://twitter.com/brunoborges"));

                var t32 = new Text(" & ");
                t32.getStyleClass().setAll("game-label", "game-lblAboutSub");
                link3.getStyleClass().setAll("game-label", "game-lblAboutSub2");

                var t24 = new Text("\n\n");
                t24.getStyleClass().setAll("game-label", "game-lblAboutSub");

                var t31 = new Text(" Version " + Game2048.VERSION + " - 2015\n\n");
                t31.getStyleClass().setAll("game-label", "game-lblAboutSub");

                flow.getChildren().setAll(t00, t01, t02, t1, t20, link1, t21, t23, link2, t22, link3);
                flow.getChildren().addAll(t24, t31);
                txtOverlay.getChildren().setAll(flow);*/
                HtmlText htmlText = new HtmlText("<center class='game-label'>\n" +
                        "    <div class='game-lblAbout'>\n" +
                        "        2048<span class='game-lblAbout2'>FX</span> Game\n" +
                        "    </div>\n" +
                        "    <div class='game-lblAboutSub'>\n" +
                        "        <div>\n" +
                        "            JavaFx game - WebFx version\n" +
                        "        </div>\n" +
                        "        <br/>\n" +
                        "        <div>\n" +
                        "            Powered by <a class='game-lblAboutSub2' href='https://openjfx.io/ target='_blank'>OpenJFX</a> Project\n" +
                        "        </div>\n" +
                        "        <br/>\n" +
                        "        <div>\n" +
                        "            ©\n" +
                        "            <a class='game-lblAboutSub2' href='https://twitter.com/JPeredaDnr' target='_blank'>@JPeredaDnr</a>\n" +
                        "            &\n" +
                        "            <a class='game-lblAboutSub2' href='https://twitter.com/brunoborges' target='_blank'>@brunoborges</a>\n" +
                        "        </div>\n" +
                        "        <br/>\n" +
                        "        <div>\n" +
                        "            Version 1.1.0 - 2015\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</center>");
                htmlText.setPrefWidth(gridWidth);
                //htmlText.setMaxHeight(400);
                txtOverlay.getChildren().setAll(htmlText);
                buttonsOverlay.getChildren().setAll(bContinue);
                prepareButtonForCss(bContinue);
                showOverlay();
                layerOnProperty.set(true);
            }
        });
        gameQuitProperty.addListener(new Overlay("Quit Game?", "Non saved data will be lost", bQuit, bContinueNo,
                "game-overlay-quit", "game-lblQuit"));

        restoreRecord();

        gameScoreProperty.addListener((ov, i, i1) -> {
            if (i1.intValue() > gameBestProperty.get()) {
                gameBestProperty.set(i1.intValue());
            }
        });

        layerOnProperty.addListener((ov, b, b1) -> {
            if (!b1) {
                removeOverlay();
                // Keep the focus on the game when the layer is removed:
                getParent().requestFocus();
            } else if (b1) {
                // Set focus on the first button
                buttonsOverlay.getChildren().get(0).requestFocus();
            }
        });

    }

    private void doClearGame() {
        saveRecord();
        gridGroup.getChildren().removeIf(c -> c instanceof Tile);
        removeOverlay();

        Arrays.asList(clearGame, resetGame, restoreGame, saveGame, layerOnProperty, gameWonProperty, gameOverProperty,
                gameAboutProperty, gamePauseProperty, gameTryAgainProperty, gameSaveProperty, gameRestoreProperty,
                gameQuitProperty).forEach(a -> a.set(false));

        gameScoreProperty.set(0);

        clearGame.set(true);
    }

    private void doResetGame() {
        doClearGame();
        resetGame.set(true);
    }

    public void animateScore() {
        if (gameMovePoints.get() == 0) {
            return;
        }

        final var timeline = new Timeline();
        lblPoints.setText("+" + gameMovePoints.getValue().toString());

        lblPoints.setOpacity(1);

        Bounds scoreBounds = lblScore.getLayoutBounds();
        Point2D scoreBottomRight = new Point2D(scoreBounds.getMaxX(), scoreBounds.getMaxY());
        for (Parent p = lblScore; p != lblPoints.getParent(); p = p.getParent())
            scoreBottomRight = p.localToParent(scoreBottomRight.getX(), scoreBottomRight.getY());
        lblPoints.setTranslateX(0);
        double lblPointsWidth = lblPoints.getText().length() * 12; //lblPoints.getLayoutBounds().getWidth(); <= return 0 in the browser :-(
        lblPoints.setLayoutX(scoreBottomRight.getX() - 12 - lblPointsWidth);
        lblPoints.setLayoutY(scoreBottomRight.getY());
        lblPoints.toFront();
        //webfx.platform.shared.services.log.Logger.log("scoreBounds = " + scoreBounds + "\nscoreBottomRight = " + scoreBottomRight + "\nlblPointsWidth = " + lblPointsWidth + "\nlayoutX = " + lblPoints.getLayoutX());

        final var kvO = new KeyValue(lblPoints.opacityProperty(), 0);
        final var kvY = new KeyValue(lblPoints.layoutYProperty(), lblPoints.getLayoutY() + 50);

        var animationDuration = Duration.millis(600);
        final KeyFrame kfO = new KeyFrame(animationDuration, kvO);
        final KeyFrame kfY = new KeyFrame(animationDuration, kvY);

        timeline.getKeyFrames().add(kfO);
        timeline.getKeyFrames().add(kfY);

        timeline.play();
    }

    public void addTile(Tile tile) {
        double layoutX = tile.getLocation().getLayoutX(CELL_SIZE) - (tile.getMinWidth() / 2);
        double layoutY = tile.getLocation().getLayoutY(CELL_SIZE) - (tile.getMinHeight() / 2);

        tile.setLayoutX(layoutX);
        tile.setLayoutY(layoutY);
        gridGroup.getChildren().add(tile);
    }

    public Tile addRandomTile(Location randomLocation) {
        var tile = Tile.newRandomTile();
        tile.setLocation(randomLocation);

        double layoutX = tile.getLocation().getLayoutX(CELL_SIZE) - (tile.getMinWidth() / 2);
        double layoutY = tile.getLocation().getLayoutY(CELL_SIZE) - (tile.getMinHeight() / 2);

        tile.setLayoutX(layoutX);
        tile.setLayoutY(layoutY);
        tile.setScaleX(0);
        tile.setScaleY(0);

        gridGroup.getChildren().add(tile);

        return tile;
    }

    public void startGame() {
        restoreRecord();

        time = LocalTime.now();
        timer.playFromStart();
    }

    public void setPoints(int points) {
        gameMovePoints.set(points);
    }

    public int getPoints() {
        return gameMovePoints.get();
    }

    public void addPoints(int points) {
        gameMovePoints.set(gameMovePoints.get() + points);
        gameScoreProperty.set(gameScoreProperty.get() + points);
    }

    public void setGameOver(boolean gameOver) {
        gameOverProperty.set(gameOver);
    }

    public void setGameWin(boolean won) {
        if (!gameWonProperty.get()) {
            gameWonProperty.set(won);
        }
    }

    public void pauseGame() {
        if (!gamePauseProperty.get()) {
            gamePauseProperty.set(true);
        }
    }

    public void aboutGame() {
        if (!gameAboutProperty.get()) {
            gameAboutProperty.set(true);
        }
    }

    public void quitGame() {
        if (gameQuitProperty.get()) {
            exitGame();
        } else {
            gameQuitProperty.set(true);
        }
    }

    protected BooleanProperty isLayerOn() {
        return layerOnProperty;
    }

    protected BooleanProperty resetGameProperty() {
        return resetGame;
    }

    protected BooleanProperty clearGameProperty() {
        return clearGame;
    }

    protected BooleanProperty saveGameProperty() {
        return saveGame;
    }

    protected BooleanProperty restoreGameProperty() {
        return restoreGame;
    }

    public boolean saveSession() {
        if (!gameSaveProperty.get()) {
            gameSaveProperty.set(true);
        }
        return true;
    }

    /*
     * Once we have confirmation
     */
    public void saveSession(Map<Location, Tile> gameGrid) {
        saveGame.set(false);
        sessionManager.saveSession(gameGrid, gameScoreProperty.getValue(),
                LocalTime.now().minusNanos(time.toNanoOfDay()).toNanoOfDay());
        keepGoing();
    }

    public boolean restoreSession() {
        if (!gameRestoreProperty.get()) {
            gameRestoreProperty.set(true);
        }
        return true;
    }

    /*
     * Once we have confirmation
     */
    public boolean restoreSession(Map<Location, Tile> gameGrid) {
        restoreGame.set(false);
        doClearGame();
        timer.stop();
        var sTime = new SimpleStringProperty("");
        int score = sessionManager.restoreSession(gameGrid, sTime);
        if (score >= 0) {
            gameScoreProperty.set(score);
            // check tiles>=2048
            gameWonProperty.set(false);
            gameGrid.forEach((l, t) -> {
                if (t != null && t.getValue() >= GameManager.FINAL_VALUE_TO_WIN) {
                    gameWonProperty.removeListener(wonListener);
                    gameWonProperty.set(true);
                    gameWonProperty.addListener(wonListener);
                }
            });
            if (!sTime.get().isEmpty()) {
                time = LocalTime.now().minusNanos(Long.parseLong(sTime.get()));
            }
            timer.play();
            return true;
        }
        // not session found, restart again
        doResetGame();
        return false;
    }

    public void saveRecord() {
        var recordManager = new RecordManager(gridOperator.getGridSize());
        recordManager.saveRecord(gameScoreProperty.getValue());
    }

    private void restoreRecord() {
        var recordManager = new RecordManager(gridOperator.getGridSize());
        gameBestProperty.set(recordManager.restoreRecord());
    }

    public void removeTiles(Set<Tile> mergedToBeRemoved) {
        gridGroup.getChildren().removeAll(mergedToBeRemoved);
    }

}
