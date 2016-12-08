package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.view.base.ButtonViewBase;
import naga.toolkit.fx.spi.view.base.ButtonViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxButtonView
        extends FxButtonBaseView<javafx.scene.control.Button, Button, ButtonViewBase, ButtonViewMixin>
        implements ButtonViewMixin, FxLayoutMeasurable {

    public FxButtonView() {
        super(new ButtonViewBase());
    }

    @Override
    javafx.scene.control.Button createFxNode() {
        return new javafx.scene.control.Button();
    }
}
