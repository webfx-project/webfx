package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.HBox;
import naga.toolkit.drawing.spi.view.HBoxView;

/**
 * @author Bruno Salmon
 */
public interface HBoxViewMixin
        extends HBoxView,
        NodeViewMixin<HBox, HBoxViewBase, HBoxViewMixin> {

}
