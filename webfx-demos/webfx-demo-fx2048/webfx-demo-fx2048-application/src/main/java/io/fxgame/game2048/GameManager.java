package io.fxgame.game2048;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bruno Borges
 * @author Jose Pereda
 */
public class GameManager extends Pane {

    public static final int FINAL_VALUE_TO_WIN = 2048;

    private static final Duration ANIMATION_EXISTING_TILE = Duration.millis(65);
    private static final Duration ANIMATION_NEWLY_ADDED_TILE = Duration.millis(125);
    private static final Duration ANIMATION_MERGED_TILE = Duration.millis(80);

    private volatile boolean movingTiles = false;
    private final List<Location> locations = new ArrayList<>();
    private final Map<Location, Tile> gameGrid;
    private final Set<Tile> mergedToBeRemoved = new HashSet<>();

    private final Board board;
    private final GridOperator gridOperator;

    /**
     * GameManager is a Group containing a Board that holds a grid and the score a
     * Map holds the location of the tiles in the grid
     * <p>
     * The purpose of the game is sum the value of the tiles up to 2048 points Based
     * on the Javascript version: https://github.com/gabrielecirulli/2048
     *
     * @param gridSize defines the size of the grid, default 4x4
     */
    public GameManager(int gridSize) {
        this.gameGrid = new HashMap<>();

        gridOperator = new GridOperator(gridSize);
        board = new Board(gridOperator);
        board.setToolBar(createToolBar());
        this.getChildren().add(board);

        board.clearGameProperty().addListener((ov, b1, b2) -> initializeGameGrid());
        board.resetGameProperty().addListener((ov, b1, b2) -> startGame());
        board.restoreGameProperty().addListener((ov, b1, b2) -> doRestoreSession());
        board.saveGameProperty().addListener((ov, b1, b2) -> doSaveSession());

        initializeGameGrid();
        startGame();
    }

    @Override
    protected void layoutChildren() {
        double width = getWidth();
        double height = getHeight();
        double boardWidth = board.prefWidth(height);
        double boardHeight = board.prefHeight(width);
        double scale = Math.min((width - UserSettings.MARGIN) / boardWidth,
                (height - UserSettings.MARGIN) / boardHeight);
        setScale(scale);
        double layoutX = (width - boardWidth * scale) / 2d;
        double layoutY = (height - boardHeight * scale) / 2d;
        board.setLayoutX(layoutX);
        board.setLayoutY(layoutY);
        //webfx.platform.shared.services.log.Logger.log("width = " + width + ", height = " + height + ", boardWidth = " + boardWidth + ", boardHeight = " + boardHeight + ", scale = " + scale);
        //layoutInArea(board, 0, 0, width, height, 0, HPos.CENTER, VPos.CENTER);

    }
    /**
     * Initializes all cells in gameGrid map to null
     */
    private void initializeGameGrid() {
        gameGrid.clear();
        locations.clear();
        gridOperator.traverseGrid((x, y) -> {
            var thisloc = new Location(x, y);
            locations.add(thisloc);
            gameGrid.put(thisloc, null);
            return 0;
        });
    }

    /**
     * Starts the game by adding 1 or 2 tiles at random locations
     */
    private void startGame() {
        var tile0 = Tile.newRandomTile();
        var randomLocs = new ArrayList<>(locations);
        Collections.shuffle(randomLocs);
        var locs = randomLocs.stream().limit(2).iterator();
        tile0.setLocation(locs.next());

        Tile tile1 = null;
        if (new Random().nextFloat() <= 0.8) { // gives 80% chance to add a second tile
            tile1 = Tile.newRandomTile();
            if (tile1.getValue() == 4 && tile0.getValue() == 4) {
                tile1 = Tile.newTile(2);
            }
            tile1.setLocation(locs.next());
        }

        Arrays.asList(tile0, tile1).stream().filter(Objects::nonNull).forEach(t -> gameGrid.put(t.getLocation(), t));

        redrawTilesInGameGrid();

        board.startGame();
    }

    /**
     * Redraws all tiles in the <code>gameGrid</code> object
     */
    private void redrawTilesInGameGrid() {
        gameGrid.values().stream().filter(Objects::nonNull).forEach(t -> board.addTile(t));
    }

