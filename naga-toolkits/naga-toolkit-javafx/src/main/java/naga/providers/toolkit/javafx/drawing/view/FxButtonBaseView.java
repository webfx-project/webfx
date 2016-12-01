package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.ButtonBase;
import naga.toolkit.drawing.spi.view.ButtonBaseView;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseView<N extends ButtonBase, FxN extends javafx.scene.control.ButtonBase> extends FxNodeViewImpl<N, FxN> implements ButtonBaseView<N> {

    @Override
    void setAndBindNodeProperties(N buttonBase, FxN fxButtonBase) {
        super.setAndBindNodeProperties(buttonBase, fxButtonBase);
        fxButtonBase.textProperty().bind(buttonBase.textProperty());
        //buttonBase.imageProperty().addListener((observable, oldValue, image) -> fxButtonBase.setGraphic((Node) Toolkit.unwrapToNativeNode(image)));
    }
}
