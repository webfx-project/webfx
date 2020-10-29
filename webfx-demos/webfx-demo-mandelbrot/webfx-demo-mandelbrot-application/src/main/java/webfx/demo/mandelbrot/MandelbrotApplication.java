/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package webfx.demo.mandelbrot;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import webfx.demo.mandelbrot.tracerframework.TracerView;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotApplication extends Application {

    private final static int REQUESTED_WIDTH = 640, REQUESTED_HEIGHT = 480; // Initial requested size but final size will probably be different when running in the browser
    private final static int MAX_PIXELS_COUNT = REQUESTED_WIDTH * REQUESTED_HEIGHT; // Limiting the frame weight as we will take a snapshot for each

    @Override
    public void start(Stage primaryStage) {
        // Creating the scene with the specified size (this size is ignored if running in the browser)
        Scene scene = new Scene(new Pane(), REQUESTED_WIDTH, REQUESTED_HEIGHT);
        primaryStage.setScene(scene);

        // Reading back the scene size which may finally be different in the case we run in the browser
        double finalSceneWidth  = scene.getWidth();
        double finalSceneHeight = scene.getHeight();

        // Deciding the canvas size
        int canvasWidth  = Math.min((int) finalSceneWidth,  REQUESTED_WIDTH);
        int canvasHeight = Math.min((int) finalSceneHeight, MAX_PIXELS_COUNT / canvasWidth);

        scene.setRoot(new TracerView(canvasWidth, canvasHeight, new MandelbrotPixelComputer()).buildView());

        primaryStage.setTitle("WebFx Mandelbrot");
        primaryStage.show();
    }

}