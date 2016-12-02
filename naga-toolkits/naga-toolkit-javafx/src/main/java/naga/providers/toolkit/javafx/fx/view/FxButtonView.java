package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.view.ButtonView;

/**
 * @author Bruno Salmon
 */
public class FxButtonView extends FxButtonBaseView<Button, javafx.scene.control.Button> implements ButtonView, FxLayoutMeasurable {

    @Override
    javafx.scene.control.Button createFxNode(Button node) {
        return new javafx.scene.control.Button();
    }

}
