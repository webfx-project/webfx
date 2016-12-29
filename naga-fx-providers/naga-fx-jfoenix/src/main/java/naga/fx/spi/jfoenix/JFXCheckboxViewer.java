package naga.fx.spi.jfoenix;

import com.jfoenix.controls.JFXCheckBox;
import naga.fx.spi.javafx.fx.viewer.FxCheckBoxViewer;
import naga.fx.scene.control.CheckBox;
import naga.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class JFXCheckboxViewer
        <FxN extends javafx.scene.control.CheckBox, N extends CheckBox, NB extends CheckBoxViewerBase<N, NB, NM>, NM extends CheckBoxViewerMixin<N, NB, NM>>

        extends FxCheckBoxViewer<FxN, N, NB, NM> {

    @Override
    protected FxN createFxNode() {
        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }
}
