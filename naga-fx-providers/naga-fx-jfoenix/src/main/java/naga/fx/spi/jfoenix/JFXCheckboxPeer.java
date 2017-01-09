package naga.fx.spi.jfoenix;

import com.jfoenix.controls.JFXCheckBox;
import naga.fx.spi.javafx.peer.FxCheckBoxPeer;
import naga.fx.scene.control.CheckBox;
import naga.fx.spi.peer.base.CheckBoxPeerMixin;
import naga.fx.spi.peer.base.CheckBoxPeerBase;

/**
 * @author Bruno Salmon
 */
public class JFXCheckboxPeer
        <FxN extends javafx.scene.control.CheckBox, N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends FxCheckBoxPeer<FxN, N, NB, NM> {

    @Override
    protected FxN createFxNode() {
        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> getNode().setSelected(newValue));
        return (FxN) checkBox;
    }
}
