package naga.providers.toolkit.javafx.fx.viewer;

import javafx.scene.Node;
import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerBase;
import naga.toolkit.fx.spi.viewer.base.EmbedGuiNodeViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxEmbedGuiNodeViewer

        extends FxNodeViewer<Node, EmbedGuiNode, EmbedGuiNodeViewerBase, EmbedGuiNodeViewerMixin>
        implements EmbedGuiNodeViewerMixin {

    public FxEmbedGuiNodeViewer() {
        super(new EmbedGuiNodeViewerBase());
    }

    @Override
    Node createFxNode() {
        return getNode().getGuiNode().unwrapToNativeNode();
    }
}
