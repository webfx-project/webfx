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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class SpaceFX extends Application {

    public static double WINDOW_WIDTH = 700, WINDOW_HEIGHT = 900;

    private boolean torpedoArmed;
    private boolean rocketArmed;
    private boolean shieldArmed;
    private SpaceFXView view;

    @Override
    public void init() {
        torpedoArmed = true;
        rocketArmed = true;
        shieldArmed = true;
    }

    @Override
    public void start(Stage stage) {
        // Creating the scene with the specified size (this size is ignored when running in the browser)
        Scene scene = new Scene(new Pane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        // Reading back the real window size in case we run in the browser
        WINDOW_HEIGHT = scene.getHeight();
        WINDOW_WIDTH = Math.min(scene.getWidth(), WINDOW_HEIGHT);
        // Note: Config will consider these variables during initialization

        // Now that config is ready, we can instantiate SpaceFXView and make it as root
        scene.setRoot(view = new SpaceFXView(stage));

        scene.getStylesheets().add(WebFxUtil.toResourceUrl("spacefx.css"));

        scene.setOnKeyPressed(e -> {
            view.userInteracted();
            if (view.isRunning()) {
                switch (e.getCode()) {
                    case UP:
                        view.decreaseSpaceShipVy();
                        break;
                    case RIGHT:
                        view.increaseSpaceShipVx();
                        break;
                    case DOWN:
                        view.increaseSpaceShipVy();
                        break;
                    case LEFT:
                        view.decreaseSpaceShipVx();
                        break;
                    case S:
                        if (shieldArmed) {
                            view.activateSpaceShipShield();
                            shieldArmed = false;
                        }
                        break;
                    case R:
                        if (rocketArmed) {
                            view.fireSpaceShipRocket();
                            rocketArmed = false;
                        }
                        break;
                    case SPACE:
                        if (torpedoArmed) {
                            view.fireSpaceShipWeapon();
                            torpedoArmed = false;
                        }
                        break;
                }
            } else if (view.isHallOfFameScreen()) {
                switch (e.getCode()) {
                    case UP:
                        if (view.getDigit1().isSelected()) {
                            view.getDigit1().up();
                        }
                        if (view.getDigit2().isSelected()) {
                            view.getDigit2().up();
                        }
                        break;
                    case RIGHT:
                        if (view.getDigit1().isSelected()) {
                            view.getDigit2().setSelected(true);
                        }
                        break;
                    case DOWN:
                        if (view.getDigit1().isSelected()) {
                            view.getDigit1().down();
                        }
                        if (view.getDigit2().isSelected()) {
                            view.getDigit2().down();
                        }
                        break;
                    case LEFT:
                        if (view.getDigit2().isSelected()) {
                            view.getDigit1().setSelected(true);
                        }
                        break;
                    case SPACE:
                        view.storePlayer();
                        break;
                }
            } else if (e.getCode() == KeyCode.P && view.isReadyToStart()) {
                view.startGame();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (view.isRunning()) {
                switch (e.getCode()) {
                    case UP:
                        view.stopSpaceShipVy();
                        break;
                    case RIGHT:
                        view.stopSpaceShipVx();
                        break;
                    case DOWN:
                        view.stopSpaceShipVy();
                        break;
                    case LEFT:
                        view.stopSpaceShipVx();
                        break;
                    case S:
                        shieldArmed = true;
                        break;
                    case R:
                        rocketArmed = true;
                        break;
                    case SPACE:
                        torpedoArmed = true;
                        break;
                }
            }
        });
/*
        scene.setOnMousePressed(e -> {
            if (!view.isRunning() && view.isReadyToStart())
                view.startGame();
        });
*/

        stage.setTitle("SpaceFX");
        stage.show();
    }

    @Override
    public void stop() {
        WebFxUtil.exit(0);
    }
}
