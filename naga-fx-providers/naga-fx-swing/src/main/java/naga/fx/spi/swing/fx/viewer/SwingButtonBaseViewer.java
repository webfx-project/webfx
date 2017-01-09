package naga.fx.spi.swing.fx.viewer;

import naga.fx.scene.Node;
import naga.fx.scene.control.ButtonBase;
import naga.fx.spi.swing.util.StyleUtil;
import naga.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.fx.spi.viewer.base.ButtonBaseViewerMixin;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * @author Bruno Salmon
 */
class SwingButtonBaseViewer
        <N extends ButtonBase, NB extends ButtonBaseViewerBase<N, NB, NM>, NM extends ButtonBaseViewerMixin<N, NB, NM>>

        extends SwingLabeledViewer<N, NB, NM>
        implements ButtonBaseViewerMixin<N, NB, NM>, SwingEmbedComponentViewer<N>, SwingLayoutMeasurable<N> {

    private final AbstractButton swingButtonBase;

    SwingButtonBaseViewer(NB base, AbstractButton swingButtonBase) {
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
            // And keep it updated if that icon changes (this can happen if the graphic comes from the SwingImageViewViewer as the image loading is done in the background)
            graphicLabel.addPropertyChangeListener("icon", graphicLabelIconListener = evt -> swingButtonBase.setIcon(graphicLabel.getIcon()));
        }
    }
}
