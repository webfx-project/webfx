package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.view.ButtonView;

/**
 * @author Bruno Salmon
 */
public interface ButtonViewMixin
        extends ButtonView,
        ButtonBaseViewMixin<Button, ButtonViewBase, ButtonViewMixin> {
}
