package naga.fx.spi.swing.peer;

import naga.fx.scene.control.RadioButton;
import naga.fx.spi.peer.base.RadioButtonPeerBase;
import naga.fx.spi.peer.base.RadioButtonPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingRadioButtonPeer
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends SwingButtonBasePeer<N, NB, NM>
        implements RadioButtonPeerMixin<N, NB, NM> {

    private final JRadioButton swingRadioButton;

    public SwingRadioButtonPeer() {
        this((NB) new RadioButtonPeerBase(), new JRadioButton());
    }

    public SwingRadioButtonPeer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingRadioButton = (JRadioButton) getSwingComponent();
        swingRadioButton.addChangeListener(event -> getNode().setSelected(swingRadioButton.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingRadioButton.setSelected(selected);
    }
}
