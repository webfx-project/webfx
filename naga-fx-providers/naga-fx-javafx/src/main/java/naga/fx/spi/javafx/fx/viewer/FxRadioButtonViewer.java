package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.control.RadioButton;
import naga.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRadioButtonViewer
        <FxN extends javafx.scene.control.RadioButton, N extends RadioButton, NB extends RadioButtonViewerBase<N, NB, NM>, NM extends RadioButtonViewerMixin<N, NB, NM>>

        extends FxButtonBaseViewer<FxN, N, NB, NM>
        implements RadioButtonViewerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxRadioButtonViewer() {
        super((NB) new RadioButtonViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.RadioButton checkBox = new javafx.scene.control.RadioButton();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
