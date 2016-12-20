package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRadioButtonViewer
        <FxN extends javafx.scene.control.RadioButton, N extends RadioButton, NV extends RadioButtonViewerBase<N, NV, NM>, NM extends RadioButtonViewerMixin<N, NV, NM>>

        extends FxButtonBaseViewer<FxN, N, NV, NM>
        implements RadioButtonViewerMixin<N, NV, NM>, FxLayoutMeasurable {

    public FxRadioButtonViewer() {
        super((NV) new RadioButtonViewerBase());
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