    /**
     * Moves the tiles according to given direction At any move, takes care of merge
     * tiles, add a new one and perform the required animations It updates the score
     * and checks if the user won the game or if the game is over
     *
     * @param direction is the selected direction to move the tiles
     */
    private void moveTiles(Direction direction) {
        synchronized (gameGrid) {
            if (movingTiles) {
                return;
            }
        }

        board.setPoints(0);
        mergedToBeRemoved.clear();
        var parallelTransition = new ParallelTransition();
        gridOperator.sortGrid(direction);
        final int tilesWereMoved = gridOperator.traverseGrid((x, y) -> {
            var thisloc = new Location(x, y);
            var farthestLocation = findFarthestLocation(thisloc, direction); // farthest available location
            var opTile = optionalTile(thisloc);

            var result = new AtomicInteger();
            var nextLocation = farthestLocation.offset(direction); // calculates to a possible merge
            optionalTile(nextLocation).filter(t -> t.isMergeable(opTile) && !t.isMerged()).ifPresent(t -> {
                var tile = opTile.get();
                t.merge(tile);
                t.toFront();
                gameGrid.put(nextLocation, t);
                gameGrid.replace(thisloc, null);

                parallelTransition.getChildren().add(animateExistingTile(tile, t.getLocation()));
                parallelTransition.getChildren().add(animateMergedTile(t));
                mergedToBeRemoved.add(tile);

                board.addPoints(t.getValue());

                if (t.getValue() == FINAL_VALUE_TO_WIN) {
                    board.setGameWin(true);
                }
                result.set(1);
            });
            if (result.get() == 0 && opTile.isPresent() && !farthestLocation.equals(thisloc)) {
                var tile = opTile.get();
                parallelTransition.getChildren().add(animateExistingTile(tile, farthestLocation));

                gameGrid.put(farthestLocation, tile);
                gameGrid.replace(thisloc, null);

                tile.setLocation(farthestLocation);

                result.set(1);
            }

            return result.get();
        });

        board.animateScore();
        if (parallelTransition.getChildren().size() > 0) {
            parallelTransition.setOnFinished(e -> {
                board.removeTiles(mergedToBeRemoved);
                // reset merged after each movement
                gameGrid.values().stream().filter(Objects::nonNull).forEach(Tile::clearMerge);

                var randomAvailableLocation = findRandomAvailableLocation();
                if (randomAvailableLocation == null && mergeMovementsAvailable() == 0) {
                    // game is over if there are no more moves available
                    board.setGameOver(true);
                } else if (randomAvailableLocation != null && tilesWereMoved > 0) {
                    synchronized (gameGrid) {
                        movingTiles = false;
                    }
                    addAndAnimateRandomTile(randomAvailableLocation);
                }
            });

            synchronized (gameGrid) {
                movingTiles = true;
            }

            parallelTransition.play();
        }
    }

    /**
     * optionalTile allows using tiles from the map at some location, whether they
     * are null or not
     *
     * @param loc location of the tile
     * @return an Optional<Tile> containing null or a valid tile
     */
    private Optional<Tile> optionalTile(Location loc) {
        return Optional.ofNullable(gameGrid.get(loc));
    }

    /**
     * Searchs for the farthest empty location where the current tile could go
     *
     * @param location  of the tile
     * @param direction of movement
     * @return a location
     */
    private Location findFarthestLocation(Location location, Direction direction) {
        Location farthest;

        do {
            farthest = location;
            location = farthest.offset(direction);
        } while (gridOperator.isValidLocation(location) && !optionalTile(location).isPresent());

        return farthest;
    }

    /**
     * Finds the number of pairs of tiles that can be merged
     * <p>
     * This method is called only when the grid is full of tiles, what makes the use
     * of Optional unnecessary, but it could be used when the board is not full to
     * find the number of pairs of mergeable tiles and provide a hint for the user,
     * for instance
     *
     * @return the number of pairs of tiles that can be merged
     */
    private int mergeMovementsAvailable() {
        final var pairsOfMergeableTiles = new AtomicInteger();

        Stream.of(Direction.UP, Direction.LEFT).parallel().forEach(direction -> {
            gridOperator.traverseGrid((x, y) -> {
                var thisloc = new Location(x, y);
                optionalTile(thisloc).ifPresent(t -> {
                    if (t.isMergeable(optionalTile(thisloc.offset(direction)))) {
                        pairsOfMergeableTiles.incrementAndGet();
                    }
                });
                return 0;
            });
        });
        return pairsOfMergeableTiles.get();
    }

    /**
     * Finds a random location or returns null if none exist
     *
     * @return a random location or <code>null</code> if there are no more locations
     *         available
     */
    private Location findRandomAvailableLocation() {
        var availableLocations = locations.stream().filter(l -> gameGrid.get(l) == null).collect(Collectors.toList());

        if (availableLocations.isEmpty()) {
            return null;
        }

        Collections.shuffle(availableLocations);

        // returns a random location
        return availableLocations.get(new Random().nextInt(availableLocations.size()));
    }

    /**
     * Adds a tile of random value to a random location with a proper animation
     *
     * @param randomLocation
     */
    private void addAndAnimateRandomTile(Location randomLocation) {
        var tile = board.addRandomTile(randomLocation);
        gameGrid.put(tile.getLocation(), tile);

        animateNewlyAddedTile(tile).play();
    }

