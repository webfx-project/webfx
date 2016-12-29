package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.control.CheckBox;
import naga.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCheckBoxViewer
        <FxN extends javafx.scene.control.CheckBox, N extends CheckBox, NB extends CheckBoxViewerBase<N, NB, NM>, NM extends CheckBoxViewerMixin<N, NB, NM>>

        extends FxButtonBaseViewer<FxN, N, NB, NM>
        implements CheckBoxViewerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxCheckBoxViewer() {
        super((NB) new CheckBoxViewerBase());
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
