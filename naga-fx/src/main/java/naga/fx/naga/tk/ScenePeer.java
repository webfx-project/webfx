package naga.fx.naga.tk;

import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.NodeViewerFactory;
import naga.fx.sun.tk.TKScene;

/**
 * @author Bruno Salmon
 */
public interface ScenePeer extends TKScene {

    Scene getScene();

    NodeViewerFactory getNodeViewerFactory();

    void updateParentAndChildrenViewers(Parent parent);

    default void onRootBound() {}

    default void onNodeViewerCreated(NodeViewer<Node> nodeViewer) {}

    default void onPropertyHit() {}

    default void onBeforePulse() {}

    default void onAfterPulse() {}
}
