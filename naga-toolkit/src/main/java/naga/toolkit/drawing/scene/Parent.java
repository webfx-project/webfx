package naga.toolkit.drawing.scene;

import javafx.collections.ObservableList;

/**
 * @author Bruno Salmon
 */
public interface Parent extends Node {

    ObservableList<Node> getChildren();

    /**
     * Requests a layout pass to be performed before the next scene is
     * rendered. This is batched up asynchronously to happen once per
     * "pulse", or frame of animation.
     * <p>
     * If this parent is either a layout root or unmanaged, then it will be
     * added directly to the scene's dirty layout list, otherwise requestParentLayout
     * will be invoked.
     */
    void requestLayout();

    /**
     * Executes a top-down layout pass on the scene graph under this parent.
     *
     * Calling this method while the Parent is doing layout is a no-op.
     */
    void layout();

}
