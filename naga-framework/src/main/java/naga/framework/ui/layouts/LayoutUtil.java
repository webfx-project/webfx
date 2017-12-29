package naga.framework.ui.layouts;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import naga.framework.ui.controls.BackgroundUtil;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.util.Numbers;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class LayoutUtil {

    public static GridPane createGoldLayout(Region child) {
        return createGoldLayout(child, 0, 0);
    }

    public static GridPane createGoldLayout(Region child, double percentageWidth, double percentageHeight) {
        return createGoldLayout(child, percentageWidth, percentageHeight, BackgroundUtil.newBackground(Color.gray(0.3, 0.5)));
    }

    public static GridPane createGoldLayout(Region child, double percentageWidth, double percentageHeight, Background background) {
        GridPane goldPane = new GridPane();
        goldPane.setAlignment(Pos.TOP_CENTER); // Horizontal alignment
        RowConstraints headerRowConstraints = new RowConstraints();
        headerRowConstraints.prefHeightProperty().bind(Properties.combine(goldPane.heightProperty(), child.heightProperty(),
                (gpHeight, cHeight) -> {
                    if (percentageHeight != 0)
                        child.setPrefHeight(gpHeight.doubleValue() * percentageHeight);
                    Platform.runLater(() -> goldPane.getRowConstraints().setAll(headerRowConstraints));
                    return (gpHeight.doubleValue() - cHeight.doubleValue()) / 2.61;
                }));
        if (percentageWidth != 0)
            child.prefWidthProperty().bind(Properties.compute(goldPane.widthProperty(), gpWidth -> gpWidth.doubleValue() * percentageWidth));
        goldPane.add(setMaxSizeToPref(child), 0, 1);
        if (background != null)
            goldPane.setBackground(background);
        return goldPane;
    }

    public static Region createHGrowable() {
        return setHGrowable(setMaxWidthToInfinite(new Region()));
    }

    public static <N extends Node> N setHGrowable(N node) {
        HBox.setHgrow(node, Priority.ALWAYS);
        return node;
    }

    public static <N extends Node> N setVGrowable(N node) {
        VBox.setVgrow(node, Priority.ALWAYS);
        return node;
    }

    public static <N extends Region> N setMinSizeToZero(N region) {
        return setMinSize(region, 0);
    }

    private static <N extends Region> N setMinSize(N region, double value) {
        region.setMinWidth(value);
        region.setMinHeight(value);
        return region;
    }

    public static <N extends Region> N setMinSizeToZeroAndPrefSizeToInfinite(N region) {
        return setMinSizeToZero(setMaxPrefSizeToInfinite(region));
    }

    public static <N extends Region> N setMaxPrefSizeToInfinite(N region) {
        return setPrefSize(setMaxSize(region, Double.MAX_VALUE), Double.MAX_VALUE);
    }

    public static <N extends Region> N setMaxPrefSize(N region, double value) {
        return setPrefSize(setMaxSize(region, value), value);
    }

    private static <N extends Region> N setPrefSize(N region, double value) {
        region.setPrefWidth(value);
        region.setPrefHeight(value);
        return region;
    }

    public static <N extends Region> N setMaxSizeToInfinite(N region) {
        return setMaxSize(region, Double.MAX_VALUE);
    }

    public static <N extends Region> N setMaxSizeToPref(N region) {
        return setMaxSize(region, USE_PREF_SIZE);
    }

    private static <N extends Region> N setMaxSize(N region, double value) {
        region.setMaxWidth(value);
        region.setMaxHeight(value);
        return region;
    }

    public static <N extends Region> N setPrefWidthToInfinite(N region) {
        region.setPrefWidth(Double.MAX_VALUE);
        return setMaxWidthToInfinite(region);
    }

    public static <N extends Region> N setPrefHeightToInfinite(N region) {
        region.setPrefHeight(Double.MAX_VALUE);
        return setMaxHeightToInfinite(region);
    }

    public static <N extends Region> N setMaxWidthToInfinite(N region) {
        region.setMaxWidth(Double.MAX_VALUE);
        return region;
    }

    public static <N extends Region> N setMaxHeightToInfinite(N region) {
        region.setMaxHeight(Double.MAX_VALUE);
        return region;
    }

    public static <N extends Region> N setMinMaxWidthToPref(N region) {
        setMinWidthToPref(region);
        setMaxWidthToPref(region);
        return region;
    }

    public static <N extends Region> N setMinWidthToPref(N region) {
        return setMinWidth(region, USE_PREF_SIZE);
    }

    public static <N extends Region> N setMaxWidthToPref(N region) {
        return setMaxWidth(region, USE_PREF_SIZE);
    }


    public static <N extends Region> N setMinWidth(N region, double value) {
        region.setMinWidth(value);
        return region;
    }

    public static <N extends Region> N setMaxWidth(N region, double value) {
        region.setMaxWidth(value);
        return region;
    }

    public static <N extends Region> N setMinMaxHeightToPref(N region) {
        setMinHeightToPref(region);
        setMaxHeightToPref(region);
        return region;
    }

    public static <N extends Region> N setMinHeightToPref(N region) {
        return setMinHeight(region, USE_PREF_SIZE);
    }

    public static <N extends Region> N setMaxHeightToPref(N region) {
        return setMaxHeight(region, USE_PREF_SIZE);
    }

    public static <N extends Region> N setMinHeight(N region, double value) {
        region.setMinHeight(value);
        return region;
    }

    public static <N extends Region> N setMaxHeight(N region, double value) {
        region.setMaxHeight(value);
        return region;
    }

    public static <N extends Node> N setUnmanagedWhenInvisible(N node) {
        node.managedProperty().bind(node.visibleProperty());
        return node;
    }

    public static <N extends Node> N setUnmanagedWhenInvisible(N node, ObservableValue<Boolean> visibleProperty) {
        node.visibleProperty().bind(visibleProperty);
        return setUnmanagedWhenInvisible(node);
    }

    public static <N extends Region> N createPadding(N content) {
        content.setPadding(new Insets(10));
        return content;
    }

    public static ScrollPane createVerticalScrollPaneWithPadding(Region content) {
        return createVerticalScrollPane(createPadding(content));
    }

    public static ScrollPane createVerticalScrollPane(Region content) {
        ScrollPane scrollPane = createScrollPane(content);
        setMinMaxWidthToPref(content);
        double verticalScrollbarExtraWidth = Toolkit.get().getVerticalScrollbarExtraWidth();
        if (verticalScrollbarExtraWidth == 0)
            content.prefWidthProperty().bind(scrollPane.widthProperty());
        else
            content.prefWidthProperty().bind(
                    // scrollPane.widthProperty().subtract(verticalScrollbarExtraWidth) // doesn't compile with GWT
                    Properties.compute(scrollPane.widthProperty(), width -> Numbers.toDouble(width.doubleValue() - verticalScrollbarExtraWidth))
            );
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    public static ScrollPane createScrollPane(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        content.getProperties().put("parentScrollPane", scrollPane); // Used by findScrollPaneAncestor()
        return scrollPane;
    }

    public static ScrollPane findScrollPaneAncestor(Node node) {
        while (true) {
            if (node == null)
                return null;
            // Assuming ScrollPane has been created using createScrollPane() which stores the scrollPane into "parentScrollPane" node property
            ScrollPane parentScrollPane = (ScrollPane) node.getProperties().get("parentScrollPane");
            if (parentScrollPane != null)
                return parentScrollPane;
            node = node.getParent();
            if (node instanceof ScrollPane)
                return (ScrollPane) node;
        }
    }

/*
    public static double computeScrollPaneHoffset(ScrollPane scrollPane) {
        double hmin = scrollPane.getHmin();
        double hmax = scrollPane.getHmax();
        double hvalue = scrollPane.getHvalue();
        double contentWidth = scrollPane.getLayoutBounds().getWidth();
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double hoffset = Math.max(0, contentWidth - viewportWidth) * (hvalue - hmin) / (hmax - hmin);
        return hoffset;
    }

    public static double computeScrollPaneVoffset(ScrollPane scrollPane) {
        double vmin = scrollPane.getVmin();
        double vmax = scrollPane.getVmax();
        double vvalue = scrollPane.getVvalue();
        double contentHeight = scrollPane.getLayoutBounds().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        double voffset = Math.max(0, contentHeight - viewportHeight) * (vvalue - vmin) / (vmax - vmin);
        return voffset;
    }
*/

    public static boolean isNodeVerticallyVisibleOnScene(Node node) {
        Bounds layoutBounds = node.getLayoutBounds();
        double minY = node.localToScene(0, layoutBounds.getMinY()).getY();
        double maxY = node.localToScene(0, layoutBounds.getMaxY()).getY();
        Scene scene = node.getScene();
        return minY >= 0 && maxY <= scene.getHeight();
    }

    public static boolean scrollNodeToBeVerticallyVisibleOnScene(Node node) {
        return scrollNodeToBeVerticallyVisibleOnScene(node, true);
    }

    public static boolean scrollNodeToBeVerticallyVisibleOnScene(Node node, boolean animate) {
        ScrollPane scrollPane = findScrollPaneAncestor(node);
        if (scrollPane != null) {
            double vValue = 1.0; // TODO: compute value in dependence of the current node position
            animateProperty(scrollPane.vvalueProperty(), vValue, animate);
            return true;
        }
        return false;
    }

    private static <T> void animateProperty(WritableValue<T> target, T finalValue, boolean animate) {
        if (!animate)
            target.setValue(finalValue);
        else {
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(1), new KeyValue(target, finalValue, Interpolator.EASE_OUT)));
            timeline.play();
        }
    }

    public static void autoFocusIfEnabled(Node node) {
        if (isAutoFocusEnabled())
            node.requestFocus();
    }

    public static boolean isAutoFocusEnabled() {
        // TODO: make it a user setting that can be stored in the device
        // Default behaviour is to disable auto focus if this can cause a (probably unwanted) virtual keyboard to appear
        return !willAVirtualKeyboardAppearOnFocus();
    }

    public static boolean willAVirtualKeyboardAppearOnFocus() {
        // No API for this so temporary implementation based on screen width size
        Rectangle2D visualBounds = Toolkit.get().getPrimaryScreen().getVisualBounds();
        return Math.min(visualBounds.getWidth(), visualBounds.getHeight()) < 800;
    }
}
