package naga.providers.toolkit.jfoenix;

import com.jfoenix.controls.JFXCheckBox;
import naga.providers.toolkit.javafx.fx.viewer.FxCheckBoxViewer;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

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
