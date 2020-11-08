package webfx.demo.tallycounter;

import eu.hansolo.fx.odometer.Odometer;
import eu.hansolo.fx.odometer.OdometerBuilder;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author Bruno Salmon
 */
public final class TallyCounterApplication extends Application {

    private int counter = 0, beforeResetCounter;
    private Odometer odometer;
    private Timeline odometerTimeline, swapTimeline;
    private boolean swapped;
    private final LedButton incrementButton = LedButton.create(Color.GREEN,  true,  this::increment);
    private final LedButton decrementButton = LedButton.create(Color.ORANGE, false, this::decrement);
    private final LedButton resetButton     = LedButton.create(Color.RED,    null,  this::reset);
    private final LedButton swapButton      = LedButton.create(Color.BLUE,    null, this::swap);
    private double leftButtonX, rightButtonX;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createTallyCounterPane(), 800, 600);
        stage.setTitle("Tally Counter");
        stage.setScene(scene);
        stage.show();
    }

    private Pane createTallyCounterPane() {
        odometer = OdometerBuilder.create()
                .digits(4)
                .decimals(0)
                .digitBackgroundColor(Color.BLACK)
                .digitForegroundColor(Color.WHITE)
                .decimalBackgroundColor(Color.BLACK)
                .decimalForegroundColor(Color.WHITE)
                .build();
        odometer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        odometer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        double gap = 10;
        Pane pane = new Pane(odometer, decrementButton, incrementButton, resetButton, swapButton) {
            @Override
            protected void layoutChildren() {
                boolean swapAnimationRunning = swapTimeline != null && swapTimeline.getStatus() == Animation.Status.RUNNING;
                Insets insets = getInsets();
                final double left = insets.getLeft();
                final double width = getWidth() - left - insets.getRight();
                final double top = insets.getTop();
                final double height = getHeight() - top - insets.getBottom();
                double odometerHeight = Math.min(height, 300);
                double odometerWidth = odometer.prefWidth(odometerHeight);
                double extraWidth = width - odometerWidth;
                double extraHeight = height - odometerHeight;
                double buttonSize;
                LedButton leftButton = swapped ? incrementButton : decrementButton;
                LedButton rightButton = swapped ? decrementButton : incrementButton;
                if (extraWidth > 1.61 * extraHeight) {
                    if (odometerWidth > 0.6 * width) {
                        odometerWidth = 0.6 * width;
                        odometerHeight = odometer.prefHeight(odometerWidth);
                        extraWidth = width - odometerWidth;
                    }
                    buttonSize = Math.min(300, Math.min(extraWidth / 2 - gap, height));
                    double baseline = top + height / 2.61;
                    layoutInArea(odometer, left + extraWidth / 2, baseline - odometerHeight / 2, odometerWidth, odometerHeight, 0, HPos.CENTER, VPos.CENTER);
                    leftButtonX = left + extraWidth / 4 - buttonSize / 2 - gap / 2;
                    rightButtonX = left + width - extraWidth / 4 - buttonSize / 2 + gap / 2;
                    if (!swapAnimationRunning) {
                        layoutInArea(leftButton, leftButtonX, baseline - buttonSize / 2, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
                        layoutInArea(rightButton, rightButtonX, baseline - buttonSize / 2, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
                    }
                } else {
                    odometerWidth = width;
                    odometerHeight = odometer.prefHeight(width);
                    double maxHeight = Math.min(300, 0.6 * height);
                    if (odometerHeight > maxHeight) {
                        odometerHeight = maxHeight;
                        odometerWidth = odometer.prefWidth(odometerHeight);
                    }
                    extraWidth = width - odometerWidth;
                    extraHeight = height - odometerHeight;
                    buttonSize = Math.min(200, Math.min(odometerWidth / 3, extraHeight));
                    double spaceY = height - odometerHeight - buttonSize;
                    layoutInArea(odometer, left + extraWidth / 2, top + spaceY / 3, odometerWidth, odometerHeight, 0, HPos.CENTER, VPos.CENTER);
                    double buttonsY = top + spaceY / 3 + odometerHeight + spaceY / 3;
                    double leftButtonCenterX = left + extraWidth / 2 + odometerWidth / 4 - gap / 2;
                    leftButtonX = leftButtonCenterX - buttonSize / 2;
                    rightButtonX = leftButtonX + odometerWidth / 2 + gap;
                    double distanceFromCorner = distance(0, getHeight(), leftButtonCenterX, buttonsY + buttonSize / 2);
                    double distanceFromRight = odometerWidth / 2 + gap;
                    double distanceDiff = distanceFromRight - distanceFromCorner;
                    if (distanceDiff > 0) {
                        double delta = distanceDiff / 2 / Math.sqrt(2);
                        leftButtonX += delta;
                        rightButtonX -= delta;
                    }
                    if (!swapAnimationRunning) {
                        layoutInArea(leftButton, leftButtonX, buttonsY, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
                        layoutInArea(rightButton, rightButtonX, buttonsY, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
                    }
                }
                double cornerButtonY = getHeight() - buttonSize / 2;
                layoutInArea(resetButton, 0 - buttonSize / 2, cornerButtonY, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(swapButton, getWidth() - buttonSize / 2, cornerButtonY, buttonSize, buttonSize, 0, HPos.CENTER, VPos.CENTER);
            }
        };
        pane.setPadding(new Insets(gap));
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        return pane;
    }

    private void increment() {
        setCounter(counter + 1);
    }

    private void decrement() {
        setCounter(counter - 1);
    }

    private void reset() {
        if (counter == 0)
            setCounter(beforeResetCounter);
        else {
            beforeResetCounter = counter;
            setCounter(0);
        }
    }

    private void swap() {
        if (swapTimeline != null)
            swapTimeline.stop();
        LedButton leftButton = swapped ? incrementButton : decrementButton;
        LedButton rightButton = swapped ? decrementButton : incrementButton;
        swapTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5),
                new KeyValue(leftButton.layoutXProperty(), rightButtonX, Interpolator.EASE_OUT),
                new KeyValue(rightButton.layoutXProperty(), leftButtonX, Interpolator.EASE_OUT)));
        swapTimeline.play();
        swapped = !swapped;
    }

    private void setCounter(int counter) {
        if (odometerTimeline != null)
            odometerTimeline.stop();
        this.counter = Math.max(counter, 0);
        odometerTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2),
                new KeyValue(odometer.valueProperty(), this.counter, Interpolator.LINEAR)));
        odometerTimeline.play();
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double a = x1 - x2, b = y1 - y2;
        return Math.sqrt(a * a + b * b);
    }

    static final class LedButton extends Region {

        private final Circle ledBorder = new Circle(), ledCentre = new Circle(), highlight = new Circle();
        private final Line hLine = new Line(), vLine = new Line();
        private final Paint pressedFill, releasedFill;
        private final InnerShadow innerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 0, 0, 0, 0);

        LedButton(Color ledColor, Boolean plus) {
            Paint borderFill = new LinearGradient( 0,  0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0,  Color.rgb( 20,  20,  20, 0.65)),
                    new Stop(0.15, Color.rgb( 20,  20,  20, 0.65)),
                    new Stop(0.26, Color.rgb( 41,  41,  41, 0.65)),
                    new Stop(0.4,  Color.rgb(100, 100, 100, 0.80)),
                    new Stop(1.0,  Color.rgb( 20,  20,  20, 0.65)));
            ledBorder.setFill(borderFill);
            pressedFill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0,  ledColor.deriveColor(0d, 1d, 0.77, 1d)),
                    new Stop(0.49, ledColor.deriveColor(0d, 1d, 0.5,  1d)),
                    new Stop(1.0,  ledColor));
            releasedFill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0,  ledColor.deriveColor(0d, 1d, 0.57, 1d)),
                    new Stop(0.49, ledColor.deriveColor(0d, 1d, 0.4,  1d)),
                    new Stop(1.0,  ledColor.deriveColor(0d, 1d, 0.2,  1d)));
            ledCentre.setFill(releasedFill);
            ledCentre.setEffect(innerShadow);
            ledCentre.setOnMousePressed(e -> {
                ledCentre.setFill(pressedFill);
                ledCentre.setEffect(new DropShadow(ledCentre.getRadius() * 0.3, ledColor));
            });
            ledCentre.setOnMouseReleased(e -> {
                ledCentre.setFill(releasedFill);
                ledCentre.setEffect(null);
            });

            Color lineColor = Color.IVORY;
            hLine.setStroke(lineColor);
            hLine.setStrokeLineCap(StrokeLineCap.ROUND);
            vLine.setStroke(lineColor);
            vLine.setStrokeLineCap(StrokeLineCap.ROUND);
            Node sign = plus == Boolean.TRUE ? new Group(hLine, vLine) : hLine;
            sign.setOpacity(0.6);
            getChildren().setAll(ledBorder, ledCentre, highlight, sign);
            highlight.setMouseTransparent(true);
            sign.setMouseTransparent(true);
            if (plus == null)
                sign.setVisible(false);
        }

        final void setOnAction(EventHandler<ActionEvent> actionHandler) {
            ledCentre.setOnMouseClicked(e -> actionHandler.handle(new ActionEvent(this, this)));
        }

        @Override public void layoutChildren() {
            double width = getWidth();
            double height = getHeight();
            double radius = Math.min(width, height) / 2;
            ledBorder.setRadius(radius);
            ledCentre.setRadius(0.8 * radius);
            highlight.setRadius(0.7 * radius);
            innerShadow.setRadius(0.8 * 0.075 / 0.15 * radius);
            Paint highlightFill = new RadialGradient(0, 0, 0 - highlight.getRadius(), 0 - highlight.getRadius(), highlight.getRadius(), false,
                    CycleMethod.NO_CYCLE, new Stop(0.0, Color.WHITE), new Stop(1.0, Color.TRANSPARENT));
            highlight.setFill(highlightFill);
            double lineLength = 0.4 * radius;
            hLine.setStartX(width / 2 - lineLength);
            hLine.setEndX(width / 2 + lineLength);
            hLine.setStrokeWidth(0.2 * radius);
            vLine.setStartY(width / 2 - lineLength);
            vLine.setEndY(width / 2 + lineLength);
            vLine.setStrokeWidth(0.2 * radius);
            for (Node child : getChildren())
                if (!(child instanceof Group))
                    layoutInArea(child, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
            layoutInArea(hLine, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
            layoutInArea(vLine, 0, 0, width, height, 0 , HPos.CENTER, VPos.CENTER);
        }

        static LedButton create(Color ledColor, Boolean plus, Runnable actionHandler) {
            LedButton ledButton = new LedButton(ledColor, plus);
            ledButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            ledButton.setOnAction(e -> actionHandler.run());
            return ledButton;
        }
    }
}
