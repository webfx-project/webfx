package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.viewer.EmbedGuiNodeViewer;

/**
 * @author Bruno Salmon
 */
public interface EmbedGuiNodeViewerMixin
        extends EmbedGuiNodeViewer,
        NodeViewerMixin<EmbedGuiNode, EmbedGuiNodeViewerBase, EmbedGuiNodeViewerMixin> {

}
