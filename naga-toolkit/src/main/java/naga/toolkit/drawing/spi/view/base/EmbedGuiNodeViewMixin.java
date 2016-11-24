package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.EmbedGuiNode;
import naga.toolkit.drawing.spi.view.EmbedGuiNodeView;

/**
 * @author Bruno Salmon
 */
public interface EmbedGuiNodeViewMixin
        extends EmbedGuiNodeView,
        NodeViewMixin<EmbedGuiNode, EmbedGuiNodeViewBase, EmbedGuiNodeViewMixin> {

}
