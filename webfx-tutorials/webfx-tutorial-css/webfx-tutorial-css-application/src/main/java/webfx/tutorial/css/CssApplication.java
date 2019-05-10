package webfx.tutorial.css;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import webfx.fxkit.extra.controls.svg.SvgText;

/**
 * @author Bruno Salmon
 */
public final class CssApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new StackPane(), 800, 400);
        scene.getStylesheets().add("webfx/tutorial/css/css/app.css");

        ToggleGroup buttonGroup = new ToggleGroup();

        ToggleButton cssButton = new ToggleButton("CSS version");
        cssButton.setToggleGroup(buttonGroup);

        ToggleButton codeButton = new ToggleButton("Code version");
        codeButton.setToggleGroup(buttonGroup);

        HBox buttonBar = new HBox(10, cssButton, codeButton);
        buttonBar.setAlignment(Pos.TOP_CENTER);
        StackPane.setMargin(buttonBar, new Insets(10));

        cssButton .setOnAction(e -> scene.setRoot(new StackPane(createCssUi(),    buttonBar)));
        codeButton.setOnAction(e -> scene.setRoot(new StackPane(createNonCssUi(), buttonBar)));

        cssButton.fire();

        primaryStage.setTitle("CSS");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent createCssUi() {
        Region bg = new Region();
        Text title = new Text("WebFX");
        Text subTitle = new SvgText("JavaFX in the browser");
        VBox vBox = new VBox(title, subTitle);
        vBox.setAlignment(Pos.CENTER);

        bg.setId("bg");
        title.getStyleClass().addAll("title", "withStroke", "withShadow");
        subTitle.getStyleClass().addAll("subtitle", "withStroke", "withShadow", "withAnimatedDash");

        timeline.stop();
        return new StackPane(bg, vBox);
    }

    private Timeline timeline = new Timeline();

    private Parent createNonCssUi() {
        Region bg = new Region();
        Text title = new Text("WebFX");
        Text subTitle = new SvgText("JavaFX in the browser");
        VBox vBox = new VBox(title, subTitle);
        vBox.setAlignment(Pos.CENTER);

        bg.setBackground(new Background(new BackgroundFill(LinearGradient.valueOf("linear-gradient(to right, orange , yellow, green, cyan, blue, violet)"), null, null)));

        applyFontStrokeAndShadow(title, 128);
        applyFontStrokeAndShadow(subTitle, 64);

        subTitle.setStrokeLineCap(StrokeLineCap.BUTT);
        subTitle.getStrokeDashArray().setAll(5d, 20d);

        timeline.getKeyFrames().setAll(new KeyFrame(new Duration(2000), new KeyValue(subTitle.strokeDashOffsetProperty(), 25)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return new StackPane(bg, vBox);
    }

    private void applyFontStrokeAndShadow(Text text, double fontSize) {
        text.setFont(Font.font("Times New Roman", fontSize));
        text.setStroke(Color.WHITE);
        text.setStrokeWidth(2);
        text.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.GREY, 10, 0, 0, 15));
    }
}
