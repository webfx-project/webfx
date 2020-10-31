/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.spacefx;

//import com.jpro.webapi.WebAPI;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.platform.shared.util.uuid.Uuid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static eu.hansolo.spacefx.Config.*;


public class SpaceFXView extends StackPane {
    private static final long                       SCREEN_TOGGLE_INTERVAL  = 10_000_000_000l;
    private static final Random                     RND                     = new Random();
    private static final boolean                    IS_BROWSER              = false; //WebAPI.isBrowser();
    //private              Task<Boolean>              initTask;
    private              Level1                     level1;
    private              Level2                     level2;
    private              Level3                     level3;
    private              long                       lastScreenToggle;
    private              boolean                    readyToStart;
    private              boolean                    platformWaitsUserInteractionBeforeAllowingSound = true;
    private              boolean                    running;
    private              boolean                    gameOverScreen;
    private              boolean                    hallOfFameScreen;
    //private              Properties                 properties;
    private              Label                      playerInitialsLabel;
    private              InitialDigit               digit1;
    private              InitialDigit               digit2;
    private              HBox                       playerInitialsDigits;
    private              Button                     saveInitialsButton;
    private              List<Player>               hallOfFame;
    private              VBox                       hallOfFameBox;
    private              Level                      level;
    private final        Image                      startImg                = WebFxUtil.newImage("startscreen.jpg");
    private final        Image                      gameOverImg             = WebFxUtil.newImage("gameover.jpg");
    private final        Image                      hallOfFameImg           = WebFxUtil.newImage("halloffamescreen.jpg");
    //private final        Image                      startImg                = isDesktop() ? WebFxUtil.newImage("startscreen.jpg")) : isIOS() ? WebFxUtil.newImage("startscreenIOS.jpg")) : WebFxUtil.newImage("startscreenAndroid.png"));
    //private final        Image                      gameOverImg             = isDesktop() ? WebFxUtil.newImage("gameover.jpg")) : isIOS() ? WebFxUtil.newImage("gameoverIOS.jpg")) : WebFxUtil.newImage("gameoverAndroid.png"));
    //private final        Image                      hallOfFameImg           = isDesktop() ? WebFxUtil.newImage("halloffamescreen.jpg")) : isIOS() ? WebFxUtil.newImage("halloffamescreenIOS.jpg")) : WebFxUtil.newImage("halloffamescreenAndroid.png"));
    private              Image[]                    asteroidImages;
    //private              Image                      torpedoButtonImg;
    //private              Image                      rocketButtonImg;
    //private              Image                      shieldButtonImg;
    private              Image                      spaceshipImg;
    private              Image                      spaceshipUpImg;
    private              Image                      spaceshipDownImg;
    private              Image                      miniSpaceshipImg;
    private              Image                      deflectorShieldImg;
    private              Image                      miniDeflectorShieldImg;
    private              Image                      torpedoImg;
    private              Image                      bigTorpedoImg;
    private              Image                      asteroidExplosionImg;
    private              Image                      spaceShipExplosionImg;
    private              Image                      hitImg;
    private              Image                      shieldUpImg;
    private              Image                      lifeUpImg;
    private              Image                      bigTorpedoBonusImg;
    private              Image                      starburstBonusImg;
    private              Image                      miniBigTorpedoBonusImg;
    private              Image                      miniStarburstBonusImg;
    private              Image                      upExplosionImg;
    private              Image                      rocketExplosionImg;
    private              Image                      rocketImg;
    private              AudioClip                  laserSound;
    private              AudioClip                  rocketLaunchSound;
    private              AudioClip                  rocketExplosionSound;
    private              AudioClip                  enemyLaserSound;
    private              AudioClip                  enemyBombSound;
    private              AudioClip                  explosionSound;
    private              AudioClip                  asteroidExplosionSound;
    private              AudioClip                  torpedoHitSound;
    private              AudioClip                  spaceShipExplosionSound;
    private              AudioClip                  enemyBossExplosionSound;
    private              AudioClip                  gameoverSound;
    private              AudioClip                  shieldHitSound;
    private              AudioClip                  enemyHitSound;
    private              AudioClip                  deflectorShieldSound;
    private              AudioClip                  levelBossTorpedoSound;
    private              AudioClip                  levelBossRocketSound;
    private              AudioClip                  levelBossBombSound;
    private              AudioClip                  levelBossExplosionSound;
    private              AudioClip                  shieldUpSound;
    private              AudioClip                  lifeUpSound;
    private              AudioClip                  levelUpSound;
    private              AudioClip                  bonusSound;
    private final        Media                      gameSoundTheme ;
    private final        Media                      soundTheme     ;
    private final        MediaPlayer                gameMediaPlayer;
    private final        MediaPlayer                mediaPlayer    ;
    private              double                     deflectorShieldRadius;
    private              boolean                    levelBossActive;
    private              Font                       scoreFont;
    private              double                     backgroundViewportY;
    private              Canvas                     canvas;
    private              GraphicsContext            ctx;
    private              Star[]                     stars;
    private              Asteroid[]                 asteroids;
    private              SpaceShip                  spaceShip;
    private              SpaceShipExplosion         spaceShipExplosion;
    private              List<Wave>                 waves;
    private              List<Wave>                 wavesToRemove;
    private              List<EnemyBoss>            enemyBosses;
    private              List<LevelBoss>            levelBosses;
    private              List<Bonus>                bonuses;
    private              List<Torpedo>              torpedos;
    private              List<BigTorpedo>           bigTorpedos;
    private              List<Rocket>               rockets;
    private              List<EnemyTorpedo>         enemyTorpedos;
    private              List<EnemyBomb>            enemyBombs;
    private              List<EnemyBossTorpedo>     enemyBossTorpedos;
    private              List<EnemyBossRocket>      enemyBossRockets;
    private              List<LevelBossTorpedo>     levelBossTorpedos;
    private              List<LevelBossRocket>      levelBossRockets;
    private              List<LevelBossBomb>        levelBossBombs;
    private              List<LevelBossExplosion>   levelBossExplosions;
    private              List<EnemyBossExplosion>   enemyBossExplosions;
    private              List<EnemyRocketExplosion> enemyRocketExplosions;
    private              List<RocketExplosion>      rocketExplosions;
    private              List<Explosion>            explosions;
    private              List<AsteroidExplosion>    asteroidExplosions;
    private              List<UpExplosion>          upExplosions;
    private              List<Hit>                  hits;
    private              List<EnemyHit>             enemyHits;
    private              long                       score;
    private              long                       levelKills;
    private              long                       kills;
    private              double                     scorePosX;
    private              double                     scorePosY;
    private              double                     mobileOffsetY;
    private              boolean                    hasBeenHit;
    private              int                        noOfLifes;
    private              int                        noOfShields;
    private              boolean                    bigTorpedosEnabled;
    private              boolean                    starburstEnabled;
    private              long                       lastShieldActivated;
    private              long                       lastEnemyBossAttack;
    private              long                       lastShieldUp;
    private              long                       lastLifeUp;
    private              long                       lastWave;
    private              long                       lastBombDropped;
    private              long                       lastTorpedoFired;
    private              long                       lastStarBlast;
    private              long                       lastBigTorpedoBonus;
    private              long                       lastStarburstBonus;
    private              long                       lastTimerCall;
    private              AnimationTimer             timer;
    private              AnimationTimer             screenTimer;
    private              Circle                     shipTouchArea;
    private              EventHandler<TouchEvent>   touchHandler;

    // ******************** Constructor ***************************************
    public SpaceFXView(Stage stage) {
        gameSoundTheme          = WebFxUtil.newMedia("RaceToMars.mp3");
        soundTheme              = WebFxUtil.newMedia("CityStomper.mp3");
        gameMediaPlayer         = new MediaPlayer(gameSoundTheme);
        mediaPlayer             = new MediaPlayer(soundTheme);

        init(stage);
        initOnBackground(stage);

        stage.showingProperty().addListener((p,o,value) -> {
            if(!value) {
                screenTimer.stop();
                timer.stop();
                WebFxUtil.stopMusic(mediaPlayer);
            }
        });

        Pane pane = new Pane(canvas, shipTouchArea, hallOfFameBox, playerInitialsLabel, playerInitialsDigits, saveInitialsButton);
        pane.setMaxSize(WIDTH, HEIGHT);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        /*if (SHOW_BUTTONS) {
            canvas.addEventHandler(TouchEvent.TOUCH_PRESSED, touchHandler);
            shipTouchArea.setOnTouchMoved(e -> {
                spaceShip.x = e.getTouchPoint().getX();
                spaceShip.y = e.getTouchPoint().getY();
            });
        } else {*/
            shipTouchArea.setOnMouseDragged(e -> {
                spaceShip.x = e.getX();
                spaceShip.y = e.getY();
            });
        //}

        saveInitialsButton.setOnAction(e -> storePlayer());

        setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //StackPane.setAlignment(pane, Pos.CENTER);
        getChildren().add(pane);

        // Start playing background music
        if (PLAY_MUSIC && !platformWaitsUserInteractionBeforeAllowingSound) { WebFxUtil.playMusic(mediaPlayer); }

        // Start timer to toggle between start screen and hall of fame
        screenTimer.start();
    }


