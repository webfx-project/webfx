package webfx.tutorial.responsivedesign;

import javafx.application.Application;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public class FlexBoxResponsiveDesignApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FlexBox Responsive Design");
/* Commented to avoid dependency with extra controls
        FlexBox flexBox = new FlexBox(
                createArea(Color.rgb(0, 52, 118), 480),
                createArea(Color.rgb(0, 98, 210), 320),
                createArea(Color.rgb(180, 210, 247), 264),
                createArea(Color.rgb(213, 223, 239), 264),
                createArea(Color.rgb(223, 225, 229), 272)
        );
        flexBox.setMaxWidth(800);
        //primaryStage.setScene(new Scene(LayoutUtil.createVerticalScrollPane(new BorderPane(flexBox)), 850, 250));
        primaryStage.setScene(new Scene(new BorderPane(flexBox), 850, 250));
*/
        primaryStage.show();
    }

    private static Region createArea(Paint fill, double minWidth) {
        Region region = new Region();
        region.setBackground(new Background(new BackgroundFill(fill, null, null)));
        region.setMinWidth(minWidth);
        region.setPrefHeight(150);
        return region;
    }
}
