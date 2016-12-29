package naga.fx.spi.swing.fx.viewer;

import naga.fx.scene.Node;
import naga.fx.scene.control.Label;
import naga.fx.spi.viewer.base.LabelViewerBase;
import naga.fx.spi.viewer.base.LabelViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingLabelViewer
        <N extends Label, NB extends LabelViewerBase<N, NB, NM>, NM extends LabelViewerMixin<N, NB, NM>>

        extends SwingLabeledViewer<N, NB, NM>
        implements LabelViewerMixin<N, NB, NM>, SwingLayoutMeasurable<N> {

    private final JLabel jLabel = new JLabel();

    public SwingLabelViewer() {
        this((NB) new LabelViewerBase());
    }

    SwingLabelViewer(NB base) {
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
