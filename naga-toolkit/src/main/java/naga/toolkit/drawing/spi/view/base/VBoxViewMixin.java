package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.VBox;
import naga.toolkit.drawing.spi.view.VBoxView;

/**
 * @author Bruno Salmon
 */
public interface VBoxViewMixin
        extends VBoxView,
        NodeViewMixin<VBox, VBoxViewBase, VBoxViewMixin> {

}
