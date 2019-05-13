package webfx.tutorial.css;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
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

    private final Scene scene = new Scene(new StackPane(), 800, 400);

    @Override
    public void start(Stage primaryStage) {
        scene.getStylesheets().add("webfx/tutorial/css/css/app.css");
        primaryStage.setTitle("CSS tutorial");
        primaryStage.setScene(scene);
        createSwitchControl(); // create control to switch between CSS and code version (starting with CSS version)
        primaryStage.show();
    }

    /***** CSS version of the UI *****/

    private Parent createCssUi() {
        Region bg = new Region();
        Text title = new Text("WebFX");
        Text subTitle = new SvgText("JavaFX in the browser");
        VBox vBox = new VBox(title, subTitle);
        vBox.setAlignment(Pos.CENTER);

        bg.setId("bg");
        title.getStyleClass().addAll("title", "withStroke", "withShadow");
        subTitle.getStyleClass().addAll("subtitle", "withStroke", "withShadow", "withAnimatedDash");

        return new StackPane(bg, vBox);
    }

    /***** Code version of the UI *****/

    private Timeline codeTimeline = new Timeline();

    private Parent createCodeUi() {
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

        codeTimeline.getKeyFrames().setAll(new KeyFrame(new Duration(2000), new KeyValue(subTitle.strokeDashOffsetProperty(), 25)));
        codeTimeline.setCycleCount(Animation.INDEFINITE);
        codeTimeline.play();

        return new StackPane(bg, vBox);
    }

    private void applyFontStrokeAndShadow(Text text, double fontSize) {
        text.setFont(Font.font("Times New Roman", fontSize));
        text.setStroke(Color.WHITE);
        text.setStrokeWidth(2);
        text.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.GREY, 10, 0, 0, 15));
    }





    /***** Switch control *****/

    private Pane switchControl;
    private boolean cssVersion;

    private void createSwitchControl() {
        double radius = 10, gap = 2, w = 80, h = 2 * (radius + gap);
        double centerX = w - radius - gap;
        Circle c = new Circle(centerX, h / 2, radius, Color.LIGHTGRAY);
        Text cssText = new Text("CSS");
        cssText.setFont(Font.font("Sans Serif", 18));
        cssText.setFill(Color.WHITE);
        cssText.setX(centerX - w / 2 - 15);
        cssText.setY(h / 2);
        cssText.setTextOrigin(VPos.CENTER);
        Text codeText = new Text("Code");
        codeText.setFont(Font.font("Sans Serif", 18));
        codeText.setFill(Color.WHITE);
        codeText.setX(centerX + w / 2 - 21);
        codeText.setY(h / 2);
        codeText.setTextOrigin(VPos.CENTER);
        Pane pane = new Pane(cssText, c, codeText);
        switchControl = new Pane(pane);
        switchControl.setId("switch");
        switchControl.setBackground(new Background(new BackgroundFill(Color.PURPLE, new CornerRadii(radius), null)));
        switchControl.setMaxWidth(w);
        switchControl.setMaxHeight(h);
        switchControl.setClip(new Rectangle(w, h));
        StackPane.setAlignment(switchControl, Pos.TOP_CENTER);
        StackPane.setMargin(switchControl, new Insets(20));

        Translate translate = new Translate();
        pane.getTransforms().setAll(translate);
        switchControl.setOnMouseClicked(e -> {
            switchUi();
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(new KeyFrame(new Duration(250), new KeyValue(translate.xProperty(), cssVersion ? 0 : - w + 2 * (radius + gap))));
            timeline.play();
        });

        switchUi(); // To apply the CSS version first
    }

    private void switchUi() {
        cssVersion = !cssVersion;
        scene.setRoot(new StackPane(cssVersion ? createCssUi() : createCodeUi(), switchControl));
        if (cssVersion)
            codeTimeline.stop();
    }
}
