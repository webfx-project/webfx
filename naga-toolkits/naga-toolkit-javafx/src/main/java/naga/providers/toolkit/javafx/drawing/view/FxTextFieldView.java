package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.TextField;
import naga.toolkit.drawing.spi.view.TextFieldView;

/**
 * @author Bruno Salmon
 */
public class FxTextFieldView extends FxTextInputControlView<TextField, javafx.scene.control.TextField> implements TextFieldView, FxLayoutMeasurable {

    @Override
    javafx.scene.control.TextField createFxNode(TextField node) {
        return new javafx.scene.control.TextField();
    }

}
