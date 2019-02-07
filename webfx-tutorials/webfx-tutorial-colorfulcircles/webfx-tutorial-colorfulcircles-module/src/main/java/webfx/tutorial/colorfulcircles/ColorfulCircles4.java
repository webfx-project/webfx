package webfx.tutorial.colorfulcircles;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.lang.Math.random;


public class ColorfulCircles4 extends Application {

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setScene(scene);
        // Reading back the final scene width and height as they might not be actually applied (the browser doesn't allow to programmatically resize the window)
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        Group circles = new Group();
        for (int i = 0; i < 30; i++) {
            Circle circle = new Circle(sceneWidth / 2, sceneHeight / 2, 150, Color.web("white", 0.05));
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }
        Rectangle colors = new Rectangle(sceneWidth, sceneHeight,
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f"))));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());
        // The black rectangle actually needs also to be bound to the scene
        Rectangle blackRectangle = new Rectangle(sceneWidth, sceneHeight, Color.BLACK);
        blackRectangle.widthProperty().bind(scene.widthProperty());
        blackRectangle.heightProperty().bind(scene.heightProperty());
        Group blendModeGroup = new Group(new Group(blackRectangle, circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        circles.setEffect(new BoxBlur(10, 10, 3));
        Timeline timeline = new Timeline();
        for (Node circle : circles.getChildren()) {
            // Using Node.getTransforms().setAll(new Translate()) instead of Node.translateX/YProperty() (which are not yet implemented in webfx-fxkit)
            Translate translate = new Translate();
            circle.getTransforms().setAll(translate);
            timeline.getKeyFrames().addAll(
                    new KeyFrame(new Duration(10000), // set end position at 40s
                            new KeyValue(translate.xProperty(), (random() - 0.5) * sceneWidth, Interpolator.EASE_BOTH),
                            new KeyValue(translate.yProperty(), (random() - 0.5) * sceneHeight, Interpolator.EASE_BOTH)));
        }
        // play 40s of animation
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
        primaryStage.show();
    }
}