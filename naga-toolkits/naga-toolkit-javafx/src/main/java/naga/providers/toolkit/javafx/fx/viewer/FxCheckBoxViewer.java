package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxViewer
        <FxN extends javafx.scene.control.CheckBox, N extends CheckBox, NV extends CheckBoxViewerBase<N, NV, NM>, NM extends CheckBoxViewerMixin<N, NV, NM>>

        extends FxButtonBaseViewer<FxN, N, NV, NM>
        implements CheckBoxViewerMixin<N, NV, NM>, FxLayoutMeasurable {

    public FxCheckBoxViewer() {
        super((NV) new CheckBoxViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }

    @Override
    public void updateSelected(Boolean selected) {
        getFxNode().setSelected(selected);
    }
}
