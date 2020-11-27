package webfx.demo.enzoclocks.circlespacker;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class CirclesPackerPane extends Pane {

    private final CirclesPacker circlesPacker = new ResponsiveCirclesPacker();
    private boolean skipTimeline = true;
    private Timeline timeline;
    private final List<KeyValue> keyValues = new ArrayList<>();
    private final List<Node> recentlyAddedNodes = new ArrayList<>();
    private final List<Node> recentlyRemovedNodes = new ArrayList<>();
    private final List<Node> reintroducedNodes = new ArrayList<>();

    {
        recentlyAddedNodes.addAll(getChildren());
        getChildren().addListener((ListChangeListener<Node>) change -> {
                    while (change.next()) {
                        if (change.wasAdded())
                            recentlyAddedNodes.addAll(change.getAddedSubList());
                        if (change.wasRemoved())
                            recentlyRemovedNodes.addAll(change.getRemoved());
                    }
                    recentlyAddedNodes.removeAll(reintroducedNodes);
                    recentlyRemovedNodes.removeAll(reintroducedNodes);
                }
        );
    }

    public CirclesPackerPane() {
    }

    public CirclesPackerPane(Node... children) {
        super(children);
    }

    @Override
    protected void layoutChildren() {
        List<Node> children = getManagedChildren();
        int n = children.size();
        circlesPacker.setCirclesCount(n);
        circlesPacker.setContainerSize(getWidth(), getHeight());
        if (recentlyAddedNodes.isEmpty() && recentlyRemovedNodes.isEmpty() && !circlesPacker.hasChanged())
            return;
        double radius = circlesPacker.getCirclesRadius();
        for (int i = 0; i < n; i++) {
            Node node = children.get(i);
            if (node instanceof Circle)
                ((Circle) node).setRadius(radius);
            double x = circlesPacker.getCircleCenterX(i) - radius;
            double y = circlesPacker.getCircleCenterY(i) - radius;
            double diameter = 2 * radius;
            boolean recentlyAdded = recentlyAddedNodes.contains(node);
            if (skipTimeline || recentlyAdded)
                layoutInArea(node, x, y, diameter, diameter, 0, HPos.CENTER, VPos.CENTER);
            else {
                node.resize(diameter, diameter);
                keyValues.addAll(Arrays.asList(
                        new KeyValue(node.layoutXProperty(), x, Interpolator.EASE_OUT),
                        new KeyValue(node.layoutYProperty(), y, Interpolator.EASE_OUT)
                ));
            }

        }
        if (timeline != null) {
            timeline.stop();
            zoomNodes(
                getChildren().stream().filter(Node::isManaged).filter(node -> node.getScaleX() != 1).collect(Collectors.toList()),
                    false, false);
        }
        zoomNodes(recentlyAddedNodes, false, true);
        zoomNodes(recentlyRemovedNodes, true, true);
        if (!keyValues.isEmpty()) {
            timeline = new Timeline(new KeyFrame(Duration.millis(500), keyValues.stream().toArray(KeyValue[]::new)));
            timeline.setOnFinished(e -> {
                getChildren().removeAll(reintroducedNodes);
                reintroducedNodes.clear();
            });
            // Postponing the play so that the animation starts after all properties changes have been processed by WebFx
            Platform.runLater(timeline::play);
        }
        getChildren().addAll(recentlyRemovedNodes);
        reintroducedNodes.addAll(recentlyRemovedNodes);
        keyValues.clear();
        recentlyAddedNodes.clear();
        recentlyRemovedNodes.clear();
        skipTimeline = false;
    }

    private void zoomNodes(List<Node> nodes, boolean removed, boolean applyInitialScale) {
        // Added nodes will scale from 0 to 1 and removed nodes from 1 to 0
        double initialScale = removed ? 1 : 0, endScale = 1 - initialScale;
        nodes.forEach(node -> {
            if (removed) // Removed nodes are also excluded from the layout (circles packer algorithm)
                node.setManaged(false); // This is how to exclude them
            if (applyInitialScale) {
                node.setScaleX(initialScale);
                node.setScaleY(initialScale);
            }
            keyValues.addAll(Arrays.asList(
                    new KeyValue(node.scaleXProperty(), endScale, Interpolator.EASE_OUT),
                    new KeyValue(node.scaleYProperty(), endScale, Interpolator.EASE_OUT)
            ));
            // Removed nodes are also fading
            if (removed)
                keyValues.add(new KeyValue(node.opacityProperty(), 0, Interpolator.EASE_OUT));
        });
    }
}
