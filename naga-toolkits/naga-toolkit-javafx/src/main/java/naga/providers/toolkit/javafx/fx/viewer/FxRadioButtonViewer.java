package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRadioButtonViewer
        extends FxButtonBaseViewer<javafx.scene.control.RadioButton, RadioButton, RadioButtonViewerBase, RadioButtonViewerMixin>
        implements RadioButtonViewerMixin, FxLayoutMeasurable {

    public FxRadioButtonViewer() {
        super(new RadioButtonViewerBase());
    }

    @Override
    javafx.scene.control.RadioButton createFxNode() {
        javafx.scene.control.RadioButton checkBox = new javafx.scene.control.RadioButton();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
