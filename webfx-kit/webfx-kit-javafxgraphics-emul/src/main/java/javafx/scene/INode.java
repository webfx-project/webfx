package javafx.scene;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.transform.Transform;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;

import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.measurable.MeasurableMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface INode extends
    MeasurableMixin,
        HasParentProperty,
        HasManagedProperty,
        HasMouseTransparentProperty,
        HasOnMouseClickedProperty,
        HasLayoutXProperty,
        HasLayoutYProperty,
        HasVisibleProperty,
        HasOpacityProperty,
        HasClipProperty,
        HasBlendModeProperty,
        HasEffectProperty {

    ObservableList<Transform> getTransforms();

    List<Transform> getAllNodeTransforms();

    default void relocate(double x, double y) {
        Bounds layoutBounds = getLayoutBounds();
        setLayoutX(x - layoutBounds.getMinX());
        setLayoutY(y - layoutBounds.getMinY());
    }

    default boolean isResizable() {
        return false;
    }

    default Orientation getContentBias() {
        return null;
    }

    default void resize(double width, double height) {
    }

    default void resizeRelocate(double x, double y, double width, double height) {
        resize(width, height);
        relocate(x, y);
    }

    void autosize();

    /**
     * This is a special value that might be returned by {@link #getBaselineOffset()}.
     * This means that the Parent (layout Pane) of this Node should use the height of this Node as a baseline.
     */
    double BASELINE_OFFSET_SAME_AS_HEIGHT = Double.NEGATIVE_INFINITY;

    /**
     * The 'alphabetic' (or 'roman') baseline offset from the node's layoutBounds.minY location
     * that should be used when this node is being vertically aligned by baseline with
     * other nodes.  By default this returns {@link #BASELINE_OFFSET_SAME_AS_HEIGHT} for resizable Nodes
     * and layoutBounds height for non-resizable.  Subclasses
     * which contain text should override this method to return their actual text baseline offset.
     *
     * @return offset of text baseline from layoutBounds.minY for non-resizable Nodes or {@link #BASELINE_OFFSET_SAME_AS_HEIGHT} otherwise
     */
    default double getBaselineOffset() {
        if (isResizable())
            return BASELINE_OFFSET_SAME_AS_HEIGHT;
        return getLayoutBounds().getHeight();
    }

    /**
     * Returns an observable map of properties on this node for use primarily
     * by application developers.
     *
     * @return an observable map of properties on this node for use primarily
     * by application developers
     */
    ObservableMap<Object, Object> getProperties();

    /**
     * Tests if Node has properties.
     *
     * @return true if node has properties.
     */
    boolean hasProperties();

    Scene getScene();

    NodePeer getNodePeer();

    NodePeer getOrCreateAndBindNodePeer();

}
