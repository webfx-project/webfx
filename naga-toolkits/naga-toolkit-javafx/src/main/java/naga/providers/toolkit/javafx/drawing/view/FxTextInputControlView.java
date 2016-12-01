package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.TextInputControl;
import naga.toolkit.drawing.spi.view.TextInputControlView;

/**
 * @author Bruno Salmon
 */
abstract class FxTextInputControlView<N extends TextInputControl, FxN extends javafx.scene.control.TextInputControl> extends FxNodeViewImpl<N, FxN> implements TextInputControlView<N> {

    @Override
    void setAndBindNodeProperties(N textInputControl, FxN fxTextInputControl) {
        super.setAndBindNodeProperties(textInputControl, fxTextInputControl);
        fxTextInputControl.textProperty().bindBidirectional(textInputControl.textProperty());
        fxTextInputControl.promptTextProperty().bind(textInputControl.promptTextProperty());
        //fxTextInputControl.fontProperty().bind(new ConvertedProperty<>(textInputControl.fontProperty(), FxFonts::toFxFont));
    }
}
