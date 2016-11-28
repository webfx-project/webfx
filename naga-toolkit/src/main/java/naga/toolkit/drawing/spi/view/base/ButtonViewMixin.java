package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.Button;
import naga.toolkit.drawing.spi.view.ButtonView;

/**
 * @author Bruno Salmon
 */
public interface ButtonViewMixin
        extends ButtonView,
        ButtonBaseViewMixin<Button, ButtonViewBase, ButtonViewMixin> {
}