    /**
     * Animation that creates a fade in effect when a tile is added to the game by
     * increasing the tile scale from 0 to 100%
     *
     * @param tile to be animated
     * @return a scale transition
     */
    private ScaleTransition animateNewlyAddedTile(Tile tile) {
        final var scaleTransition = new ScaleTransition(ANIMATION_NEWLY_ADDED_TILE, tile);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setOnFinished(e -> {
            // after last movement on full grid, check if there are movements available
            if (this.gameGrid.values().parallelStream().noneMatch(Objects::isNull) && mergeMovementsAvailable() == 0) {
                board.setGameOver(true);
            }
        });
        return scaleTransition;
    }

    /**
     * Animation that moves the tile from its previous location to a new location
     *
     * @param tile        to be animated
     * @param newLocation new location of the tile
     * @return a timeline
     */
    private Timeline animateExistingTile(Tile tile, Location newLocation) {
        var timeline = new Timeline();
        var kvX = new KeyValue(tile.layoutXProperty(),
                newLocation.getLayoutX(Board.CELL_SIZE) - (tile.getMinHeight() / 2), Interpolator.EASE_OUT);
        var kvY = new KeyValue(tile.layoutYProperty(),
                newLocation.getLayoutY(Board.CELL_SIZE) - (tile.getMinHeight() / 2), Interpolator.EASE_OUT);

        var kfX = new KeyFrame(ANIMATION_EXISTING_TILE, kvX);
        var kfY = new KeyFrame(ANIMATION_EXISTING_TILE, kvY);

        timeline.getKeyFrames().add(kfX);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

    /**
     * Animation that creates a pop effect when two tiles merge by increasing the
     * tile scale to 120% at the middle, and then going back to 100%
     *
     * @param tile to be animated
     * @return a sequential transition
     */
    private SequentialTransition animateMergedTile(Tile tile) {
        final var scale0 = new ScaleTransition(ANIMATION_MERGED_TILE, tile);
        scale0.setToX(1.2);
        scale0.setToY(1.2);
        scale0.setInterpolator(Interpolator.EASE_IN);

        final var scale1 = new ScaleTransition(ANIMATION_MERGED_TILE, tile);
        scale1.setToX(1.0);
        scale1.setToY(1.0);
        scale1.setInterpolator(Interpolator.EASE_OUT);

        return new SequentialTransition(scale0, scale1);
    }

    /*************************************************************************/
    /************************ Public methods *********************************/
    /*************************************************************************/

    /**
     * Move the tiles according user input if overlay is not on
     *
     * @param direction
     */
    public void move(Direction direction) {
        if (!board.isLayerOn().get()) {
            moveTiles(direction);
        }
    }

    /**
     * Set gameManager scale to adjust overall game size
     *
     * @param scale
     */
    public void setScale(double scale) {
        board.setScaleX(scale);
        board.setScaleY(scale);
    }

    /**
     * Pauses the game time, covers the grid
     */
    public void pauseGame() {
        board.pauseGame();
    }

    /**
     * Quit the game with confirmation
     */
    public void quitGame() {
        board.quitGame();
    }

    /**
     * Ask to save the game from a properties file with confirmation
     */
    public void saveSession() {
        board.saveSession();
    }

    /**
     * Save the game to a properties file, without confirmation
     */
    private void doSaveSession() {
        board.saveSession(gameGrid);
    }

    /**
     * Ask to restore the game from a properties file with confirmation
     */
    public void restoreSession() {
        board.restoreSession();
    }

    /**
     * Restore the game from a properties file, without confirmation
     */
    private void doRestoreSession() {
        initializeGameGrid();
        if (board.restoreSession(gameGrid)) {
            redrawTilesInGameGrid();
        }
    }

    /**
     * Save actual record to a properties file
     */
    public void saveRecord() {
        board.saveRecord();
    }

    private HBox createToolBar() {
        var btItem1 = createButtonItem("mSave", "Save Session", t -> saveSession());
        var btItem2 = createButtonItem("mRestore", "Restore Session", t -> restoreSession());
        var btItem3 = createButtonItem("mPause", "Pause Game", t -> board.pauseGame());
        var btItem4 = createButtonItem("mReplay", "Try Again", t -> board.tryAgain());
        var btItem5 = createButtonItem("mInfo", "About the Game", t -> board.aboutGame());
        var btItem6 = createButtonItem("mQuit", "Quit Game", t -> quitGame());

        var toolbar = new HBox(btItem1, btItem2, btItem3, btItem4, btItem5, btItem6);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(10.0));
        return toolbar;
    }

    private Button createButtonItem(String symbol, String text, EventHandler<ActionEvent> t) {
        var g = new Button();
        g.setPrefSize(40, 40);
        g.setId(symbol);
        g.setOnAction(t);
        //g.setTooltip(new Tooltip(text));
        return g;
    }

}
