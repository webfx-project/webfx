package webfx.framework.client.ui.util.layout;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.Numbers;

import java.util.function.Predicate;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public final class LayoutUtil {

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
        // Making the gold pane invisible during a few animation frames because its height may not be stable on start
        goldPane.setVisible(false);
        UiScheduler.scheduleInAnimationFrame(() -> goldPane.setVisible(true), 5, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
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
        // For any strange reason this additional code is required for the gwt version to work properly (unnecessary for the JavaFx version)
        goldPane.widthProperty().addListener(observable -> goldPane.requestLayout());
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
        return setMaxPrefSize(region, Double.MAX_VALUE);
    }

    public static <N extends Region> N setMaxPrefSize(N region, double value) {
        return setPrefSize(setMaxSize(region, value), value);
    }

    public static <N extends Region> N setPrefSize(N region, double value) {
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

    public static <N extends Region> N setPadding(N content, double top, double right, double bottom, double left) {
        content.setPadding(new Insets(top, right, bottom, left));
        return content;
    }

    public static <N extends Region> N setPadding(N content, double topBottom, double rightLeft) {
        return setPadding(content, topBottom, rightLeft, topBottom, rightLeft);
    }

    public static <N extends Region> N setPadding(N content, double topRightBottomLeft) {
        return setPadding(content, new Insets(topRightBottomLeft));
    }

    public static <N extends Region> N setPadding(N content, Insets padding) {
        content.setPadding(padding);
        return content;
    }

    public static <N extends Region> N createPadding(N content) {
        return setPadding(content, new Insets(10));
    }

    public static <N extends Region> N removePadding(N content) {
        return setPadding(content, Insets.EMPTY);
    }

    // lookup method

    public static Node lookupChild(Node node, Predicate<Node> predicate) {
        if (node != null) {
            if (predicate.test(node))
                return node;
            if (node instanceof Parent) {
                ObservableList<Node> children = node instanceof SplitPane ? ((SplitPane) node).getItems() : ((Parent) node).getChildrenUnmodifiable();
                for (Node child : children) {
                    Node n = lookupChild(child, predicate);
                    if (n != null)
                        return n;
                }
            }
        }
        return null;
    }

    // ScrollPane utility methods

    public static ScrollPane createVerticalScrollPaneWithPadding(Region content) {
        return createVerticalScrollPane(createPadding(content));
    }

    public static ScrollPane createVerticalScrollPane(Region content) {
        ScrollPane scrollPane = createScrollPane(setMinMaxWidthToPref(content));
        double verticalScrollbarExtraWidth = WebFxKitLauncher.getVerticalScrollbarExtraWidth();
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

    public static double computeScrollPaneHoffset(ScrollPane scrollPane) {
        double hmin = scrollPane.getHmin();
        double hmax = scrollPane.getHmax();
        double hvalue = scrollPane.getHvalue();
        double contentWidth = scrollPane.getContent().getLayoutBounds().getWidth();
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double hoffset = Math.max(0, contentWidth - viewportWidth) * (hvalue - hmin) / (hmax - hmin);
        return hoffset;
    }

    public static double computeScrollPaneVoffset(ScrollPane scrollPane) {
        double vmin = scrollPane.getVmin();
        double vmax = scrollPane.getVmax();
        double vvalue = scrollPane.getVvalue();
        double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
        double viewportHeight = scrollPane.getViewportBounds().getHeight();
        double voffset = Math.max(0, contentHeight - viewportHeight) * (vvalue - vmin) / (vmax - vmin);
        return voffset;
    }

    // Snap methods from Region (but public)

    /**
     * If snapToPixel is true, then the value is ceil'd using Math.ceil. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or ceil'd based on snapToPixel
     */
    private static double snapSize(double value, boolean snapToPixel) {
        return snapToPixel ? Math.ceil(value) : value;
    }

    /**
     * Returns a value ceiled to the nearest pixel.
     * @param value the size value to be snapped
     * @return value ceiled to nearest pixel
     */
    public static double snapSize(double value) {
        return snapSize(value, true);
    }

    /**
     * If snapToPixel is true, then the value is rounded using Math.round. Otherwise,
     * the value is simply returned.
     *
     * @param value The value that needs to be snapped
     * @param snapToPixel Whether to snap to pixel
     * @return value either as passed in or rounded based on snapToPixel
     */
    public static double snapPosition(double value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    /**
     * Returns a value rounded to the nearest pixel.
     * @param value the position value to be snapped
     * @return value rounded to nearest pixel
     */
    public static double snapPosition(double value) {
        return snapPosition(value, true);
    }

    // used for layout to adjust widths to honor the min/max policies consistently
    public static double boundedSize(double value, double min, double max) {
        // if max < value, return max
        // if min > value, return min
        // if min > max, return min
        return Math.min(Math.max(value, min), Math.max(min,max));
    }
}
