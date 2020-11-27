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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.demo.mandelbrot.tracerframework.TracerView;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotApplication extends Application {

    private final static double MAX_PIXELS_COUNT = 640 * 480; // Limiting the frame weight as we will take a snapshot for each

    @Override
    public void start(Stage primaryStage) {
        // Deciding the canvas size
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double w = screenBounds.getWidth(), h = screenBounds.getHeight(), r = w / h, wh = w * h;
        if (wh > MAX_PIXELS_COUNT) {
            w = Math.sqrt(MAX_PIXELS_COUNT * r);
            h = w / r;
        }

        // Creating the scene with the specified size (this size is ignored if running in the browser)
        primaryStage.setScene(new Scene(new TracerView((int) w, (int) h, new MandelbrotPixelComputer()).buildView(), w, h));
        primaryStage.setTitle("WebFx Mandelbrot");
        primaryStage.show();
    }
}