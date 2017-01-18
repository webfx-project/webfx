package naga.fx.spi.swing.peer;

import emul.javafx.scene.control.CheckBox;
import naga.fx.spi.peer.base.CheckBoxPeerBase;
import naga.fx.spi.peer.base.CheckBoxPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBoxPeer
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>

        extends SwingButtonBasePeer<N, NB, NM>
        implements CheckBoxPeerMixin<N, NB, NM> {

    private final JCheckBox swingCheckBox;

    public SwingCheckBoxPeer() {
        this((NB) new CheckBoxPeerBase(), new JCheckBox());
    }

    public SwingCheckBoxPeer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingCheckBox = (JCheckBox) getSwingComponent();
        swingCheckBox.addChangeListener(event -> getNode().setSelected(swingCheckBox.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingCheckBox.setSelected(selected);
    }
}
