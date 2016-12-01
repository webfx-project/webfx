package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class FxButtonView extends FxButtonBaseView<Button, javafx.scene.control.Button> implements FxLayoutMeasurable {

    @Override
    javafx.scene.control.Button createFxNode(Button node) {
        return new javafx.scene.control.Button();
    }

}
