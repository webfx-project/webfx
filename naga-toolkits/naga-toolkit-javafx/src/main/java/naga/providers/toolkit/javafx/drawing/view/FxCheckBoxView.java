package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxView extends FxButtonBaseView<CheckBox, javafx.scene.control.CheckBox> implements FxLayoutMeasurable {

    @Override
    javafx.scene.control.CheckBox createFxNode(CheckBox node) {
        return new javafx.scene.control.CheckBox();
    }

    @Override
    void setAndBindNodeProperties(CheckBox checkBox, javafx.scene.control.CheckBox fxCheckBox) {
        super.setAndBindNodeProperties(checkBox, fxCheckBox);
        fxCheckBox.selectedProperty().bindBidirectional(checkBox.selectedProperty());
    }
}
