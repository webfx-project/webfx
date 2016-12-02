package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.EmbedGuiNode;
import naga.toolkit.fx.spi.view.EmbedGuiNodeView;

/**
 * @author Bruno Salmon
 */
public interface EmbedGuiNodeViewMixin
        extends EmbedGuiNodeView,
        NodeViewMixin<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin> {

}
