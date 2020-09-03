package webfx.demos.service;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import webfx.extras.flexbox.FlexBox;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.demos.service.services.alert.AlertService;
import webfx.demos.service.services.console.Console;
import webfx.demos.service.services.forecast.ForecastService;
import webfx.demos.service.services.forecast.spi.ForecastMetrics;

import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class ForecastServiceApplication extends Application {

    private final String place = "Somewhere in the world";

    @Override
    public void start(Stage primaryStage) {
        Console.log("Starting...");
        primaryStage.setTitle("Forecast Application");
        primaryStage.setScene(new Scene(createForecastUi(), 600, 400));
        primaryStage.show();
    }


    private final BorderPane borderPane = new BorderPane();
    private final FlexBox flowPane = new FlexBox();

    private Parent createForecastUi() {
        borderPane.setBottom(flowPane);
        refreshForecasts();
        return borderPane;
    }

    private void refreshForecasts() {
        ForecastService.getWeekForecast(place).setHandler(ar -> {
            if (ar.failed())
                AlertService.alert(ar.cause().getMessage());
            else {
                flowPane.getChildren().setAll(
                        ar.result().stream()
                                .map(this::createDayForecastNode)
                                .collect(Collectors.toList()));
            }
        });
    }

    private Node createDayForecastNode(ForecastMetrics dayForecastMetrics) {
        return createDayForecastNode(dayForecastMetrics, true);
    }

    private final static Color unselectedBackgroundColor = Color.WHITE;
    private final static Color unselectedBorderColor = Color.WHITE;
    private final static Color selectedBackgroundColor = Color.grayRgb(233);
    private final static Color selectedBorderColor = Color.BLUE;
    private final static Insets margin = new Insets(5);
    private Pane lastSelectedPane;

    private Node createDayForecastNode(ForecastMetrics dayForecastMetrics, boolean thumb) {
        ImageView imageView = createSkyStateImageView(dayForecastMetrics.getSkyState());
        Pane container = new Pane(imageView) {
            @Override
            protected void layoutChildren() {
                double width = getWidth();
                double height = getHeight();
                double imageSize = Math.min(width, height);
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);
                layoutInArea(imageView, (width - imageSize) / 2, (height - imageSize) / 2, imageSize, imageSize, 0, HPos.LEFT, VPos.TOP);
            }

            @Override
            protected double computePrefHeight(double width) {
                return width >= 0 ? width : getWidth(); // make it square (height = width)
            }
        };
        container.setOnMouseClicked(event -> {
            if (lastSelectedPane != null)
                setBackgroundAndBorder(lastSelectedPane, unselectedBackgroundColor, unselectedBorderColor);
            setBackgroundAndBorder(container, selectedBackgroundColor, selectedBorderColor);
            Node mainNode = createDayForecastNode(dayForecastMetrics, false);
            mainNode.setOnMouseClicked(e -> refreshForecasts());
            borderPane.setCenter(mainNode);
            lastSelectedPane = container;
        });
        setBackgroundAndBorder(container, unselectedBackgroundColor, unselectedBorderColor);
        if (thumb)
            FlexBox.setMargin(container, margin);
        else
            BorderPane.setMargin(container, margin);
        return container;
    }

    private ImageView createSkyStateImageView(ForecastMetrics.SkyState skyState) {
        boolean supportsSvg = !"JavaFx".equals(WebFxKitLauncher.getUserAgent());
        String imageExtension = supportsSvg ? ".svg" : ".png";
        return createImageView("webfx/demos/service/services/forecast/images/" + skyState.name().toLowerCase() + imageExtension);
    }

    private ImageView createImageView(String url) {
        return url == null ? new ImageView() : new ImageView(url);
    }

    private static void setBackgroundAndBorder(Region region, Paint backgroundFill, Paint borderFill) {
        region.setBackground(new Background(new BackgroundFill(backgroundFill, null, null)));
        region.setBorder(new Border(new BorderStroke(borderFill, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
    }
}