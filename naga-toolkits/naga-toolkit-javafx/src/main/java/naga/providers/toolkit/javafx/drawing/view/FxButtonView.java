package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.Button;

/**
 * @author Bruno Salmon
 */
public class FxButtonView<N extends Button> extends FxNodeViewImpl<N, javafx.scene.control.Button> {

    @Override
    javafx.scene.control.Button createFxNode(N node) {
        return new javafx.scene.control.Button();
    }

    @Override
    void setAndBindNodeProperties(N button, javafx.scene.control.Button fxButton) {
        super.setAndBindNodeProperties(button, fxButton);
        fxButton.textProperty().bind(button.textProperty());
        //button.imageProperty().addListener((observable, oldValue, image) -> fxButton.setGraphic((Node) Toolkit.unwrapToNativeNode(image)));
    }
}
