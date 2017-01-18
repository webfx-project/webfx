package naga.fx.spi.swing.peer;

import emul.javafx.scene.Node;
import emul.javafx.scene.control.Label;
import naga.fx.spi.peer.base.LabelPeerBase;
import naga.fx.spi.peer.base.LabelPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingLabelPeer
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends SwingLabeledPeer<N, NB, NM>
        implements LabelPeerMixin<N, NB, NM>, SwingLayoutMeasurable<N> {

    private final JLabel jLabel = new JLabel();

    public SwingLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    SwingLabelPeer(NB base) {
        super(base);
    }

    @Override
    public JComponent getSwingComponent() {
        return jLabel;
    }

    @Override
    public void updateText(String text) {
        jLabel.setText(text);
    }

    @Override
    public void updateGraphic(Node graphic) {
    }
}