    // ******************** Methods *******************************************
    public void init(Stage stage) {
        scoreFont        = Fonts.spaceBoy(SCORE_FONT_SIZE);
        running          = false;
        gameOverScreen   = false;
        levelBossActive  = false;
        lastScreenToggle = WebFxUtil.nanoTime();
        hallOfFameScreen = false;

        playerInitialsLabel = new Label("Type in your initials");
        playerInitialsLabel.setAlignment(Pos.CENTER);
        playerInitialsLabel.setPrefWidth(WIDTH);
        playerInitialsLabel.setTextFill(null); // To allow css color in browser
        Helper.enableNode(playerInitialsLabel, false);

        digit1 = new InitialDigit();
        digit2 = new InitialDigit();
        ToggleGroup toggleGroup = new ToggleGroup();
        digit1.setToggleGroup(toggleGroup);
        digit2.setToggleGroup(toggleGroup);
        digit1.setSelected(true);
        playerInitialsDigits = new HBox(0, digit1, digit2);
        Helper.enableNode(playerInitialsDigits, false);

        saveInitialsButton = new Button("Save Initials");
        saveInitialsButton.setPrefWidth(WIDTH * 0.6);
        saveInitialsButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        saveInitialsButton.setTextFill(null); // To allow css color management
        saveInitialsButton.setBorder(null); // To allow css border management
        Helper.enableNode(saveInitialsButton, false);

        // PreFill hall of fame
        //properties = PropertyManager.INSTANCE.getProperties();

        Player p1 = new Player(PropertyManager.INSTANCE.getString("hallOfFame1"));
        Player p2 = new Player(PropertyManager.INSTANCE.getString("hallOfFame2"));
        Player p3 = new Player(PropertyManager.INSTANCE.getString("hallOfFame3"));

        hallOfFame = new ArrayList<>(3);
        hallOfFame.add(p1);
        hallOfFame.add(p2);
        hallOfFame.add(p3);

        HBox p1Entry  = createHallOfFameEntry(p1);
        HBox p2Entry  = createHallOfFameEntry(p2);
        HBox p3Entry  = createHallOfFameEntry(p3);
        hallOfFameBox = new VBox(20, p1Entry, p2Entry, p3Entry);
        hallOfFameBox.setPrefWidth(WIDTH * 0.6);
        hallOfFameBox.setAlignment(Pos.CENTER);
        //hallOfFameBox.setTranslateY(-HEIGHT * 0.1);
        hallOfFameBox.setMouseTransparent(true);
        hallOfFameBox.relocate((WIDTH - hallOfFameBox.getPrefWidth()) * 0.5, (HEIGHT - hallOfFameBox.getPrefHeight()) * 0.5 -HEIGHT * 0.1);
        Helper.enableNode(hallOfFameBox, false);

        // Mediaplayer for background music
        mediaPlayer.setCycleCount(-1);
        mediaPlayer.setVolume(1);

        // Mediaplayer for game background music
        gameMediaPlayer.setCycleCount(-1);
        gameMediaPlayer.setVolume(1);

        // Load sounds
        laserSound              = WebFxUtil.newAudioClip("laserSound.mp3");
        rocketLaunchSound       = WebFxUtil.newAudioClip("rocketLaunch.mp3");
        rocketExplosionSound    = WebFxUtil.newAudioClip("rocketExplosion.mp3");
        enemyLaserSound         = WebFxUtil.newAudioClip("enemyLaserSound.mp3");
        enemyBombSound          = WebFxUtil.newAudioClip("enemyBomb.mp3");
        explosionSound          = WebFxUtil.newAudioClip("explosionSound.mp3");
        asteroidExplosionSound  = WebFxUtil.newAudioClip("asteroidExplosion.mp3");
        torpedoHitSound         = WebFxUtil.newAudioClip("hit.mp3");
        spaceShipExplosionSound = WebFxUtil.newAudioClip("spaceShipExplosionSound.mp3");
        enemyBossExplosionSound = WebFxUtil.newAudioClip("enemyBossExplosion.mp3");
        gameoverSound           = WebFxUtil.newAudioClip("gameover.mp3");
        shieldHitSound          = WebFxUtil.newAudioClip("shieldhit.mp3");
        enemyHitSound           = WebFxUtil.newAudioClip("enemyBossShieldHit.mp3");
        deflectorShieldSound    = WebFxUtil.newAudioClip("deflectorshieldSound.mp3");
        levelBossTorpedoSound   = WebFxUtil.newAudioClip("levelBossTorpedo.mp3");
        levelBossRocketSound    = WebFxUtil.newAudioClip("levelBossRocket.mp3");
        levelBossBombSound      = WebFxUtil.newAudioClip("levelBossBomb.mp3");
        levelBossExplosionSound = WebFxUtil.newAudioClip("explosionSound1.mp3");
        shieldUpSound           = WebFxUtil.newAudioClip("shieldUp.mp3");
        lifeUpSound             = WebFxUtil.newAudioClip("lifeUp.mp3");
        levelUpSound            = WebFxUtil.newAudioClip("levelUp.mp3");
        bonusSound              = WebFxUtil.newAudioClip("bonus.mp3");

        // Variable initialization
        backgroundViewportY           = SWITCH_POINT;
        canvas                        = new Canvas(WIDTH, HEIGHT);
        ctx                           = canvas.getGraphicsContext2D();
        stars                         = new Star[NO_OF_STARS];
        asteroids                     = new Asteroid[NO_OF_ASTEROIDS];
        spaceShipExplosion            = new SpaceShipExplosion(0, 0, 0, 0);
        waves                         = new ArrayList<>();
        wavesToRemove                 = new ArrayList<>();
        enemyBosses                   = new ArrayList<>();
        levelBosses                   = new ArrayList<>();
        bonuses                       = new ArrayList<>();
        rockets                       = new ArrayList<>();
        torpedos                      = new ArrayList<>();
        bigTorpedos                   = new ArrayList<>();
        enemyRocketExplosions         = new ArrayList<>();
        explosions                    = new ArrayList<>();
        asteroidExplosions            = new ArrayList<>();
        upExplosions                  = new ArrayList<>();
        enemyTorpedos                 = new ArrayList<>();
        enemyBombs                    = new ArrayList<>();
        enemyBossTorpedos             = new ArrayList<>();
        enemyBossRockets              = new ArrayList<>();
        levelBossTorpedos             = new ArrayList<>();
        levelBossRockets              = new ArrayList<>();
        levelBossBombs                = new ArrayList<>();
        levelBossExplosions           = new ArrayList<>();
        enemyBossExplosions           = new ArrayList<>();
        rocketExplosions              = new ArrayList<>();
        hits                          = new ArrayList<>();
        enemyHits                     = new ArrayList<>();
        score                         = 0;
        levelKills                    = 0;
        kills                         = 0;
        hasBeenHit                    = false;
        noOfLifes                     = NO_OF_LIFES;
        noOfShields                   = NO_OF_SHIELDS;
        bigTorpedosEnabled            = false;
        starburstEnabled              = false;
        lastShieldActivated           = 0;
        lastEnemyBossAttack           = WebFxUtil.nanoTime();
        lastShieldUp                  = WebFxUtil.nanoTime();
        lastLifeUp                    = WebFxUtil.nanoTime();
        lastWave                      = WebFxUtil.nanoTime();
        lastTorpedoFired              = WebFxUtil.nanoTime();
        lastStarBlast                 = WebFxUtil.nanoTime();
        lastBigTorpedoBonus           = WebFxUtil.nanoTime();
        lastStarburstBonus            = WebFxUtil.nanoTime();
        //long deltaTime                = FPS_60;
        long deltaTime                = IS_BROWSER ? FPS_30 : FPS_60;
        timer = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now > lastTimerCall) {
                    lastTimerCall = now + deltaTime;
                    updateAndDraw();
                }
                if (now > lastEnemyBossAttack + ENEMY_BOSS_ATTACK_INTERVAL) {
                    spawnEnemyBoss(spaceShip);
                    lastEnemyBossAttack = now;
                }
                if (now > lastShieldUp + SHIELD_UP_SPAWN_INTERVAL && noOfShields < NO_OF_SHIELDS) {
                    spawnShieldUp();
                    lastShieldUp = now;
                }
                if (now > lastLifeUp + LIFE_UP_SPAWN_INTERVAL && noOfLifes < NO_OF_LIFES) {
                    spawnLifeUp();
                    lastLifeUp = now;
                }
                if (now > lastWave + WAVE_SPAWN_INTERVAL && SHOW_ENEMIES) {
                    spawnWave();
                    lastWave = now;
                }
                if (now > lastBigTorpedoBonus + BIG_TORPEDO_BONUS_INTERVAL) {
                    spawnBigTorpedoBonus();
                    lastBigTorpedoBonus = now;
                }
                if (now > lastStarburstBonus + STARBURST_BONUS_INTERVAL) {
                    spawnStarburstBonus();
                    lastStarburstBonus = now;
                }
            }
        };
        screenTimer = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (!running && now > lastScreenToggle + SCREEN_TOGGLE_INTERVAL) {
                    hallOfFameScreen = !hallOfFameScreen;
                    if (hallOfFameScreen) {
                        ctx.drawImage(hallOfFameImg, 0, 0, WIDTH, HEIGHT);
                        Helper.enableNode(hallOfFameBox, true);
                    } else {
                        ctx.drawImage(startImg, 0, 0, WIDTH, HEIGHT);
                        Helper.enableNode(hallOfFameBox, false);
                    }
                    lastScreenToggle = now;
                }
            }
        };

        shipTouchArea = new Circle();

        touchHandler = e -> {
            EventType<TouchEvent>  type  = e.getEventType();
            if (TouchEvent.TOUCH_PRESSED.equals(type)) {
/*
                if (SHOW_BUTTONS) {
                    double x = e.getTouchPoint().getX();
                    double y = e.getTouchPoint().getY();
                    if (Helper.isInsideCircle(TORPEDO_BUTTON_CX, TORPEDO_BUTTON_CY, TORPEDO_BUTTON_R, x, y)) {
                        spawnWeapon(spaceShip.x, spaceShip.y);
                    } else if (Helper.isInsideCircle(ROCKET_BUTTON_CX, ROCKET_BUTTON_CY, ROCKET_BUTTON_R, x, y)) {
                        if (rockets.size() < MAX_NO_OF_ROCKETS) {
                            spawnRocket(spaceShip.x, spaceShip.y);
                        }
                    } else if (Helper.isInsideCircle(SHIELD_BUTTON_CX, SHIELD_BUTTON_CY, SHIELD_BUTTON_R, x, y)) {
                        if (noOfShields > 0 && !spaceShip.shield) {
                            lastShieldActivated = WebFxUtil.nanoTime();
                            spaceShip.shield = true;
                            playSound(deflectorShieldSound);
                        }
                    }
                }
*/
            }
        };

        initStars();

        scorePosX = WIDTH * 0.5;
        scorePosY = 40 * SCALING_FACTOR;

        //mobileOffsetY = isIOS() ? 30 : 0;
        mobileOffsetY = 0;

        // Preparing GraphicsContext
        canvas.sceneProperty().addListener(scene -> Platform.runLater(() -> {
            ctx.setFont(scoreFont);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.setTextBaseline(VPos.CENTER);
            WebFxUtil.onImageLoaded(startImg, () -> ctx.drawImage(startImg, 0, 0, WIDTH, HEIGHT));
            WebFxUtil.setLoadingContext(ctx);
        }));
    }

    private void initOnBackground(Stage stage) {
        /*initTask = new Task<>() {
            @Override protected Boolean call() {*/
                // Load images
                spaceshipImg            = WebFxUtil.newImage("spaceship.png", 48 * SCALING_FACTOR, 48 * SCALING_FACTOR);
                torpedoImg              = WebFxUtil.newImage("torpedo.png", 17 * SCALING_FACTOR, 20 * SCALING_FACTOR);
                spaceshipUpImg          = WebFxUtil.newImage("spaceshipUp.png", 48 * SCALING_FACTOR, 48 * SCALING_FACTOR);
                spaceshipDownImg        = WebFxUtil.newImage("spaceshipDown.png", 48 * SCALING_FACTOR, 48 * SCALING_FACTOR);
                miniSpaceshipImg        = WebFxUtil.newImage("spaceship.png", 16 * SCALING_FACTOR, 16 * SCALING_FACTOR);
                hitImg                  = WebFxUtil.newImage("torpedoHit2.png", 400 * SCALING_FACTOR, 160 * SCALING_FACTOR);
                asteroidImages          = new Image[] { WebFxUtil.newImage("asteroid1.png", 140 * SCALING_FACTOR, 140 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid2.png", 140 * SCALING_FACTOR, 140 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid3.png", 140 * SCALING_FACTOR, 140 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid4.png", 110 * SCALING_FACTOR, 110 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid5.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid6.png", 120 * SCALING_FACTOR, 120 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid7.png", 110 * SCALING_FACTOR, 110 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid8.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid9.png", 130 * SCALING_FACTOR, 130 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid10.png", 120 * SCALING_FACTOR, 120 * SCALING_FACTOR),
                                                        WebFxUtil.newImage("asteroid11.png", 140 * SCALING_FACTOR, 140 * SCALING_FACTOR) };
                //torpedoButtonImg        = WebFxUtil.newImage("torpedoButton.png", 64 * SCALING_FACTOR, 64 * SCALING_FACTOR);
                //rocketButtonImg         = WebFxUtil.newImage("rocketButton.png", 64 * SCALING_FACTOR, 64 * SCALING_FACTOR);
                //shieldButtonImg         = WebFxUtil.newImage("shieldButton.png", 64 * SCALING_FACTOR, 64 * SCALING_FACTOR);
                asteroidExplosionImg    = WebFxUtil.newImage("asteroidExplosion.png", 1024 * SCALING_FACTOR, 896 * SCALING_FACTOR);

                // Init levels
                level1 = new Level1();

                spaceShipExplosionImg   = WebFxUtil.newImage("spaceshipexplosion.png", 800 * SCALING_FACTOR, 600 * SCALING_FACTOR);
                deflectorShieldImg      = WebFxUtil.newImage("deflectorshield.png", 100 * SCALING_FACTOR, 100 * SCALING_FACTOR);
                miniDeflectorShieldImg  = WebFxUtil.newImage("deflectorshield.png", 16 * SCALING_FACTOR, 16 * SCALING_FACTOR);
                bigTorpedoImg           = WebFxUtil.newImage("bigtorpedo.png", 22 * SCALING_FACTOR, 40 * SCALING_FACTOR);
                shieldUpImg             = WebFxUtil.newImage("shieldUp.png", 50 * SCALING_FACTOR, 50 * SCALING_FACTOR);
                lifeUpImg               = WebFxUtil.newImage("lifeUp.png", 50 * SCALING_FACTOR, 50 * SCALING_FACTOR);
                bigTorpedoBonusImg      = WebFxUtil.newImage("bigTorpedoBonus.png", 50 * SCALING_FACTOR, 50 * SCALING_FACTOR);
                starburstBonusImg       = WebFxUtil.newImage("starburstBonus.png", 50 * SCALING_FACTOR, 50 * SCALING_FACTOR);
                miniBigTorpedoBonusImg  = WebFxUtil.newImage("bigTorpedoBonus.png", 20 * SCALING_FACTOR, 20 * SCALING_FACTOR);
                miniStarburstBonusImg   = WebFxUtil.newImage("starburstBonus.png", 20 * SCALING_FACTOR, 20 * SCALING_FACTOR);
                upExplosionImg          = WebFxUtil.newImage("upExplosion.png", 400 * SCALING_FACTOR, 700 * SCALING_FACTOR);
                rocketExplosionImg      = WebFxUtil.newImage("rocketExplosion.png", 960 * SCALING_FACTOR, 768 * SCALING_FACTOR);
                rocketImg               = WebFxUtil.newImage("rocket.png", 17 * SCALING_FACTOR, 50 * SCALING_FACTOR);

                level2 = new Level2();
                level3 = new Level3();
                level  = level1;

                deflectorShieldRadius   = deflectorShieldImg.getRequestedWidth() * 0.5;
                spaceShip               = new SpaceShip(spaceshipImg, spaceshipUpImg, spaceshipDownImg);

                // Adjust audio clip volumes
                explosionSound.setVolume(0.5);
                torpedoHitSound.setVolume(0.5);

                initAsteroids();

/*
                return true;
            }
        };
        initTask.setOnSucceeded(e -> {
*/
            shipTouchArea.setCenterX(spaceShip.x);
            shipTouchArea.setCenterY(spaceShip.y);
            shipTouchArea.setRadius(deflectorShieldRadius);
            shipTouchArea.setStroke(Color.TRANSPARENT);
            shipTouchArea.setFill(Color.TRANSPARENT);
            readyToStart = true;
/*
        });
        initTask.setOnFailed(e -> readyToStart = false);
        new Thread(initTask, "initThread").start();
*/
    }

    private void initStars() {
        for (int i = 0; i < NO_OF_STARS; i++) {
            Star star = new Star();
            star.y = RND.nextDouble() * HEIGHT;
            stars[i] = star;
        }
    }

    private void initAsteroids() {
        for (int i = 0 ; i < NO_OF_ASTEROIDS ; i++) {
            asteroids[i] = new Asteroid(asteroidImages[RND.nextInt(asteroidImages.length)]);
        }
    }


    // Update and draw
    private void updateAndDraw() {
        ctx.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw background
        if (SHOW_BACKGROUND) {
            backgroundViewportY -= 0.5;
            if (backgroundViewportY <= 0) {
                backgroundViewportY = SWITCH_POINT; //backgroundImg.getHeight() - HEIGHT;
            }
            ctx.drawImage(level.getBackgroundImg(), 0, backgroundViewportY, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT);
        }

        // Draw Stars
        if (SHOW_STARS) {
            ctx.setFill(Color.rgb(255, 255, 255, 0.9));
            for (int i = 0; i < NO_OF_STARS; i++) {
                Star star = stars[i];
                star.update();
                ctx.fillOval(star.x, star.y, star.size, star.size);
            }
        }

        // Draw Asteroids
        for (int i = 0 ; i < NO_OF_ASTEROIDS ; i++) {
            Asteroid asteroid = asteroids[i];
            asteroid.update();
            ctx.save();
            ctx.translate(asteroid.cX, asteroid.cY);
            ctx.rotate(asteroid.rot);
            ctx.scale(asteroid.scale, asteroid.scale);
            ctx.translate(-asteroid.imgCenterX, -asteroid.imgCenterY);
            ctx.drawImage(asteroid.image, 0, 0);
            ctx.restore();

            // Check for torpedo hits
            for (Torpedo torpedo : torpedos) {
                if (isHitCircleCircle(torpedo.x, torpedo.y, torpedo.radius, asteroid.cX, asteroid.cY, asteroid.radius)) {
                    asteroid.hits--;
                    if (asteroid.hits <= 0) {
                        double explosionScale = 2 * asteroid.scale;
                        asteroidExplosions.add(new AsteroidExplosion(asteroid.cX - ASTEROID_EXPLOSION_FRAME_CENTER * explosionScale, asteroid.cY - ASTEROID_EXPLOSION_FRAME_CENTER * 2 * asteroid.scale, asteroid.vX, asteroid.vY, 2 * asteroid.scale));
                        score += asteroid.value;
                        asteroid.respawn();
                        torpedo.toBeRemoved = true;
                        playSound(asteroidExplosionSound);
                    } else {
                        hits.add(new Hit(torpedo.x - HIT_FRAME_CENTER, torpedo.y - HIT_FRAME_HEIGHT, asteroid.vX, asteroid.vY));
                        torpedo.toBeRemoved = true;
                        playSound(torpedoHitSound);
                    }
                }
            }

            // Check for bigTorpedo hits
            for (BigTorpedo bigTorpedo : bigTorpedos) {
                if (isHitCircleCircle(bigTorpedo.x, bigTorpedo.y, bigTorpedo.radius, asteroid.cX, asteroid.cY, asteroid.radius)) {
                    asteroid.hits--;
                    if (asteroid.hits <= 0) {
                        double explosionScale = 2 * asteroid.scale;
                        asteroidExplosions.add(new AsteroidExplosion(asteroid.cX - ASTEROID_EXPLOSION_FRAME_CENTER * explosionScale, asteroid.cY - ASTEROID_EXPLOSION_FRAME_CENTER * 2 * asteroid.scale, asteroid.vX, asteroid.vY, 2 * asteroid.scale));
                        score += asteroid.value;
                        asteroid.respawn();
                        bigTorpedo.toBeRemoved = true;
                        playSound(asteroidExplosionSound);
                    } else {
                        hits.add(new Hit(bigTorpedo.x - HIT_FRAME_CENTER, bigTorpedo.y - HIT_FRAME_HEIGHT, asteroid.vX, asteroid.vY));
                        bigTorpedo.toBeRemoved = true;
                        playSound(torpedoHitSound);
                    }
                }
            }

            // Check for rocket hits
            for (Rocket rocket : rockets) {
                if (isHitCircleCircle(rocket.x, rocket.y, rocket.radius, asteroid.cX, asteroid.cY, asteroid.radius)) {
                    rocketExplosions.add(new RocketExplosion(asteroid.cX - ROCKET_EXPLOSION_FRAME_CENTER * asteroid.scale, asteroid.cY - ROCKET_EXPLOSION_FRAME_CENTER * asteroid.scale, asteroid.vX, asteroid.vY, asteroid.scale));
                    score += asteroid.value;
                    asteroid.respawn();
                    rocket.toBeRemoved = true;
                    playSound(rocketExplosionSound);
                }
            }

            // Check for space ship hit
            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, deflectorShieldRadius, asteroid.cX, asteroid.cY, asteroid.radius);
                } else {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, spaceShip.radius, asteroid.cX, asteroid.cY, asteroid.radius);
                }
                if (hit) {
                    spaceShipExplosion.countX = 0;
                    spaceShipExplosion.countY = 0;
                    spaceShipExplosion.x      = spaceShip.x - SPACESHIP_EXPLOSION_FRAME_WIDTH;
                    spaceShipExplosion.y      = spaceShip.y - SPACESHIP_EXPLOSION_FRAME_HEIGHT;
                    if (spaceShip.shield) {
                        playSound(explosionSound);
                        double explosionScale = 2 * asteroid.scale;
                        asteroidExplosions.add(new AsteroidExplosion(asteroid.cX - ASTEROID_EXPLOSION_FRAME_CENTER * explosionScale, asteroid.cY - ASTEROID_EXPLOSION_FRAME_CENTER * 2 * asteroid.scale, asteroid.vX, asteroid.vY, 2 * asteroid.scale));
                    } else {
                        playSound(spaceShipExplosionSound);
                        hasBeenHit = true;
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                    asteroid.respawn();
                }
            }
        }

        // Draw Wave
        for (Wave wave : waves) {
            if (wave.isRunning) {
                wave.update(ctx);
            } else {
                wavesToRemove.add(wave);
            }
        }
        waves.removeAll(wavesToRemove);

        // Draw EnemyBoss
        for (EnemyBoss enemyBoss : enemyBosses) {
            enemyBoss.update();
            ctx.save();
            ctx.translate(enemyBoss.x - enemyBoss.radius, enemyBoss.y - enemyBoss.radius);
            ctx.save();
            ctx.translate(enemyBoss.radius, enemyBoss.radius);
            ctx.rotate(enemyBoss.r);
            ctx.translate(-enemyBoss.radius, -enemyBoss.radius);
            ctx.drawImage(enemyBoss.image, 0, 0);
            ctx.restore();
            ctx.restore();

            // Check for torpedo hits with enemy boss
            for (Torpedo torpedo : torpedos) {
                if (isHitCircleCircle(torpedo.x, torpedo.y, torpedo.radius, enemyBoss.x, enemyBoss.y, enemyBoss.radius)) {
                    enemyBoss.hits -= TORPEDO_DAMAGE;
                    if (enemyBoss.hits == 0) {
                        enemyBossExplosions.add(
                            new EnemyBossExplosion(enemyBoss.x - ENEMY_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, enemyBoss.y - ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, enemyBoss.vX,
                                                           enemyBoss.vY, 0.5));
                        score += enemyBoss.value;
                        kills++;
                        levelKills++;
                        enemyBoss.toBeRemoved = true;
                        torpedo.toBeRemoved = true;
                        playSound(enemyBossExplosionSound);
                    } else {
                        enemyHits.add(new EnemyHit(torpedo.x - HIT_FRAME_CENTER, torpedo.y - HIT_FRAME_HEIGHT, enemyBoss.vX, enemyBoss.vY));
                        torpedo.toBeRemoved = true;
                        playSound(enemyHitSound);
                    }
                }
            }

            // Check for bigTorpedo hits with enemy boss
            for (BigTorpedo bigTorpedo : bigTorpedos) {
                if (isHitCircleCircle(bigTorpedo.x, bigTorpedo.y, bigTorpedo.radius, enemyBoss.x, enemyBoss.y, enemyBoss.radius)) {
                    enemyBoss.hits -= BIG_TORPEDO_DAMAGE;
                    if (enemyBoss.hits <= 0) {
                        enemyBossExplosions.add(
                            new EnemyBossExplosion(enemyBoss.x - ENEMY_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, enemyBoss.y - ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, enemyBoss.vX,
                                                   enemyBoss.vY, 0.5));
                        score += enemyBoss.value;
                        kills++;
                        levelKills++;
                        enemyBoss.toBeRemoved = true;
                        bigTorpedo.toBeRemoved = true;
                        playSound(enemyBossExplosionSound);
                    } else {
                        enemyHits.add(new EnemyHit(bigTorpedo.x - HIT_FRAME_CENTER, bigTorpedo.y - HIT_FRAME_HEIGHT, enemyBoss.vX, enemyBoss.vY));
                        bigTorpedo.toBeRemoved = true;
                        playSound(enemyHitSound);
                    }
                }
            }

            // Check for rocket hits with enemy boss
            for (Rocket rocket : rockets) {
                if (isHitCircleCircle(rocket.x, rocket.y, rocket.radius, enemyBoss.x, enemyBoss.y, enemyBoss.radius)) {
                    enemyBossExplosions.add(
                        new EnemyBossExplosion(enemyBoss.x - ENEMY_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, enemyBoss.y - ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, enemyBoss.vX, enemyBoss.vY, 0.5));
                    score += enemyBoss.value;
                    kills++;
                    levelKills++;
                    enemyBoss.toBeRemoved = true;
                    rocket.toBeRemoved = true;
                    playSound(enemyBossExplosionSound);
                }
            }


            // Check for space ship hit with enemy boss
            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, deflectorShieldRadius, enemyBoss.x, enemyBoss.y, enemyBoss.radius);
                } else {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, spaceShip.radius, enemyBoss.x, enemyBoss.y, enemyBoss.radius);
                }
                if (hit) {
                    if (spaceShip.shield) {
                        enemyBossExplosions.add(new EnemyBossExplosion(enemyBoss.x - ENEMY_BOSS_EXPLOSION_FRAME_WIDTH * 0.125, enemyBoss.y - ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT * 0.125, enemyBoss.vX, enemyBoss.vY, 0.5));
                        //playSound(enemyBossExplosionSound);
                    } else {
                        spaceShipExplosion.countX = 0;
                        spaceShipExplosion.countY = 0;
                        spaceShipExplosion.x = spaceShip.x - SPACESHIP_EXPLOSION_FRAME_WIDTH;
                        spaceShipExplosion.y = spaceShip.y - SPACESHIP_EXPLOSION_FRAME_HEIGHT;
                        playSound(spaceShipExplosionSound);
                        hasBeenHit = true;
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                    enemyBoss.toBeRemoved = true;
                }
            }
        }

        // Draw LevelBoss
        for (LevelBoss levelBoss : levelBosses) {
            levelBoss.update();
            ctx.save();
            ctx.translate(levelBoss.x - levelBoss.radius, levelBoss.y - levelBoss.radius);
            ctx.save();
            ctx.translate(levelBoss.radius, levelBoss.radius);
            ctx.rotate(levelBoss.r);
            ctx.translate(-levelBoss.radius, -levelBoss.radius);
            ctx.drawImage(levelBoss.image, 0, 0);
            ctx.restore();
            ctx.restore();

            // Check for torpedo hits with enemy boss
            for (Torpedo torpedo : torpedos) {
                if (isHitCircleCircle(torpedo.x, torpedo.y, torpedo.radius, levelBoss.x, levelBoss.y, levelBoss.radius)) {
                    levelBoss.hits -= TORPEDO_DAMAGE;
                    if (levelBoss.hits <= 0) {
                        levelBossExplosions.add(new LevelBossExplosion(levelBoss.x - LEVEL_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, levelBoss.y - LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, levelBoss.vX, levelBoss.vY, 1.0));
                        score += levelBoss.value;
                        kills++;
                        levelBoss.toBeRemoved = true;
                        levelBossActive = false;
                        levelKills = 0;
                        nextLevel();
                        torpedo.toBeRemoved = true;
                        playSound(levelBossExplosionSound);
                    } else {
                        enemyHits.add(new EnemyHit(torpedo.x - HIT_FRAME_CENTER, torpedo.y - HIT_FRAME_HEIGHT, levelBoss.vX, levelBoss.vY));
                        torpedo.toBeRemoved = true;
                        playSound(enemyHitSound);
                    }
                }
            }

            // Check for bigTorpedo hits with enemy boss
            for (BigTorpedo bigTorpedo : bigTorpedos) {
                if (isHitCircleCircle(bigTorpedo.x, bigTorpedo.y, bigTorpedo.radius, levelBoss.x, levelBoss.y, levelBoss.radius)) {
                    levelBoss.hits -= BIG_TORPEDO_DAMAGE;
                    if (levelBoss.hits <= 0) {
                        levelBossExplosions.add(new LevelBossExplosion(levelBoss.x - LEVEL_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, levelBoss.y - LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, levelBoss.vX, levelBoss.vY, 1.0));
                        score += levelBoss.value;
                        kills++;
                        levelBoss.toBeRemoved = true;
                        levelBossActive = false;
                        levelKills = 0;
                        nextLevel();
                        bigTorpedo.toBeRemoved = true;
                        playSound(levelBossExplosionSound);
                    } else {
                        enemyHits.add(new EnemyHit(bigTorpedo.x - HIT_FRAME_CENTER, bigTorpedo.y - HIT_FRAME_HEIGHT, levelBoss.vX, levelBoss.vY));
                        bigTorpedo.toBeRemoved = true;
                        playSound(enemyHitSound);
                    }
                }
            }

            // Check for rocket hits with level boss
            for (Rocket rocket : rockets) {
                if (isHitCircleCircle(rocket.x, rocket.y, rocket.radius, levelBoss.x, levelBoss.y, levelBoss.radius)) {
                    levelBoss.hits -= ROCKET_DAMAGE;
                    if (levelBoss.hits <= 0) {
                        levelBossExplosions.add(new LevelBossExplosion(levelBoss.x - LEVEL_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, levelBoss.y - LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, levelBoss.vX, levelBoss.vY, 1.0));
                        score += levelBoss.value;
                        kills++;
                        levelKills++;
                        levelBoss.toBeRemoved = true;
                        levelBossActive = false;
                        levelKills = 0;
                        nextLevel();
                        rocket.toBeRemoved = true;
                        playSound(levelBossExplosionSound);
                    } else {
                        enemyHits.add(new EnemyHit(rocket.x - HIT_FRAME_CENTER, rocket.y - HIT_FRAME_HEIGHT, levelBoss.vX, levelBoss.vY));
                        rocket.toBeRemoved = true;
                        playSound(enemyHitSound);
                    }
                }
            }

            // Check for space ship hit with level boss
            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, deflectorShieldRadius, levelBoss.x, levelBoss.y, levelBoss.radius);
                } else {
                    hit = isHitCircleCircle(spaceShip.x, spaceShip.y, spaceShip.radius, levelBoss.x, levelBoss.y, levelBoss.radius);
                }
                if (hit) {
                    if (spaceShip.shield) {
                        lastShieldActivated = 0;
                        levelBoss.hits -= SHIELD_DAMAGE;
                        if (levelBoss.hits <= 0) {
                            levelBossExplosions.add(new LevelBossExplosion(levelBoss.x - LEVEL_BOSS_EXPLOSION_FRAME_WIDTH * 0.25, levelBoss.y - LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT * 0.25, levelBoss.vX, levelBoss.vY, 1.0));
                            score += levelBoss.value;
                            kills++;
                            levelKills++;
                            levelBoss.toBeRemoved = true;
                            levelBossActive = false;
                            levelKills = 0;
                            nextLevel();
                            playSound(levelBossExplosionSound);
                        }
                    } else {
                        spaceShipExplosion.countX = 0;
                        spaceShipExplosion.countY = 0;
                        spaceShipExplosion.x = spaceShip.x - SPACESHIP_EXPLOSION_FRAME_WIDTH;
                        spaceShipExplosion.y = spaceShip.y - SPACESHIP_EXPLOSION_FRAME_HEIGHT;
                        playSound(spaceShipExplosionSound);
                        hasBeenHit = true;
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                    levelBoss.toBeRemoved = true;
                    levelBossActive = false;
                    levelKills = 0;
                    nextLevel();
                }
            }
        }

        // Draw Bonuses
        for (Bonus bonus : bonuses) {
            bonus.update();
            ctx.save();
            ctx.translate(bonus.cX, bonus.cY);
            ctx.rotate(bonus.rot);
            ctx.translate(-bonus.imgCenterX, -bonus.imgCenterY);
            ctx.drawImage(bonus.image, 0, 0);
            ctx.restore();

            // Check for space ship contact
            boolean hit;
            if (spaceShip.shield) {
                hit = isHitCircleCircle(spaceShip.x, spaceShip.y, deflectorShieldRadius, bonus.cX, bonus.cY, bonus.radius);
            } else {
                hit = isHitCircleCircle(spaceShip.x, spaceShip.y, spaceShip.radius, bonus.cX, bonus.cY, bonus.radius);
            }
            if (hit) {
                if (bonus instanceof LifeUp) {
                    if (noOfLifes <= NO_OF_LIFES - 1) { noOfLifes++; }
                    playSound(lifeUpSound);
                } else if (bonus instanceof ShieldUp) {
                    if (noOfShields <= NO_OF_SHIELDS - 1) { noOfShields++; }
                    playSound(shieldUpSound);
                } else if (bonus instanceof BigTorpedoBonus) {
                    bigTorpedosEnabled = true;
                    playSound(bonusSound);
                } else if (bonus instanceof StarburstBonus) {
                    starburstEnabled = true;
                    playSound(bonusSound);
                }
                upExplosions.add(new UpExplosion(bonus.cX - UP_EXPLOSION_FRAME_CENTER, bonus.cY - UP_EXPLOSION_FRAME_CENTER, bonus.vX, bonus.vY, 1.0));
                bonus.toBeRemoved = true;
            }
        }

        // Draw Torpedos
        for (Torpedo torpedo : torpedos) {
            torpedo.update();
            ctx.drawImage(torpedo.image, torpedo.x - torpedo.radius, torpedo.y - torpedo.radius);
        }

        // Draw BigTorpedos
        for (BigTorpedo bigTorpedo : bigTorpedos) {
            bigTorpedo.update();
            ctx.save();
            ctx.translate(bigTorpedo.x - bigTorpedo.width / 2, bigTorpedo.y - bigTorpedo.height / 2);
            ctx.save();
            ctx.translate(bigTorpedo.width / 2, bigTorpedo.height / 2);
            ctx.rotate(bigTorpedo.r - 45);
            ctx.translate(-bigTorpedo.width / 2, -bigTorpedo.height / 2);
            ctx.drawImage(bigTorpedo.image, 0, 0);
            ctx.restore();
            ctx.restore();
        }

        // Draw Rockets
        for (Rocket rocket : rockets) {
            rocket.update();
            ctx.drawImage(rocket.image, rocket.x - rocket.halfWidth, rocket.y - rocket.halfHeight);
        }

        // Draw EnemyTorpedos
        for (EnemyTorpedo enemyTorpedo : enemyTorpedos) {
            enemyTorpedo.update();
            ctx.drawImage(enemyTorpedo.image, enemyTorpedo.x, enemyTorpedo.y);
        }

        // Draw EnemyBombs
        for (EnemyBomb enemyBomb : enemyBombs) {
            enemyBomb.update();
            ctx.drawImage(enemyBomb.image, enemyBomb.x, enemyBomb.y);
        }

        // Draw EnemyBossTorpedos
        for (EnemyBossTorpedo enemyBossTorpedo : enemyBossTorpedos) {
            enemyBossTorpedo.update();
            ctx.drawImage(enemyBossTorpedo.image, enemyBossTorpedo.x, enemyBossTorpedo.y);
        }

        // Draw EnemyBossRockets
        for (EnemyBossRocket enemyBossRocket : enemyBossRockets) {
            enemyBossRocket.update();
            ctx.save();
            ctx.translate(enemyBossRocket.x - enemyBossRocket.width / 2, enemyBossRocket.y - enemyBossRocket.height / 2);
            ctx.save();
            ctx.translate(enemyBossRocket.width / 2, enemyBossRocket.height / 2);
            ctx.rotate(enemyBossRocket.r);
            ctx.translate(-enemyBossRocket.width / 2, -enemyBossRocket.height / 2);
            ctx.drawImage(enemyBossRocket.image, 0, 0);
            ctx.restore();
            ctx.restore();
        }

        // Draw LevelBossTorpedos
        for (LevelBossTorpedo levelBossTorpedo : levelBossTorpedos) {
            levelBossTorpedo.update();
            ctx.save();
            ctx.translate(levelBossTorpedo.x - levelBossTorpedo.width / 2, levelBossTorpedo.y - levelBossTorpedo.height / 2);
            ctx.save();
            ctx.translate(levelBossTorpedo.width / 2, levelBossTorpedo.height / 2);
            ctx.rotate(levelBossTorpedo.r);
            ctx.translate(-levelBossTorpedo.width / 2, -levelBossTorpedo.height / 2);
            ctx.drawImage(levelBossTorpedo.image, 0, 0);
            ctx.restore();
            ctx.restore();
        }

        // Draw LevelBossRockets
        for (LevelBossRocket levelBossRocket : levelBossRockets) {
            levelBossRocket.update();
            ctx.save();
            ctx.translate(levelBossRocket.x - levelBossRocket.width / 2, levelBossRocket.y - levelBossRocket.height / 2);
            ctx.save();
            ctx.translate(levelBossRocket.width / 2, levelBossRocket.height / 2);
            ctx.rotate(levelBossRocket.r);
            ctx.translate(-levelBossRocket.width / 2, -levelBossRocket.height / 2);
            ctx.drawImage(levelBossRocket.image, 0, 0);
            ctx.restore();
            ctx.restore();
        }

        // Draw LevelBossBombs
        for (LevelBossBomb levelBossBomb : levelBossBombs) {
            levelBossBomb.update();
            ctx.drawImage(levelBossBomb.image, levelBossBomb.x, levelBossBomb.y);
        }

        // Draw Explosions
        for (Explosion explosion : explosions) {
            explosion.update();
            ctx.drawImage(level.getExplosionImg(), explosion.countX * EXPLOSION_FRAME_WIDTH, explosion.countY * EXPLOSION_FRAME_HEIGHT, EXPLOSION_FRAME_WIDTH, EXPLOSION_FRAME_HEIGHT,
                          explosion.x, explosion.y, EXPLOSION_FRAME_WIDTH * explosion.scale, EXPLOSION_FRAME_HEIGHT * explosion.scale);
        }

        // Draw AsteroidExplosions
        for (AsteroidExplosion asteroidExplosion : asteroidExplosions) {
            asteroidExplosion.update();
            ctx.drawImage(asteroidExplosionImg, asteroidExplosion.countX * ASTEROID_EXPLOSION_FRAME_WIDTH, asteroidExplosion.countY * ASTEROID_EXPLOSION_FRAME_HEIGHT,
                          ASTEROID_EXPLOSION_FRAME_WIDTH, ASTEROID_EXPLOSION_FRAME_HEIGHT, asteroidExplosion.x, asteroidExplosion.y,
                          ASTEROID_EXPLOSION_FRAME_WIDTH * asteroidExplosion.scale, ASTEROID_EXPLOSION_FRAME_HEIGHT * asteroidExplosion.scale);
        }

        // Draw RocketExplosions
        for (RocketExplosion rocketExplosion : rocketExplosions) {
            rocketExplosion.update();
            ctx.drawImage(rocketExplosionImg, rocketExplosion.countX * ROCKET_EXPLOSION_FRAME_WIDTH, rocketExplosion.countY * ROCKET_EXPLOSION_FRAME_HEIGHT, ROCKET_EXPLOSION_FRAME_WIDTH, ROCKET_EXPLOSION_FRAME_HEIGHT, rocketExplosion.x, rocketExplosion.y, ROCKET_EXPLOSION_FRAME_WIDTH * rocketExplosion.scale,
                          ROCKET_EXPLOSION_FRAME_HEIGHT * rocketExplosion.scale);
        }

        // Draw EnemyRocketExplosions
        for (EnemyRocketExplosion enemyRocketExplosion : enemyRocketExplosions) {
            enemyRocketExplosion.update();
            ctx.drawImage(level.getEnemyRocketExplosionImg(), enemyRocketExplosion.countX * ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH, enemyRocketExplosion.countY * ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT, ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH, ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT, enemyRocketExplosion.x, enemyRocketExplosion.y, ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH * enemyRocketExplosion.scale,
                          ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT * enemyRocketExplosion.scale);
        }

        // Draw EnemyBossExplosions
        for (EnemyBossExplosion enemyBossExplosion : enemyBossExplosions) {
            enemyBossExplosion.update();
            ctx.drawImage(level.getEnemyBossExplosionImg(), enemyBossExplosion.countX * ENEMY_BOSS_EXPLOSION_FRAME_WIDTH, enemyBossExplosion.countY * ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT,
                          ENEMY_BOSS_EXPLOSION_FRAME_WIDTH, ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT, enemyBossExplosion.x, enemyBossExplosion.y,
                          ENEMY_BOSS_EXPLOSION_FRAME_WIDTH * enemyBossExplosion.scale, ENEMY_BOSS_EXPLOSION_FRAME_HEIGHT * enemyBossExplosion.scale);
        }

        // Draw LevelBossExplosions
        for (LevelBossExplosion levelBossExplosion : levelBossExplosions) {
            levelBossExplosion.update();
            ctx.drawImage(level.getLevelBossExplosionImg(), levelBossExplosion.countX * LEVEL_BOSS_EXPLOSION_FRAME_WIDTH, levelBossExplosion.countY * LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT,
                          LEVEL_BOSS_EXPLOSION_FRAME_WIDTH, LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT, levelBossExplosion.x, levelBossExplosion.y,
                          LEVEL_BOSS_EXPLOSION_FRAME_WIDTH * levelBossExplosion.scale, LEVEL_BOSS_EXPLOSION_FRAME_HEIGHT * levelBossExplosion.scale);
        }

        // Draw UpExplosions
        for (UpExplosion upExplosion : upExplosions) {
            upExplosion.update();
            ctx.drawImage(upExplosionImg, upExplosion.countX * UP_EXPLOSION_FRAME_WIDTH, upExplosion.countY * UP_EXPLOSION_FRAME_HEIGHT, UP_EXPLOSION_FRAME_WIDTH, UP_EXPLOSION_FRAME_HEIGHT, upExplosion.x, upExplosion.y,
                          UP_EXPLOSION_FRAME_WIDTH * upExplosion.scale, UP_EXPLOSION_FRAME_HEIGHT * upExplosion.scale);
        }

        // Draw Hits
        for (Hit hit : hits) {
            hit.update();
            ctx.drawImage(hitImg, hit.countX * HIT_FRAME_WIDTH, hit.countY * HIT_FRAME_HEIGHT, HIT_FRAME_WIDTH, HIT_FRAME_HEIGHT, hit.x, hit.y, HIT_FRAME_WIDTH, HIT_FRAME_HEIGHT);
        }

        // Draw EnemyBoss Hits
        for (EnemyHit hit : enemyHits) {
            hit.update();
            ctx.drawImage(level.getEnemyBossHitImg(), hit.countX * ENEMY_HIT_FRAME_WIDTH, hit.countY * ENEMY_HIT_FRAME_HEIGHT, ENEMY_HIT_FRAME_WIDTH, ENEMY_HIT_FRAME_HEIGHT, hit.x, hit.y, ENEMY_HIT_FRAME_WIDTH, ENEMY_HIT_FRAME_HEIGHT);
        }

        // Draw Spaceship, score, lifes and shields
        if (noOfLifes > 0) {
            // Draw Spaceship or it's explosion
            if (hasBeenHit) {
                spaceShipExplosion.update();
                ctx.drawImage(spaceShipExplosionImg, spaceShipExplosion.countX * SPACESHIP_EXPLOSION_FRAME_WIDTH, spaceShipExplosion.countY * SPACESHIP_EXPLOSION_FRAME_HEIGHT,
                              SPACESHIP_EXPLOSION_FRAME_WIDTH, SPACESHIP_EXPLOSION_FRAME_HEIGHT, spaceShip.x - SPACESHIP_EXPLOSION_FRAME_CENTER, spaceShip.y - SPACESHIP_EXPLOSION_FRAME_CENTER,
                              SPACESHIP_EXPLOSION_FRAME_WIDTH, SPACESHIP_EXPLOSION_FRAME_HEIGHT);
                spaceShip.respawn();
            } else {
                // Draw space ship
                spaceShip.update();

                ctx.save();
                ctx.setGlobalAlpha(spaceShip.isVulnerable ? 1.0 : 0.5);
                if (spaceShip.vY < 0) {
                    ctx.drawImage(spaceshipUpImg,spaceShip.x - spaceShip.radius, spaceShip.y - spaceShip.radius);
                } else if (spaceShip.vY > 0) {
                    ctx.drawImage(spaceshipDownImg,spaceShip.x - spaceShip.radius, spaceShip.y - spaceShip.radius);
                } else {
                    ctx.drawImage(spaceshipImg,spaceShip.x - spaceShip.radius, spaceShip.y - spaceShip.radius);
                }

                ctx.restore();

                if (spaceShip.shield) {
                    long delta = WebFxUtil.nanoTime() - lastShieldActivated;
                    if (delta > DEFLECTOR_SHIELD_TIME) {
                        spaceShip.shield = false;
                        noOfShields--;
                    } else {
                        ctx.setStroke(SPACEFX_COLOR_TRANSLUCENT);
                        ctx.setFill(SPACEFX_COLOR_TRANSLUCENT);
                        ctx.strokeRect(SHIELD_INDICATOR_X, SHIELD_INDICATOR_Y + mobileOffsetY, SHIELD_INDICATOR_WIDTH, SHIELD_INDICATOR_HEIGHT);
                        ctx.fillRect(SHIELD_INDICATOR_X, SHIELD_INDICATOR_Y + mobileOffsetY, SHIELD_INDICATOR_WIDTH - SHIELD_INDICATOR_WIDTH * delta / DEFLECTOR_SHIELD_TIME,
                                     SHIELD_INDICATOR_HEIGHT);
                        ctx.setGlobalAlpha(RND.nextDouble() * 0.5 + 0.1);
                        ctx.drawImage(deflectorShieldImg, spaceShip.x - deflectorShieldRadius, spaceShip.y - deflectorShieldRadius);
                        ctx.setGlobalAlpha(1);
                    }
                }

                if (bigTorpedosEnabled) {
                    long delta = WebFxUtil.nanoTime() - lastBigTorpedoBonus;
                    if (delta > BIG_TORPEDO_TIME) {
                        bigTorpedosEnabled = false;
                    }
                }

                if (starburstEnabled) {
                    long delta = WebFxUtil.nanoTime() - lastStarburstBonus;
                    if (delta > STARBURST_TIME) {
                        starburstEnabled = false;
                    }
                }
            }

            // Draw score
            ctx.setFill(SPACEFX_COLOR);
            ctx.setFont(scoreFont);
            ctx.fillText(Long.toString(score), scorePosX, scorePosY + mobileOffsetY);

            // Draw lifes
            for (int i = 0 ; i < noOfLifes ; i++) {
                ctx.drawImage(miniSpaceshipImg, i * WebFxUtil.getImageWidth(miniSpaceshipImg) + 10, 20 + mobileOffsetY);
            }

            // Draw shields
            for (int i = 0 ; i < noOfShields ; i++) {
                ctx.drawImage(miniDeflectorShieldImg, WIDTH - i * (WebFxUtil.getImageWidth(miniDeflectorShieldImg) + 5), 20 + mobileOffsetY);
            }

            // Draw bigTorpedo and starburst icon
            if (starburstEnabled) {
                ctx.drawImage(miniStarburstBonusImg, 10, 40 + mobileOffsetY);
            } else if (bigTorpedosEnabled) {
                ctx.drawImage(miniBigTorpedoBonusImg, 10, 40 + mobileOffsetY);
            }
        }

        // Draw Buttons
        /*if (SHOW_BUTTONS) {
            ctx.drawImage(torpedoButtonImg, TORPEDO_BUTTON_X, TORPEDO_BUTTON_Y);
            ctx.drawImage(rocketButtonImg, ROCKET_BUTTON_X, ROCKET_BUTTON_Y);
            ctx.drawImage(shieldButtonImg, SHIELD_BUTTON_X, SHIELD_BUTTON_Y);
        }*/

        // Remove sprites
        enemyBosses.removeIf(sprite -> sprite.toBeRemoved);
        levelBosses.removeIf(sprite -> sprite.toBeRemoved);
        bonuses.removeIf(sprite -> sprite.toBeRemoved);
        torpedos.removeIf(sprite -> sprite.toBeRemoved);
        bigTorpedos.removeIf(sprite -> sprite.toBeRemoved);
        rockets.removeIf(sprite -> sprite.toBeRemoved);
        enemyTorpedos.removeIf(sprite -> sprite.toBeRemoved);
        enemyBombs.removeIf(sprite -> sprite.toBeRemoved);
        enemyBossTorpedos.removeIf(sprite -> sprite.toBeRemoved);
        enemyBossRockets.removeIf(sprite -> sprite.toBeRemoved);
        levelBossTorpedos.removeIf(sprite -> sprite.toBeRemoved);
        levelBossRockets.removeIf(sprite -> sprite.toBeRemoved);
        levelBossBombs.removeIf(sprite -> sprite.toBeRemoved);
        levelBossExplosions.removeIf(sprite -> sprite.toBeRemoved);
        enemyBossExplosions.removeIf(sprite -> sprite.toBeRemoved);
        enemyRocketExplosions.removeIf(sprite -> sprite.toBeRemoved);
        rocketExplosions.removeIf(sprite -> sprite.toBeRemoved);
        explosions.removeIf(sprite -> sprite.toBeRemoved);
        asteroidExplosions.removeIf(sprite -> sprite.toBeRemoved);
        upExplosions.removeIf(sprite -> sprite.toBeRemoved);
        hits.removeIf(sprite -> sprite.toBeRemoved);
        enemyHits.removeIf(sprite -> sprite.toBeRemoved);

        // Remove waves
        wavesToRemove.clear();
    }


    // Spawn different objects
    private void spawnWeapon(final double x, final double y) {
        if (starburstEnabled) {
            fireStarburst();
        } else if (bigTorpedosEnabled) {
            bigTorpedos.add(new BigTorpedo(bigTorpedoImg, x, y, 0, -BIG_TORPEDO_SPEED * 2.333333, 45));
        } else {
            torpedos.add(new Torpedo(torpedoImg, x, y));
        }
        playSound(laserSound);
    }

    private void spawnBigTorpedo(final double x, final double y) {
        bigTorpedos.add(new BigTorpedo(bigTorpedoImg, x, y, 0, -BIG_TORPEDO_SPEED * 2.333333, 45));
        playSound(laserSound);
    }

    private void spawnRocket(final double x, final double y) {
        rockets.add(new Rocket(rocketImg, x, y));
        playSound(rocketLaunchSound);
    }

    private void spawnEnemyTorpedo(final double x, final double y, final double vX, final double vY) {
        double vFactor = ENEMY_TORPEDO_SPEED / Math.abs(vY); // make sure the speed is always the defined one
        enemyTorpedos.add(new EnemyTorpedo(level.getEnemyTorpedoImg(), x, y, vFactor * vX, vFactor * vY));
        playSound(enemyLaserSound);
    }

    private void spawnEnemyBomb(final double x, final double y) {
        enemyBombs.add(new EnemyBomb(level.getEnemyBombImg(), x, y, 0, ENEMY_BOMB_SPEED));
        playSound(enemyBombSound);
    }

    private void spawnEnemyBoss(final SpaceShip spaceShip) {
        if (levelBossActive || !SHOW_ENEMY_BOSS) { return; }
        enemyBosses.add(new EnemyBoss(spaceShip, level.getEnemyBossImg4(), RND.nextBoolean()));
    }

    private void spawnLevelBoss(final SpaceShip spaceShip) {
        if (levelBossActive) { return; }
        levelBossActive = true;
        levelBosses.add(new LevelBoss(spaceShip, level.getLevelBossImg(), true, true));
    }

    private void spawnShieldUp() {
        bonuses.add(new ShieldUp(shieldUpImg));
    }

    private void spawnLifeUp() {
        bonuses.add(new LifeUp(lifeUpImg));
    }

    private void spawnBigTorpedoBonus() {
        bonuses.add(new BigTorpedoBonus(bigTorpedoBonusImg));
    }

    private void spawnStarburstBonus() {
        if (level.equals(level1)) { return; }
        bonuses.add(new StarburstBonus(starburstBonusImg));
    }

    private void spawnWave() {
        switch (level.getDifficulty()) {
            case EASY:
                if (levelKills < NO_OF_KILLS_STAGE_1 && !levelBossActive) {
                    waves.add(new Wave(WAVE_TYPES_SLOW[RND.nextInt(WAVE_TYPES_SLOW.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], false, false));
                } else if (levelKills >= NO_OF_KILLS_STAGE_1 && levelKills < NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_MEDIUM[RND.nextInt(WAVE_TYPES_MEDIUM.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_SLOW, WaveType.TYPE_11_SLOW, spaceShip, 10, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], false, false));
                    }
                } else if (levelKills >= NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    spawnLevelBoss(spaceShip);
                } else if (!levelBossActive) {
                    waves.add(new Wave(WAVE_TYPES_MEDIUM[RND.nextInt(WAVE_TYPES_MEDIUM.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                }
                break;
            case NORMAL:
                if (levelKills < NO_OF_KILLS_STAGE_1 && !levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_MEDIUM[RND.nextInt(WAVE_TYPES_MEDIUM.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], false, false));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_MEDIUM, WaveType.TYPE_11_MEDIUM, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], false, false));
                    }
                } else if (levelKills >= NO_OF_KILLS_STAGE_1 && levelKills < NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_FAST[RND.nextInt(WAVE_TYPES_FAST.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_FAST, WaveType.TYPE_11_FAST, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    }
                } else if (levelKills >= NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    spawnLevelBoss(spaceShip);
                } else if (!levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_FAST[RND.nextInt(WAVE_TYPES_FAST.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_FAST, WaveType.TYPE_11_FAST, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    }
                }
                break;
            case HARD:
                if (levelKills < NO_OF_KILLS_STAGE_1 && !levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_FAST[RND.nextInt(WAVE_TYPES_FAST.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_FAST, WaveType.TYPE_11_FAST, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, false));
                    }
                } else if (levelKills >= NO_OF_KILLS_STAGE_1 && levelKills < NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_FAST[RND.nextInt(WAVE_TYPES_FAST.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_FAST, WaveType.TYPE_11_FAST, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                    }
                } else if (levelKills >= NO_OF_KILLS_STAGE_2 && !levelBossActive) {
                    spawnLevelBoss(spaceShip);
                } else if (!levelBossActive) {
                    if (RND.nextBoolean()) {
                        waves.add(new Wave(WAVE_TYPES_FAST[RND.nextInt(WAVE_TYPES_FAST.length)], spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                    } else {
                        waves.add(new Wave(WaveType.TYPE_10_FAST, WaveType.TYPE_11_FAST, spaceShip, level.getDifficulty().noOfEnemies, level.getEnemyImages()[RND.nextInt(level.getEnemyImages().length)], true, true));
                    }
                }
                break;
        }
    }

    private void spawnEnemyBossTorpedo(final double x, final double y, final double vX, final double vY) {
        double factor = ENEMY_BOSS_TORPEDO_SPEED / Math.abs(vY); // make sure the speed is always the defined one
        enemyBossTorpedos.add(new EnemyBossTorpedo(level.getEnemyBossTorpedoImg(), x, y, factor * vX, factor * vY));
        playSound(enemyLaserSound);
    }

    private void spawnEnemyBossRocket(final double x, final double y) {
        enemyBossRockets.add(new EnemyBossRocket(spaceShip, level.getEnemyBossRocketImg(), x, y));
        playSound(rocketLaunchSound);
    }

    private void spawnLevelBossTorpedo(final double x, final double y, final double vX, final double vY, final double r) {
        double factor = LEVEL_BOSS_TORPEDO_SPEED / Math.abs(vY); // make sure the speed is always the defined one
        levelBossTorpedos.add(new LevelBossTorpedo(level.getLevelBossTorpedoImg(), x, y, factor * vX, factor * vY, r));
        playSound(levelBossTorpedoSound);
    }

    private void spawnLevelBossRocket(final double x, final double y) {
        levelBossRockets.add(new LevelBossRocket(spaceShip, level.getLevelBossRocketImg(), x, y));
        playSound(levelBossRocketSound);
    }

    private void spawnLevelBossBomb(final double x, final double y) {
        levelBossBombs.add(new LevelBossBomb(level.getLevelBossBombImg(), x, y, 0, LEVEL_BOSS_BOMB_SPEED));
        playSound(levelBossBombSound);
    }


    // Hit test
    private boolean isHitCircleCircle(final double c1X, final double c1Y, final double c1R, final double c2X, final double c2Y, final double c2R) {
        double distX    = c1X - c2X;
        double distY    = c1Y - c2Y;
        double distance = Math.sqrt((distX * distX) + (distY * distY));
        return (distance <= c1R + c2R);
    }


    // Game Over
    private void gameOver() {
        timer.stop();
        running = false;
        gameOverScreen = true;
        if (PLAY_MUSIC) {
            gameMediaPlayer.pause();
        }

        boolean isInHallOfFame = score > hallOfFame.get(2).score;

        PauseTransition pauseBeforeGameOverScreen = new PauseTransition(Duration.millis(1000));
        pauseBeforeGameOverScreen.setOnFinished(e -> {
            ctx.clearRect(0, 0, WIDTH, HEIGHT);
            ctx.drawImage(gameOverImg, 0, 0, WIDTH, HEIGHT);
            ctx.setFill(SPACEFX_COLOR);
            ctx.setFont(Fonts.spaceBoy(SCORE_FONT_SIZE));
            ctx.fillText(Long.toString(score), scorePosX, HEIGHT * 0.25);
            playSound(gameoverSound);
        });
        pauseBeforeGameOverScreen.play();

        if (isInHallOfFame) {
            PauseTransition pauseInGameOverScreen = new PauseTransition(Duration.millis(5000));
            pauseInGameOverScreen.setOnFinished(e -> {
                // Add player to hall of fame
                ctx.clearRect(0, 0, WIDTH, HEIGHT);
                ctx.drawImage(hallOfFameImg, 0, 0, WIDTH, HEIGHT);

                hallOfFameScreen = true;
                Helper.enableNode(hallOfFameBox, true);
                Helper.enableNode(playerInitialsLabel, true);
                Helper.enableNode(playerInitialsDigits, true);
                Helper.enableNode(saveInitialsButton, true);
                playerInitialsLabel.relocate((WIDTH - playerInitialsLabel.getPrefWidth()) * 0.5, HEIGHT * 0.7);
                playerInitialsDigits.relocate((WIDTH - digit1.getPrefWidth() - digit2.getPrefWidth()) * 0.5, HEIGHT * 0.8);
                saveInitialsButton.relocate((WIDTH - saveInitialsButton.getPrefWidth()) * 0.5, HEIGHT - saveInitialsButton.getPrefHeight() - HEIGHT * 0.075);
                Platform.runLater(() -> playerInitialsDigits.requestFocus());
            });
            pauseInGameOverScreen.play();
        } else {
            // Back to StartScreen
            PauseTransition pauseInGameOverScreen = new PauseTransition(Duration.millis(5000));
            pauseInGameOverScreen.setOnFinished(a -> reInitGame());
            pauseInGameOverScreen.play();
        }
    }


    // Reinitialize game
    private void reInitGame() {
        ctx.clearRect(0, 0, WIDTH, HEIGHT);
        ctx.drawImage(startImg, 0, 0, WIDTH, HEIGHT);

        Helper.enableNode(hallOfFameBox, false);
        gameOverScreen = false;
        explosions.clear();
        torpedos.clear();
        bigTorpedos.clear();
        enemyTorpedos.clear();
        enemyBombs.clear();
        enemyBossTorpedos.clear();
        enemyBossRockets.clear();
        enemyBosses.clear();
        levelBosses.clear();
        levelBossTorpedos.clear();
        levelBossRockets.clear();
        levelBossBombs.clear();
        bonuses.clear();
        waves.clear();
        initAsteroids();
        spaceShip.init();
        hasBeenHit  = false;
        noOfLifes   = NO_OF_LIFES;
        noOfShields = NO_OF_SHIELDS;
        level       = level1;
        score       = 0;
        kills       = 0;
        levelKills  = 0;
        if (PLAY_MUSIC && !platformWaitsUserInteractionBeforeAllowingSound) {
            WebFxUtil.playMusic(mediaPlayer);
        }

        screenTimer.start();
    }


    // Create Hall of Fame entry
    private HBox createHallOfFameEntry(final Player player) {
        Label playerName  = new Label(player.name);
        playerName.setTextFill(SPACEFX_COLOR);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label playerScore = new Label(Long.toString(player.score));
        playerScore.setTextFill(SPACEFX_COLOR);
        playerScore.setAlignment(Pos.CENTER_RIGHT);

        HBox entry = new HBox(20, playerName, spacer, playerScore);
        entry.setPrefWidth(WIDTH);
        return entry;
    }


    // Play audio clips
    private void playSound(final AudioClip audioClip) {
        if (PLAY_SOUND) {
            WebFxUtil.playSound(audioClip);
        }
    }


    // Iterate through levels
    private void nextLevel() {
        playSound(levelUpSound);
        if (level3.equals(level)) {
            level = level1;
            return;
        } else if (level2.equals(level)) {
            level = level3;
            return;
        } else if (level1.equals(level)) {
            level = level2;
            return;
        }
    }


    // ******************** Public Methods ************************************
    public void startGame() {
        if (gameOverScreen) { return; }
        ctx.clearRect(0, 0, WIDTH, HEIGHT);
        if (SHOW_BACKGROUND) {
            ctx.drawImage(level.getBackgroundImg(), 0, 0);
        }
        if (PLAY_MUSIC) {
            mediaPlayer.pause();
            WebFxUtil.playMusic(gameMediaPlayer);
        }
        Helper.enableNode(hallOfFameBox, false);
        screenTimer.stop();
        running = true;
        timer.start();
    }

    public void userInteracted() {
        platformWaitsUserInteractionBeforeAllowingSound = false;
    }

    public boolean isReadyToStart() { return readyToStart; }

    public boolean isRunning() { return running; }

    public boolean isHallOfFameScreen() { return hallOfFameScreen; }

    public void increaseSpaceShipVx() { spaceShip.vX = 5; }
    public void decreaseSpaceShipVx() { spaceShip.vX = -5; }
    public void stopSpaceShipVx() { spaceShip.vX = 0; }

    public void increaseSpaceShipVy() { spaceShip.vY = 5; }
    public void decreaseSpaceShipVy() { spaceShip.vY = -5; }
    public void stopSpaceShipVy() { spaceShip.vY = 0; }

    public void activateSpaceShipShield() {
        if (noOfShields > 0 && !spaceShip.shield) {
            lastShieldActivated = WebFxUtil.nanoTime();
            spaceShip.shield = true;
            playSound(deflectorShieldSound);
        }
    }

    public void fireSpaceShipRocket() {
        // Max 5 rockets at the same time
        if (rockets.size() < MAX_NO_OF_ROCKETS) {
            spawnRocket(spaceShip.x, spaceShip.y);
        }
    }

    public void fireSpaceShipWeapon() {
        if (WebFxUtil.nanoTime() - lastTorpedoFired < MIN_TORPEDO_INTERVAL) { return; }
        spawnWeapon(spaceShip.x, spaceShip.y);
        lastTorpedoFired = WebFxUtil.nanoTime();
    }

    public void fireStarburst() {
        if (!starburstEnabled || (WebFxUtil.nanoTime() - lastStarBlast < MIN_STARBURST_INTERVAL)) { return; }
        double offset    = Math.toRadians(-135);
        double angleStep = Math.toRadians(22.5);
        double angle     = 0;
        double x         = spaceShip.x;
        double y         = spaceShip.y;
        double vX;
        double vY;
        for (int i = 0 ; i < 5 ; i++) {
            vX = BIG_TORPEDO_SPEED * Math.cos(offset + angle);
            vY = BIG_TORPEDO_SPEED * Math.sin(offset + angle);
            bigTorpedos.add(new BigTorpedo(bigTorpedoImg, x, y, vX * BIG_TORPEDO_SPEED, vY * BIG_TORPEDO_SPEED, Math.toDegrees(angle)));
            angle += angleStep;
        }
        lastStarBlast = WebFxUtil.nanoTime();
        playSound(laserSound);
    }

    public InitialDigit getDigit1() { return digit1; }
    public InitialDigit getDigit2() { return digit2; }

    public void storePlayer() {
        hallOfFame.add(new Player((digit1.getCharacter() + digit2.getCharacter()), score));
        Collections.sort(hallOfFame);
        hallOfFame = hallOfFame.stream().limit(3).collect(Collectors.toList());

        // Store hall of fame in properties
        PropertyManager.INSTANCE.set("hallOfFame1", hallOfFame.get(0).toPropertyString());
        PropertyManager.INSTANCE.set("hallOfFame2", hallOfFame.get(1).toPropertyString());
        PropertyManager.INSTANCE.set("hallOfFame3", hallOfFame.get(2).toPropertyString());
        PropertyManager.INSTANCE.storeProperties();

        HBox p1Entry = createHallOfFameEntry(new Player(PropertyManager.INSTANCE.getString("hallOfFame1")));
        HBox p2Entry = createHallOfFameEntry(new Player(PropertyManager.INSTANCE.getString("hallOfFame2")));
        HBox p3Entry = createHallOfFameEntry(new Player(PropertyManager.INSTANCE.getString("hallOfFame3")));
        hallOfFameBox.getChildren().setAll(p1Entry, p2Entry, p3Entry);
        hallOfFameBox.relocate((WIDTH - hallOfFameBox.getPrefWidth()) * 0.5, (HEIGHT - hallOfFameBox.getPrefHeight()) * 0.5);

        Helper.enableNode(playerInitialsLabel, false);
        Helper.enableNode(playerInitialsDigits, false);
        Helper.enableNode(saveInitialsButton, false);

        PauseTransition waitForHallOfFame = new PauseTransition(Duration.millis(3000));
        waitForHallOfFame.setOnFinished(a -> reInitGame());
        waitForHallOfFame.play();
    }


    // ******************** Space Object Classes ******************************
    private abstract class Sprite {
        protected final Random rnd;
        public          Image   image;
        public          double  x;
        public          double  y;
        public          double  r;
        public          double  vX;
        public          double  vY;
        public          double  vR;
        public          double  width;
        public          double  height;
        public          double  size;
        public          double  radius;
        public          boolean toBeRemoved;


        public Sprite() {
            this(null, 0, 0, 0, 0, 0, 0);
        }
        public Sprite(final Image image) {
            this(image, 0, 0, 0, 0, 0, 0);
        }
        public Sprite(final Image image, final double x, final double y) {
            this(image, x, y, 0, 0, 0, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double vX, final double vY) {
            this(image, x, y, 0, vX, vY, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double r, final double vX, final double vY) {
            this(image, x, y, r, vX, vY, 0);
        }
        public Sprite(final Image image, final double x, final double y, final double r, final double vX, final double vY, final double vR) {
            this.rnd         = new Random();
            this.image       = image;
            this.x           = x;
            this.y           = y;
            this.r           = r;
            this.vX          = vX;
            this.vY          = vY;
            this.vR          = vR;
            computeImageSizeDependentFields();
            this.toBeRemoved = false;
        }

        protected void computeImageSizeDependentFields() {
            width = WebFxUtil.getImageWidth(image);
            height = WebFxUtil.getImageHeight(image);
            size = Math.max(width, height);
            radius = size * 0.5;
            WebFxUtil.onImageLoadedIfLoading(image, () -> {
                computeImageSizeDependentFields();
                update();
            });
        }

        protected void init() {}

        public void respawn() {}

        public abstract void update();
    }

    private abstract class AnimatedSprite extends Sprite {
        protected final int    maxFrameX;
        protected final int    maxFrameY;
        protected       double scale;
        protected       int    countX;
        protected       int    countY;


        public AnimatedSprite(final int maxFrameX, final int maxFrameY, final double scale) {
            this(0, 0, 0, 0, 0, 0, maxFrameX, maxFrameY, scale);
        }
        public AnimatedSprite(final double x, final double y, final double vX, final double vY, final int maxFrameX, final int maxFrameY, final double scale) {
            this(x, y, 0, vX, vY, 0, maxFrameX, maxFrameY, scale);
        }
        public AnimatedSprite(final double x, final double y, final double r, final double vX, final double vY, final double vR, final int maxFrameX, final int maxFrameY, final double scale) {
            super(null, x, y, r, vX, vY, vR);
            this.maxFrameX = maxFrameX;
            this.maxFrameY = maxFrameY;
            this.scale     = scale;
            this.countX    = 0;
            this.countY    = 0;
        }

        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private abstract class Bonus extends Sprite {
        protected final double  xVariation   = 2;
        protected final double  minSpeedY    = 2;
        protected final double  minRotationR = 0.1;
        protected       double  imgCenterX;
        protected       double  imgCenterY;
        protected       double  cX;
        protected       double  cY;
        protected       double  rot;
        protected       boolean rotateRight;
        protected       double  vYVariation;

        public Bonus(final Image image) {
            super(image);
        }
    }

    private class Star {
        private final Random rnd        = new Random();
        private final double xVariation = 0;
        private final double minSpeedY  = 4;
        private       double x;
        private       double y;
        private       double size;
        private       double vX;
        private       double vY;
        private       double vYVariation;


        public Star() {
            // Random size
            size = rnd.nextInt(2) + 1;

            // Position
            x = (int)(rnd.nextDouble() * WIDTH);
            y = -size;

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            // Velocity
            vX = (int) (Math.round((rnd.nextDouble() * xVariation) - xVariation * 0.5));
            vY = (int) (Math.round(((rnd.nextDouble() * 1.5) + minSpeedY) * vYVariation));
        }


        private void respawn() {
            x = (int) (RND.nextDouble() * WIDTH);
            y = -size;
        }

        private void update() {
            x += vX;
            y += vY;

            // Respawn star
            if(y > HEIGHT + size) {
                respawn();
            }
        }
    }

    private class Player implements Comparable<Player> {
        private final String id;
        private       String name;
        private       Long   score;


        public Player(final String propertyString) {
            this(propertyString.split(",")[0], propertyString.split(",")[1], Long.valueOf(propertyString.split(",")[2]));
        }
        public Player(final String name, final Long score) {
            this(Uuid.randomUuid(), name, score);
        }
        public Player(final String id, final String name, final Long score) {
            this.id    = id;
            this.name  = name;
            this.score = score;
        }


        @Override public int compareTo(final Player player) {
            return Long.compare(player.score, this.score);
        }

        public String toPropertyString() {
            return new StringBuilder(this.id).append(",").append(this.name).append(",").append(this.score).toString();
        }

        @Override public String toString() {
            return new StringBuilder().append("{ ")
                                      .append("\"id\"").append(":").append(id).append(",")
                                      .append("\"name\"").append(":").append(name).append(",")
                                      .append("\"score\"").append(":").append(score)
                                      .append(" }")
                                      .toString();
        }
    }

    private class Wave {
        private static final long         ENEMY_SPAWN_INTERVAL = 250_000_000l;
        private final        WaveType     waveType1;
        private final        WaveType     waveType2;
        private final        SpaceShip    spaceShip;
        private final        int          noOfEnemies;
        private final        int          noOfSmartEnemies;
        private final        Image        image;
        private final        boolean      canFire;
        private final        boolean      canBomb;
        private final        List<Enemy>  enemies;
        private final        List<Enemy>  smartEnemies;
        private              int          enemiesSpawned;
        private              long         lastEnemySpawned;
        private              boolean      alternateWaveType;
        private              boolean      toggle;
        private              boolean      isRunning;


        public Wave(final WaveType waveType, final SpaceShip spaceShip, final int noOfEnemies, final Image image, final boolean canFire, final boolean canBomb) {
            this(waveType, null, spaceShip, noOfEnemies, image, canFire, canBomb);
        }
        public Wave(final WaveType waveType1, final WaveType waveType2, final SpaceShip spaceShip, final int noOfEnemies, final Image image, final boolean canFire, final boolean canBomb) {
            if (null == waveType1) { throw new IllegalArgumentException("You need at least define waveType1."); }
            this.waveType1         = waveType1;
            this.waveType2         = waveType2;
            this.spaceShip         = spaceShip;
            this.noOfEnemies       = noOfEnemies;
            this.noOfSmartEnemies  = level.getDifficulty().noOfSmartEnemies;
            this.image             = image;
            this.canFire           = canFire;
            this.canBomb           = canBomb;
            this.enemies           = new ArrayList<>(noOfEnemies);
            this.smartEnemies      = new ArrayList<>();
            this.enemiesSpawned    = 0;
            this.alternateWaveType = null == waveType2 ? false : true;
            this.toggle            = true;
            this.isRunning         = true;
        }


        public void update(final GraphicsContext ctx) {
            if (isRunning) {
                if (enemiesSpawned < noOfEnemies && WebFxUtil.nanoTime() - lastEnemySpawned > ENEMY_SPAWN_INTERVAL) {
                    Enemy enemy = spawnEnemy();
                    if (smartEnemies.size() < level.getDifficulty().noOfSmartEnemies && RND.nextBoolean()) {
                        smartEnemies.add(enemy);
                    }
                    lastEnemySpawned = WebFxUtil.nanoTime();
                }

                enemies.forEach(enemy -> {
                    if (level.getIndex() > 1 &&
                        !enemy.smart &&
                        enemy.frameCounter > waveType1.totalFrames * 0.35 &&
                        smartEnemies.contains(enemy)) {
                        enemy.smart = RND.nextBoolean();
                    }

                    enemy.update();

                    ctx.save();
                    ctx.translate(enemy.x - enemy.radius, enemy.y - enemy.radius);
                    ctx.save();
                    ctx.translate(enemy.radius, enemy.radius);
                    ctx.rotate(enemy.r);
                    ctx.translate(-enemy.radius, -enemy.radius);
                    ctx.drawImage(enemy.image, 0, 0);
                    ctx.restore();
                    ctx.restore();

                    // Check for torpedo hits
                    for (Torpedo torpedo : torpedos) {
                        if (isHitCircleCircle(torpedo.x, torpedo.y, torpedo.radius, enemy.x, enemy.y, enemy.radius)) {
                            explosions.add(new Explosion(enemy.x - EXPLOSION_FRAME_WIDTH * 0.25, enemy.y - EXPLOSION_FRAME_HEIGHT * 0.25, enemy.vX, enemy.vY, 0.35));
                            score += enemy.value;
                            kills++;
                            levelKills++;
                            enemy.toBeRemoved = true;
                            torpedo.toBeRemoved = true;
                            playSound(spaceShipExplosionSound);
                        }
                    }

                    // Check for bigTorpedo hits
                    for (BigTorpedo bigTorpedo : bigTorpedos) {
                        if (isHitCircleCircle(bigTorpedo.x, bigTorpedo.y, bigTorpedo.radius, enemy.x, enemy.y, enemy.radius)) {
                            explosions.add(new Explosion(enemy.x - EXPLOSION_FRAME_WIDTH * 0.25, enemy.y - EXPLOSION_FRAME_HEIGHT * 0.25, enemy.vX, enemy.vY, 0.35));
                            score += enemy.value;
                            kills++;
                            levelKills++;
                            enemy.toBeRemoved = true;
                            bigTorpedo.toBeRemoved = true;
                            playSound(spaceShipExplosionSound);
                        }
                    }

                    // Check for rocket hits
                    for (Rocket rocket : rockets) {
                        if (isHitCircleCircle(rocket.x, rocket.y, rocket.radius, enemy.x, enemy.y, enemy.radius)) {
                            rocketExplosions.add(new RocketExplosion(enemy.x - ROCKET_EXPLOSION_FRAME_WIDTH * 0.25, enemy.y - ROCKET_EXPLOSION_FRAME_HEIGHT * 0.25, enemy.vX, enemy.vY, 0.5));
                            score += enemy.value;
                            kills++;
                            levelKills++;
                            enemy.toBeRemoved = true;
                            rocket.toBeRemoved = true;
                            playSound(rocketExplosionSound);
                        }
                    }

                    // Check for space ship hit
                    if (spaceShip.isVulnerable && !hasBeenHit) {
                        boolean hit;
                        if (spaceShip.shield) {
                            hit = isHitCircleCircle(spaceShip.x, spaceShip.y, deflectorShieldRadius, enemy.x, enemy.y, enemy.radius);
                        } else {
                            hit = isHitCircleCircle(spaceShip.x, spaceShip.y, spaceShip.radius, enemy.x, enemy.y, enemy.radius);
                        }
                        if (hit) {
                            if (spaceShip.shield) {
                                explosions.add(new Explosion(enemy.x - EXPLOSION_FRAME_WIDTH * 0.125, enemy.y - EXPLOSION_FRAME_HEIGHT * 0.125, enemy.vX, enemy.vY, 0.35));
                                playSound(spaceShipExplosionSound);
                            } else {
                                spaceShipExplosion.countX = 0;
                                spaceShipExplosion.countY = 0;
                                spaceShipExplosion.x      = spaceShip.x - SPACESHIP_EXPLOSION_FRAME_WIDTH;
                                spaceShipExplosion.y      = spaceShip.y - SPACESHIP_EXPLOSION_FRAME_HEIGHT;
                                playSound(spaceShipExplosionSound);
                                hasBeenHit = true;
                                noOfLifes--;
                                if (0 == noOfLifes) {
                                    gameOver();
                                }
                            }
                            enemy.toBeRemoved = true;
                        }
                    }
                });

                enemies.removeIf(enemy -> enemy.toBeRemoved);
                if (enemies.isEmpty() && enemiesSpawned == noOfEnemies) { isRunning = false; }
            }
        }

        private Enemy spawnEnemy() {
            Enemy enemy;
            if (alternateWaveType) {
                enemy = new Enemy(toggle ? waveType1 : waveType2, spaceShip, image, canFire, canBomb);
            } else {
                enemy = new Enemy(waveType1, spaceShip, image, canFire, canBomb);
            }
            toggle = !toggle;
            enemies.add(enemy);
            enemiesSpawned++;
            return enemy;
        }
    }


    // ******************** Sprites *******************************************
    private class SpaceShip extends Sprite {
        private static final long      INVULNERABLE_TIME = 3_000_000_000l;
        private        final Image     imageUp;
        private        final Image     imageDown;
        private              long      born;
        private              boolean   shield;
        public               boolean   isVulnerable;


        public SpaceShip(final Image image, final Image imageUp, final Image imageDown) {
            super(image);
            this.imageUp   = imageUp;
            this.imageDown = imageDown;
            init();
        }


        @Override protected void init() {
            this.born         = WebFxUtil.nanoTime();
            this.x            = WIDTH * 0.5;
            computeImageSizeDependentFields();
            this.vX           = 0;
            this.vY           = 0;
            this.shield       = false;
            this.isVulnerable = false;
        }

        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            this.y            = HEIGHT - 2 * height;
        }

        @Override public void respawn() {
            this.vX           = 0;
            this.vY           = 0;
            this.shield       = false;
            this.born         = WebFxUtil.nanoTime();
            this.isVulnerable = false;
        }

        @Override public void update() {
            if (!isVulnerable && WebFxUtil.nanoTime() - born > INVULNERABLE_TIME) {
                isVulnerable = true;
            }
            x += vX;
            y += vY;
            if (x + width * 0.5 > WIDTH) {
                x = WIDTH - width * 0.5;
            }
            if (x - width * 0.5 < 0) {
                x = width * 0.5;
            }
            if (y + height * 0.5 > HEIGHT) {
                y = HEIGHT - height * 0.5;
            }
            if (y - height * 0.5 < 0) {
                y = height * 0.5;
            }
            shipTouchArea.setCenterX(x);
            shipTouchArea.setCenterY(y);
        }
    }

    private class Asteroid extends Sprite {
        private static final int     MAX_VALUE      = 10;
        private final        Random  rnd            = new Random();
        private final        double  xVariation     = 2;
        private final        double  minSpeedY      = 2;
        private final        double  minRotationR   = 0.1;
        private              double  imgCenterX;
        private              double  imgCenterY;
        private              double  radius;
        private              double  cX;
        private              double  cY;
        private              double  rot;
        private              boolean rotateRight;
        private              double  scale;
        private              double  vYVariation;
        private              int     value;
        private              int     hits;


        public Asteroid(final Image image) {
            super(image);
            init();
        }


        @Override protected void init() {
            // Position
            x   = rnd.nextDouble() * WIDTH;
            y   = -WebFxUtil.getImageHeight(image);
            rot = 0;

            // Random Size
            scale = (rnd.nextDouble() * 0.4) + 0.4;

            // No of hits (0.2 - 0.8)
            hits = (int) (scale * 5.0);

            // Value
            value = (int) (1 / scale * MAX_VALUE);

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            computeImageSizeDependentFields();

            // Velocity
            vX          = ((rnd.nextDouble() * xVariation) - xVariation * 0.5) * VELOCITY_FACTOR_X;
            vY          = (((rnd.nextDouble() * 1.5) + minSpeedY * 1/scale) * vYVariation) * VELOCITY_FACTOR_Y;
            vR          = ((rnd.nextDouble() * 0.5) + minRotationR) * VELOCITY_FACTOR_R;
            rotateRight = rnd.nextBoolean();
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            width      = WebFxUtil.getImageWidth(image) * scale;
            height     = WebFxUtil.getImageHeight(image) * scale;
            size       = Math.max(width, height);
            radius     = size * 0.5;
            imgCenterX = WebFxUtil.getImageWidth(image) * 0.5;
            imgCenterY = WebFxUtil.getImageHeight(image) * 0.5;
        }

        @Override public void respawn() {
            this.image = asteroidImages[RND.nextInt(asteroidImages.length)];
            init();
        }

        @Override public void update() {
            x += vX;
            y += vY;

            cX = x + imgCenterX;
            cY = y + imgCenterY;

            if (rotateRight) {
                rot += vR;
                if (rot > 360) { rot = 0; }
            } else {
                rot -= vR;
                if (rot < 0) { rot = 360; }
            }

            // Respawn asteroid
            if(x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                respawn();
            }
        }
    }

    private class Torpedo extends Sprite {

        public Torpedo(final Image image, final double x, final double y) {
            super(image, x, y - WebFxUtil.getImageHeight(image), 0, TORPEDO_SPEED);
        }


        @Override public void update() {
            y -= vY;
            if (y < -size) {
                toBeRemoved = true;
            }
        }
    }

    private class BigTorpedo extends Sprite {

        public BigTorpedo(final Image image, final double x, final double y, final double vX, final double vY, final double r) {
            super(image, x, y, r, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;
            if (x < -width || x > WIDTH + width || y < -height || y > HEIGHT + height) {
                toBeRemoved = true;
            }
        }
    }

    private class Rocket extends Sprite {
        public double halfWidth;
        public double halfHeight;


        public Rocket(final Image image, final double x, final double y) {
            super(image, x, y - WebFxUtil.getImageHeight(image), 0, ROCKET_SPEED);
            halfWidth  = width * 0.5;
            halfHeight = height * 0.5;
        }


        @Override public void update() {
            y -= vY;
            if (y < -size) {
                toBeRemoved = true;
            }
        }
    }

    private class Enemy extends Sprite {
        public static final  long      TIME_BETWEEN_SHOTS  = 500_000_000l;
        public static final  long      TIME_BETWEEN_BOMBS  = 1_000_000_000l;
        public static final  double    HALF_ANGLE_OF_SIGHT = 5;
        private static final double    BOMB_RANGE          = 10;
        private static final int       MAX_VALUE           = 50;
        private final        WaveType  waveType;
        public               int       frameCounter;
        private              SpaceShip spaceShip;
        public               boolean   canFire;
        public               boolean   canBomb;
        public               boolean   smart;
        private              int       noOfBombs;
        private              double    oldX;
        private              double    oldY;
        private              double    dX;
        private              double    dY;
        private              double    dist;
        private              double    factor;
        public               int       value;
        public               long      lastShot;
        public               long      lastBomb;
        public               boolean   toBeRemoved;


        public Enemy(final WaveType waveType, final SpaceShip spaceShip, final Image image, final boolean canFire, final boolean canBomb) {
            this(waveType, spaceShip, image, canFire, canBomb, false);
        }
        public Enemy(final WaveType waveType, final SpaceShip spaceShip, final Image image, final boolean canFire, final boolean canBomb, final boolean smart) {
            super(image);
            this.waveType     = waveType;
            this.frameCounter = 0;
            this.spaceShip    = spaceShip;
            this.canFire      = canFire;
            this.canBomb      = canBomb;
            this.noOfBombs    = NO_OF_ENEMY_BOMBS;
            this.toBeRemoved  = false;
            this.smart        = smart;
            init();
        }


        @Override protected void init() {
            x    = waveType.coordinates.get(0).x;
            y    = waveType.coordinates.get(0).y;
            r    = waveType.coordinates.get(0).r;
            oldX = x;
            oldY = y;

            // Value
            value = rnd.nextInt(MAX_VALUE) + 1;

            computeImageSizeDependentFields();

            // Velocity
            vX = 0;
            vY = 1;

            lastShot = WebFxUtil.nanoTime();
        }

        @Override public void update() {
            if (toBeRemoved) { return; }
            oldX = x;
            oldY = y;
            if (smart) {
                dX     = spaceShip.x - x;
                dY     = spaceShip.y - y;
                dist   = Math.sqrt(dX * dX + dY * dY);
                factor = ENEMY_SPEED / dist;
                if (spaceShip.isVulnerable && spaceShip.y > y && y < OUT_OF_SENSING_HEIGHT) {
                    vX = dX * factor;
                    vY = dY * factor;
                }
                x += vX;
                y += vY;
                r = Math.toDegrees(Math.atan2(vY, vX)) - 90;
            } else {
                x  = waveType.coordinates.get(frameCounter).x;
                y  = waveType.coordinates.get(frameCounter).y;
                r  = waveType.coordinates.get(frameCounter).r;
                vX = x - oldX;
                vY = y - oldY;
            }

            long now = WebFxUtil.nanoTime();

            if (canFire) {
                if (now - lastShot > TIME_BETWEEN_SHOTS) {
                    double[] p0 = { x, y };
                    double[] p1 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, -HALF_ANGLE_OF_SIGHT);
                    double[] p2 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, HALF_ANGLE_OF_SIGHT);

                    double area = 0.5 * (-p1[1] * p2[0] + p0[1] * (-p1[0] + p2[0]) + p0[0] * (p1[1] - p2[1]) + p1[0] * p2[1]);
                    double s    = 1 / (2 * area) * (p0[1] * p2[0] - p0[0] * p2[1] + (p2[1] - p0[1]) * spaceShip.x + (p0[0] - p2[0]) * spaceShip.y);
                    double t    = 1 / (2 * area) * (p0[0] * p1[1] - p0[1] * p1[0] + (p0[1] - p1[1]) * spaceShip.x + (p1[0] - p0[0]) * spaceShip.y);
                    if (s > 0 && t > 0 && 1 - s - t > 0) {
                        spawnEnemyTorpedo(x, y, vX * 2, vY * 2);
                        lastShot = now;
                    }
                }
            }

            if (canBomb && now - lastBombDropped > BOMB_DROP_INTERVAL && noOfBombs > 0) {
                if (now - lastBomb > TIME_BETWEEN_BOMBS && spaceShip.y > y) {
                    if (spaceShip.x > x - BOMB_RANGE && spaceShip.x < x + BOMB_RANGE) {
                        spawnEnemyBomb(x, y);
                        lastBomb        = now;
                        lastBombDropped = now;
                        noOfBombs--;
                    }
                }
            }

            // Remove Enemy
            if (smart) {
                if(x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                    toBeRemoved = true;
                }
            } else {
                frameCounter++;
                if (frameCounter >= waveType.totalFrames) {
                    toBeRemoved = true;
                }
            }
        }
    }

    private class EnemyTorpedo extends Sprite {

        public EnemyTorpedo(final Image image, final double x, final double y, final double vX, final double vY) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class EnemyBomb extends Sprite {

        public EnemyBomb(final Image image, final double x, final double y, final double vX, final double vY) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class EnemyBoss extends Sprite {
        private static final int       MAX_VALUE            = 100;
        private static final long      TIME_BETWEEN_SHOTS   = 500_000_000l;
        private static final long      TIME_BETWEEN_ROCKETS = 5_000_000_000l;
        private static final double    HALF_ANGLE_OF_SIGHT  = 10;
        private final        SpaceShip spaceShip;
        private              double    dX;
        private              double    dY;
        private              double    dist;
        private              double    factor;
        private              int       value;
        private              int       hits;
        private              long      lastShot;
        private              long      lastRocket;
        private              boolean   hasRockets;


        public EnemyBoss(final SpaceShip spaceShip, final Image image, final boolean hasRockets) {
            super(image);
            this.spaceShip  = spaceShip;
            this.hasRockets = hasRockets;
            init();
        }


        @Override protected void init() {
            // Position
            x = rnd.nextDouble() * WIDTH;
            y = -WebFxUtil.getImageHeight(image);

            // Value
            value = rnd.nextInt(MAX_VALUE) + 1;

            computeImageSizeDependentFields();

            // Velocity
            vX = 0;
            vY = ENEMY_BOSS_SPEED;

            // No of hits
            hits = 5;
        }

        @Override public void update() {
            dX     = spaceShip.x - x;
            dY     = spaceShip.y - y;
            dist   = Math.sqrt(dX * dX + dY * dY);
            factor = ENEMY_BOSS_SPEED / dist;
            if (spaceShip.isVulnerable && y < OUT_OF_SENSING_HEIGHT) {
                vX = dX * factor;
                vY = dY * factor;
            }

            x += vX;
            y += vY;

            r = Math.toDegrees(Math.atan2(vY, vX)) - 90;

            long now = WebFxUtil.nanoTime();

            if (hasRockets) {
                if (now - lastRocket > TIME_BETWEEN_ROCKETS) {
                    double[] p0 = { x, y };
                    double[] p1 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, -HALF_ANGLE_OF_SIGHT);
                    double[] p2 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, HALF_ANGLE_OF_SIGHT);

                    double area = 0.5 * (-p1[1] * p2[0] + p0[1] * (-p1[0] + p2[0]) + p0[0] * (p1[1] - p2[1]) + p1[0] * p2[1]);
                    double s    = 1 / (2 * area) * (p0[1] * p2[0] - p0[0] * p2[1] + (p2[1] - p0[1]) * spaceShip.x + (p0[0] - p2[0]) * spaceShip.y);
                    double t    = 1 / (2 * area) * (p0[0] * p1[1] - p0[1] * p1[0] + (p0[1] - p1[1]) * spaceShip.x + (p1[0] - p0[0]) * spaceShip.y);
                    if (s > 0 && t > 0 && 1 - s - t > 0) {
                        spawnEnemyBossRocket(x, y);
                        lastRocket = now;
                    }
                }
            } else {
                if (now - lastShot > TIME_BETWEEN_SHOTS) {
                    double[] p0 = { x, y };
                    double[] p1 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, -HALF_ANGLE_OF_SIGHT);
                    double[] p2 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vX, y + HEIGHT * vY, x, y, HALF_ANGLE_OF_SIGHT);

                    double area = 0.5 * (-p1[1] * p2[0] + p0[1] * (-p1[0] + p2[0]) + p0[0] * (p1[1] - p2[1]) + p1[0] * p2[1]);
                    double s    = 1 / (2 * area) * (p0[1] * p2[0] - p0[0] * p2[1] + (p2[1] - p0[1]) * spaceShip.x + (p0[0] - p2[0]) * spaceShip.y);
                    double t    = 1 / (2 * area) * (p0[0] * p1[1] - p0[1] * p1[0] + (p0[1] - p1[1]) * spaceShip.x + (p1[0] - p0[0]) * spaceShip.y);
                    if (s > 0 && t > 0 && 1 - s - t > 0) {
                        spawnEnemyBossTorpedo(x, y, vX, vY);
                        lastShot = now;
                    }
                }
            }

            switch (hits) {
                case 5: image = level.getEnemyBossImg4();break;
                case 4: image = level.getEnemyBossImg3();break;
                case 3: image = level.getEnemyBossImg2();break;
                case 2: image = level.getEnemyBossImg1();break;
                case 1: image = level.getEnemyBossImg0();break;
            }

            // Remove enemy boss
            if(x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class EnemyBossTorpedo extends Sprite {

        public EnemyBossTorpedo(final Image image, final double x, final double y, final double vX, final double vY) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class EnemyBossRocket extends Sprite {
        private final long      rocketLifespan = 2_500_000_000l;
        private final SpaceShip spaceShip;
        private       long      born;
        private       double    dX;
        private       double    dY;
        private       double    dist;
        private       double    factor;


        public EnemyBossRocket(final SpaceShip spaceShip, final Image image, final double x, final double y) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, 0, 1);
            this.spaceShip = spaceShip;
            this.born      = WebFxUtil.nanoTime();
        }


        @Override public void update() {
            dX     = spaceShip.x - x;
            dY     = spaceShip.y - y;
            dist   = Math.sqrt(dX * dX + dY * dY);
            factor = ENEMY_BOSS_ROCKET_SPEED / dist;
            if (spaceShip.y > y) {
                vX = dX * factor;
                vY = dY * factor;
            }

            x += vX;
            y += vY;

            r = Math.toDegrees(Math.atan2(vY, vX)) - 90;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
            if (WebFxUtil.nanoTime() - born > rocketLifespan) {
                enemyRocketExplosions.add(new EnemyRocketExplosion(x - ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH * 0.25, y - ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT * 0.25, vX, vY, 0.5));
                toBeRemoved = true;
            }
        }
    }

    private class LevelBoss extends Sprite {
        private static final int       MAX_VALUE            = 500;
        private static final long      TIME_BETWEEN_SHOTS   = 400_000_000l;
        private static final long      TIME_BETWEEN_ROCKETS = 3_500_000_000l;
        private static final long      TIME_BETWEEN_BOMBS   = 2_500_000_000l;
        private static final double    HALF_ANGLE_OF_SIGHT  = 22;
        private static final double    BOMB_RANGE           = 50;
        private static final long      WAITING_PHASE        = 10_000_000_000l;
        private final        SpaceShip spaceShip;
        private              double    dX;
        private              double    dY;
        private              double    dist;
        private              double    factor;
        private              double    weaponSpawnY;
        private              double    vpX;
        private              double    vpY;
        private              int       value;
        private              int       hits;
        private              long      lastShot;
        private              long      lastRocket;
        private              boolean   hasRockets;
        private              boolean   hasBombs;
        private              long      waitingStart;


        public LevelBoss(final SpaceShip spaceShip, final Image image, final boolean hasRockets, final boolean hasBombs) {
            super(image);
            this.spaceShip  = spaceShip;
            this.hasRockets = hasRockets;
            this.hasBombs   = hasBombs;
            init();
        }


        @Override protected void init() {
            // Position
            x = 0.5 * WIDTH;
            y = -WebFxUtil.getImageHeight(image);

            // Value
            value = MAX_VALUE;

            computeImageSizeDependentFields();

            // Velocity
            vX = 0;
            vY = LEVEL_BOSS_SPEED;

            // Rotation
            r = 0;

            // No of hits
            hits = 80;

            waitingStart = 0;
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            weaponSpawnY = height * 0.4;
        }

        @Override public void update() {
            if (y < height * 0.6) {
                // Approaching
                vY = LEVEL_BOSS_SPEED;
            } else {
                if (waitingStart == 0) {
                    waitingStart = WebFxUtil.nanoTime();
                }
                dX     = spaceShip.x - x;
                dY     = spaceShip.y - y;
                dist   = Math.sqrt(dX * dX + dY * dY);
                factor = LEVEL_BOSS_SPEED / dist;
                vpX    = dX * factor;
                vpY    = dY * factor;

                if (WebFxUtil.nanoTime() < waitingStart + WAITING_PHASE) {
                    // Waiting
                    vX = dX * factor * 10;
                    vY = 0;
                } else if (y < OUT_OF_SENSING_HEIGHT) {
                    // Attacking
                    vX = vpX;
                    vY = vpY;
                    r  = Math.toDegrees(Math.atan2(vY, vX)) - 90;
                }
            }

            x += vX;
            y += vY;

            long now = WebFxUtil.nanoTime();

            if (hasRockets) {
                if (now - lastRocket > TIME_BETWEEN_ROCKETS) {
                    double[] p0 = { x, y };
                    double[] p1 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vpX, y + HEIGHT * vpY, x, y, -HALF_ANGLE_OF_SIGHT);
                    double[] p2 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vpX, y + HEIGHT * vpY, x, y, HALF_ANGLE_OF_SIGHT);

                    double area = 0.5 * (-p1[1] * p2[0] + p0[1] * (-p1[0] + p2[0]) + p0[0] * (p1[1] - p2[1]) + p1[0] * p2[1]);
                    double s    = 1 / (2 * area) * (p0[1] * p2[0] - p0[0] * p2[1] + (p2[1] - p0[1]) * spaceShip.x + (p0[0] - p2[0]) * spaceShip.y);
                    double t    = 1 / (2 * area) * (p0[0] * p1[1] - p0[1] * p1[0] + (p0[1] - p1[1]) * spaceShip.x + (p1[0] - p0[0]) * spaceShip.y);
                    if (s > 0 && t > 0 && 1 - s - t > 0) {
                        spawnLevelBossRocket(x, y + weaponSpawnY);
                        lastRocket = now;
                    }
                }
            }
            if (hasBombs) {
                if (now - lastBombDropped > TIME_BETWEEN_BOMBS && spaceShip.y > y) {
                    if (spaceShip.x > x - BOMB_RANGE && spaceShip.x < x + BOMB_RANGE) {
                        spawnLevelBossBomb(x, y);
                        lastBombDropped = now;
                    }
                }
            }

            if (now - lastShot > TIME_BETWEEN_SHOTS) {
                double[] p0 = { x, y };
                double[] p1 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vpX, y + HEIGHT * vpY, x, y, -HALF_ANGLE_OF_SIGHT);
                double[] p2 = Helper.rotatePointAroundRotationCenter(x + HEIGHT * vpX, y + HEIGHT * vpY, x, y, HALF_ANGLE_OF_SIGHT);

                double area = 0.5 * (-p1[1] * p2[0] + p0[1] * (-p1[0] + p2[0]) + p0[0] * (p1[1] - p2[1]) + p1[0] * p2[1]);
                double s    = 1 / (2 * area) * (p0[1] * p2[0] - p0[0] * p2[1] + (p2[1] - p0[1]) * spaceShip.x + (p0[0] - p2[0]) * spaceShip.y);
                double t    = 1 / (2 * area) * (p0[0] * p1[1] - p0[1] * p1[0] + (p0[1] - p1[1]) * spaceShip.x + (p1[0] - p0[0]) * spaceShip.y);
                if (s > 0 && t > 0 && 1 - s - t > 0) {
                    double[] tp = Helper.rotatePointAroundRotationCenter(x, y + radius, x, y, r);
                    spawnLevelBossTorpedo(tp[0], tp[1], vX, vY, r);
                    lastShot = now;
                }
            }

            // Remove level boss
            if(x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
                nextLevel();
            }
        }
    }

    private class LevelBossTorpedo extends Sprite {

        public LevelBossTorpedo(final Image image, final double x, final double y, final double vX, final double vY, final double r) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, r, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class LevelBossRocket extends Sprite {
        private final long      rocketLifespan = 3_000_000_000l;
        private final SpaceShip spaceShip;
        private       long      born;
        private       double    dX;
        private       double    dY;
        private       double    dist;
        private       double    factor;


        public LevelBossRocket(final SpaceShip spaceShip, final Image image, final double x, final double y) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, 0, 1);
            this.spaceShip = spaceShip;
            this.born      = WebFxUtil.nanoTime();
        }


        @Override public void update() {
            dX     = spaceShip.x - x;
            dY     = spaceShip.y - y;
            dist   = Math.sqrt(dX * dX + dY * dY);
            factor = ENEMY_BOSS_ROCKET_SPEED / dist;
            if (spaceShip.y > y) {
                vX = dX * factor;
                vY = dY * factor;
            }

            x += vX;
            y += vY;

            r = Math.toDegrees(Math.atan2(vY, vX)) - 90;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
            if (WebFxUtil.nanoTime() - born > rocketLifespan) {
                enemyRocketExplosions.add(new EnemyRocketExplosion(x - ENEMY_ROCKET_EXPLOSION_FRAME_WIDTH * 0.25, y - ENEMY_ROCKET_EXPLOSION_FRAME_HEIGHT * 0.25, vX, vY, 0.5));
                toBeRemoved = true;
            }
        }
    }

    private class LevelBossBomb extends Sprite {

        public LevelBossBomb(final Image image, final double x, final double y, final double vX, final double vY) {
            super(image, x - WebFxUtil.getImageWidth(image) / 2.0, y, vX, vY);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            if (spaceShip.isVulnerable && !hasBeenHit) {
                boolean hit;
                if (spaceShip.shield) {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, deflectorShieldRadius);
                } else {
                    hit = isHitCircleCircle(x, y, radius, spaceShip.x, spaceShip.y, spaceShip.radius);
                }
                if (hit) {
                    toBeRemoved = true;
                    if (spaceShip.shield) {
                        playSound(shieldHitSound);
                    } else {
                        hasBeenHit = true;
                        playSound(spaceShipExplosionSound);
                        noOfLifes--;
                        if (0 == noOfLifes) {
                            gameOver();
                        }
                    }
                }
            } else if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }


    // ******************** AnimatedSprites ***********************************
    private class EnemyRocketExplosion extends AnimatedSprite {

        public EnemyRocketExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 4, 7, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class AsteroidExplosion extends AnimatedSprite {

        public AsteroidExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 8, 7, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class Explosion extends AnimatedSprite {

        public Explosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 8, 7, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class EnemyBossExplosion extends AnimatedSprite {

        public EnemyBossExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 4, 7, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class LevelBossExplosion extends AnimatedSprite {

        public LevelBossExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 8, 3, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class UpExplosion extends AnimatedSprite {

        public UpExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 4, 7, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class SpaceShipExplosion extends AnimatedSprite {

        public SpaceShipExplosion(final double x, final double y, final double vX, final double vY) {
            super(x, y, vX, vY, 8, 6, 1.0);
        }


        @Override public void update() {
            countX++;
            if (countX == maxFrameX) {
                countX = 0;
                countY++;
                if (countY == maxFrameY) {
                    countY = 0;
                }
                if (countX == 0 && countY == 0) {
                    hasBeenHit = false;
                    spaceShip.x = WIDTH * 0.5;
                    spaceShip.y = HEIGHT - 2 * spaceShip.height;
                    shipTouchArea.setCenterX(spaceShip.x);
                    shipTouchArea.setCenterY(spaceShip.y);
                }
            }
        }
    }

    private class RocketExplosion extends AnimatedSprite {

        public RocketExplosion(final double x, final double y, final double vX, final double vY, final double scale) {
            super(x, y, vX, vY, 5, 4, scale);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class Hit extends AnimatedSprite {

        public Hit(final double x, final double y, final double vX, final double vY) {
            super(x, y, vX, vY, 5, 2, 1.0);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }

    private class EnemyHit extends AnimatedSprite {

        public EnemyHit(final double x, final double y, final double vX, final double vY) {
            super(x, y, vX, vY, 5, 2, 1.0);
        }


        @Override public void update() {
            x += vX;
            y += vY;

            countX++;
            if (countX == maxFrameX) {
                countY++;
                if (countX == maxFrameX && countY == maxFrameY) {
                    toBeRemoved = true;
                }
                countX = 0;
                if (countY == maxFrameY) {
                    countY = 0;
                }
            }
        }
    }


    // ******************** Bonuses *******************************************
    private class ShieldUp extends Bonus {

        public ShieldUp(final Image image) {
            super(image);
            init();
        }


        @Override protected void init() {
            // Position
            x = rnd.nextDouble() * WIDTH;
            y = - WebFxUtil.getImageHeight(image);
            rot = 0;

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            computeImageSizeDependentFields();

            // Velocity
            if (x < FIRST_QUARTER_WIDTH) {
                vX = rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else if (x > LAST_QUARTER_WIDTH) {
                vX = -rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else {
                vX = ((rnd.nextDouble() * xVariation) - xVariation * 0.5) * VELOCITY_FACTOR_X;
            }
            vY = (((rnd.nextDouble() * 1.5) + minSpeedY) * vYVariation) * VELOCITY_FACTOR_Y;
            vR = (((rnd.nextDouble()) * 0.5) + minRotationR) * VELOCITY_FACTOR_R;
            rotateRight = rnd.nextBoolean();
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            imgCenterX = width * 0.5;
            imgCenterY = height * 0.5;
        }

        @Override public void update() {
            x += vX;
            y += vY;

            cX = x + imgCenterX;
            cY = y + imgCenterY;

            if (rotateRight) {
                rot += vR;
                if (rot > 360) { rot = 0; }
            } else {
                rot -= vR;
                if (rot < 0) { rot = 360; }
            }

            // Remove shieldUp
            if (x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class LifeUp extends Bonus {

        public LifeUp(final Image image) {
            super(image);
            init();
        }


        @Override protected void init() {
            // Position
            x = rnd.nextDouble() * WIDTH;
            y = - WebFxUtil.getImageHeight(image);
            rot = 0;

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            computeImageSizeDependentFields();

            // Velocity
            if (x < FIRST_QUARTER_WIDTH) {
                vX = rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else if (x > LAST_QUARTER_WIDTH) {
                vX = -rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else {
                vX = ((rnd.nextDouble() * xVariation) - xVariation * 0.5) * VELOCITY_FACTOR_X;
            }
            vY = (((rnd.nextDouble() * 1.5) + minSpeedY) * vYVariation) * VELOCITY_FACTOR_Y;
            vR = (((rnd.nextDouble()) * 0.5) + minRotationR) * VELOCITY_FACTOR_R;
            rotateRight = rnd.nextBoolean();
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            imgCenterX = width * 0.5;
            imgCenterY = height * 0.5;
        }

        @Override public void update() {
            x += vX;
            y += vY;

            cX = x + imgCenterX;
            cY = y + imgCenterY;

            if (rotateRight) {
                rot += vR;
                if (rot > 360) { rot = 0; }
            } else {
                rot -= vR;
                if (rot < 0) { rot = 360; }
            }

            // Remove lifeUp
            if (x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class BigTorpedoBonus extends Bonus {

        public BigTorpedoBonus(final Image image) {
            super(image);
            init();
        }


        @Override protected void init() {
            // Position
            x = rnd.nextDouble() * WIDTH;
            y = -WebFxUtil.getImageHeight(image);
            rot = 0;

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            computeImageSizeDependentFields();

            // Velocity
            if (x < FIRST_QUARTER_WIDTH) {
                vX = rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else if (x > LAST_QUARTER_WIDTH) {
                vX = -rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else {
                vX = ((rnd.nextDouble() * xVariation) - xVariation * 0.5) * VELOCITY_FACTOR_X;
            }
            vY = (((rnd.nextDouble() * 1.5) + minSpeedY) * vYVariation) * VELOCITY_FACTOR_Y;
            vR = (((rnd.nextDouble()) * 0.5) + minRotationR) * VELOCITY_FACTOR_R;
            rotateRight = rnd.nextBoolean();
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            imgCenterX = width * 0.5;
            imgCenterY = height * 0.5;
        }

        @Override public void update() {
            x += vX;
            y += vY;

            cX = x + imgCenterX;
            cY = y + imgCenterY;

            if (rotateRight) {
                rot += vR;
                if (rot > 360) { rot = 0; }
            } else {
                rot -= vR;
                if (rot < 0) { rot = 360; }
            }

            // Remove lifeUp
            if (x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }

    private class StarburstBonus extends Bonus {

        public StarburstBonus(final Image image) {
            super(image);
            init();
        }


        @Override protected void init() {
            // Position
            x = rnd.nextDouble() * WIDTH;
            y = -WebFxUtil.getImageHeight(image);
            rot = 0;

            // Random Speed
            vYVariation = (rnd.nextDouble() * 0.5) + 0.2;

            computeImageSizeDependentFields();

            // Velocity
            if (x < FIRST_QUARTER_WIDTH) {
                vX = rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else if (x > LAST_QUARTER_WIDTH) {
                vX = -rnd.nextDouble() * VELOCITY_FACTOR_X;
            } else {
                vX = ((rnd.nextDouble() * xVariation) - xVariation * 0.5) * VELOCITY_FACTOR_X;
            }
            vY = (((rnd.nextDouble() * 1.5) + minSpeedY) * vYVariation) * VELOCITY_FACTOR_Y;
            vR = (((rnd.nextDouble()) * 0.5) + minRotationR) * VELOCITY_FACTOR_R;
            rotateRight = rnd.nextBoolean();
        }

        @Override
        protected void computeImageSizeDependentFields() {
            super.computeImageSizeDependentFields();
            imgCenterX = width * 0.5;
            imgCenterY = height * 0.5;
        }

        @Override public void update() {
            x += vX;
            y += vY;

            cX = x + imgCenterX;
            cY = y + imgCenterY;

            if (rotateRight) {
                rot += vR;
                if (rot > 360) { rot = 0; }
            } else {
                rot -= vR;
                if (rot < 0) { rot = 360; }
            }

            // Remove lifeUp
            if (x < -size || x - radius > WIDTH || y - height > HEIGHT) {
                toBeRemoved = true;
            }
        }
    }
}
