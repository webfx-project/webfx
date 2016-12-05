package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.view.ButtonBaseView;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBaseView<N extends ButtonBase, FxN extends javafx.scene.control.ButtonBase> extends FxRegionView<N, FxN> implements ButtonBaseView<N> {

    @Override
    void setAndBindNodeProperties(N buttonBase, FxN fxButtonBase) {
        super.setAndBindNodeProperties(buttonBase, fxButtonBase);
        fxButtonBase.textProperty().bind(buttonBase.textProperty());
        //buttonBase.imageProperty().addListener((observable, oldValue, image) -> fxButtonBase.setGraphic((Node) Toolkit.unwrapToNativeNode(image)));
    }
}
