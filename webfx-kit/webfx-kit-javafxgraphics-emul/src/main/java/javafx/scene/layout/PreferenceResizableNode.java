package javafx.scene.layout;

import javafx.scene.INode;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;

/**
 * @author Bruno Salmon
 */
public interface PreferenceResizableNode extends INode,
        HasReadOnlyWidthProperty,
        HasMinWidthProperty,
        HasPrefWidthProperty,
        HasMaxWidthProperty,
        HasReadOnlyHeightProperty,
        HasMinHeightProperty,
        HasPrefHeightProperty,
        HasMaxHeightProperty {

    default boolean isResizable() {
        return true;
    }

    /**
     * Sentinel value which can be passed to a region's
     * {@link #setMinWidth(Double) setMinWidth},
     * {@link #setMinHeight(Double) setMinHeight},
     * {@link #setMaxWidth(Double) setMaxWidth} or
     * {@link #setMaxHeight(Double) setMaxHeight}
     * methods to indicate that the preferred dimension should be used for that max and/or min constraint.
     */
    double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;

    /**
     * Sentinel value which can be passed to a region's
     * {@link #setMinWidth(Double) setMinWidth},
     * {@link #setMinHeight(Double) setMinHeight},
     * {@link #setPrefWidth(Double) setPrefWidth},
     * {@link #setPrefHeight(Double) setPrefHeight},
     * {@link #setMaxWidth(Double) setMaxWidth},
     * {@link #setMaxHeight(Double) setMaxHeight} methods
     * to reset the region's size constraint back to it's intrinsic size returned
     * by {@link #computeMinWidth(Double) computeMinWidth}, {@link #computeMinHeight(double) computeMinHeight},
     * {@link #computePrefWidth(double) computePrefWidth}, {@link #computePrefHeight(double) computePrefHeight},
     * {@link #computeMaxWidth(double) computeMaxWidth}, or {@link #computeMaxHeight(double) computeMaxHeight}.
     */
    double USE_COMPUTED_SIZE = -1;

}
