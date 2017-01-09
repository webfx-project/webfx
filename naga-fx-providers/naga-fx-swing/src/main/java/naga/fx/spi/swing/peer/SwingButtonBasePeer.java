package naga.fx.spi.swing.peer;

import naga.fx.scene.Node;
import naga.fx.scene.control.ButtonBase;
import naga.fx.spi.peer.base.ButtonBasePeerBase;
import naga.fx.spi.swing.util.StyleUtil;
import naga.fx.spi.peer.base.ButtonBasePeerMixin;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author Bruno Salmon
 */
class SwingButtonBasePeer
        <N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends SwingLabeledPeer<N, NB, NM>
        implements ButtonBasePeerMixin<N, NB, NM>, SwingEmbedComponentPeer<N>, SwingLayoutMeasurable<N> {

    private final AbstractButton swingButtonBase;

    SwingButtonBasePeer(NB base, AbstractButton swingButtonBase) {
        super(base);
        this.swingButtonBase = swingButtonBase;
        swingButtonBase.setFont(StyleUtil.getFont(false, false));
        swingButtonBase.addActionListener(e -> {
            N node = getNode();
            node.fireEvent(new naga.fx.event.ActionEvent(node, node));
        });
    }

    @Override
    public JComponent getSwingComponent() {
        return swingButtonBase;
    }

    @Override
    public void updateText(String text) {
        swingButtonBase.setText(text);
    }

    private JLabel graphicLabel;
    private PropertyChangeListener graphicLabelIconListener;

    @Override
    public void updateGraphic(Node graphic) {
        // Removing previous icon listener if any
        if (graphicLabel != null)
            graphicLabel.removePropertyChangeListener("icon", graphicLabelIconListener);
        // Getting the swing component associated with the graphic
        JComponent swingGraphic = toSwingComponent(graphic);
        // For now we accept only images coming from JLabel components
        graphicLabel = swingGraphic instanceof JLabel ? (JLabel) swingGraphic : null;
        if (graphicLabel != null) {
            // We set the swing button icon to that icon
            swingButtonBase.setIcon(graphicLabel.getIcon());
            // And keep it updated if that icon changes (this can happen if the graphic comes from the SwingImageViewPeer as the image loading is done in the background)
            graphicLabel.addPropertyChangeListener("icon", graphicLabelIconListener = evt -> swingButtonBase.setIcon(graphicLabel.getIcon()));
        }
    }
}
