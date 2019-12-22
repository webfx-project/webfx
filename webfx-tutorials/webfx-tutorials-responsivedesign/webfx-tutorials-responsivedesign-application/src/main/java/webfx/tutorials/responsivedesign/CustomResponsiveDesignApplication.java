package webfx.tutorials.responsivedesign;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public class CustomResponsiveDesignApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Custom Responsive Design");
        Region a1 = createArea(Color.rgb(0, 52, 118));
        Region a2 = createArea(Color.rgb(0, 98, 210));
        Region a3 = createArea(Color.rgb(180, 210, 247));
        Region a4 = createArea(Color.rgb(213, 223, 239));
        Region a5 = createArea(Color.rgb(223, 225, 229));
        Pane customBox = new Pane() {
            @Override
            protected void layoutChildren() {
                double width = getWidth();
                if (width >= 800) {
                    double left = (width - 800) / 2;
                    layoutInArea(a1, left, 0, 480, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a2, left + 480, 0, 320, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a3, left, 150, 264, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a4, left + 264, 150, 264, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a5, left + 264*2, 150, 272, 150, 0, HPos.LEFT, VPos.TOP);
                } else if (width > 600) {
                    layoutInArea(a1, 0, 0, width, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a2, 0, 150, width / 2, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a3, width / 2, 150, width / 2, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a4, 0, 300, width / 2, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a5, width / 2, 300, width / 2, 150, 0, HPos.LEFT, VPos.TOP);
                } else {
                    layoutInArea(a1, 0, 0, width, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a2, 0, 150, width, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a3, 0, 300, width, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a4, 0, 450, width, 150, 0, HPos.LEFT, VPos.TOP);
                    layoutInArea(a5, 0, 600, width, 150, 0, HPos.LEFT, VPos.TOP);
                }
            }

            @Override
            protected double computePrefHeight(double width) {
                if (width == -1)
                    width = getWidth();
                return width >= 800 ? 300 : width > 600 ? 450 : 750;
            }
        };
        customBox.getChildren().setAll(a1, a2, a3, a4, a5);
        customBox.setMaxWidth(800);
        //primaryStage.setScene(new Scene(LayoutUtil.createVerticalScrollPane(customBox), 850, 250));
        primaryStage.setScene(new Scene(customBox, 850, 250));
        primaryStage.show();
    }

    private static Region createArea(Paint fill) {
        Region region = new Region();
        region.setBackground(new Background(new BackgroundFill(fill, null, null)));
        return region;
    }
}
