package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseView<N extends ButtonBase, FxN extends javafx.scene.control.ButtonBase> extends FxNodeViewImpl<N, FxN> {

    @Override
    void setAndBindNodeProperties(N buttonBase, FxN fxButtonBase) {
        super.setAndBindNodeProperties(buttonBase, fxButtonBase);
        fxButtonBase.textProperty().bind(buttonBase.textProperty());
        //buttonBase.imageProperty().addListener((observable, oldValue, image) -> fxButtonBase.setGraphic((Node) Toolkit.unwrapToNativeNode(image)));
    }
}
