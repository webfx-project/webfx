package naga.providers.toolkit.swing.fx.viewer;

import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerBase;
import naga.toolkit.fx.spi.viewer.base.ButtonBaseViewerMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
class SwingButtonBaseViewer
        <N extends ButtonBase, NV extends ButtonBaseViewerBase<N, NV, NM>, NM extends ButtonBaseViewerMixin<N, NV, NM>>
        extends SwingRegionViewer<N, NV, NM>
        implements ButtonBaseViewerMixin<N, NV, NM>, SwingEmbedComponentViewer<N>, SwingLayoutMeasurable<N> {

    private final AbstractButton swingButtonBase;

    SwingButtonBaseViewer(NV base, AbstractButton swingButtonBase) {
        super(base);
        this.swingButtonBase = swingButtonBase;
        swingButtonBase.setFont(StyleUtil.getFont(false, false));
        swingButtonBase.addActionListener(e -> {
            UiEventHandler<? super MouseEvent> onMouseClicked = getNode().getOnMouseClicked();
            if (onMouseClicked != null)
                onMouseClicked.handle(new SwingMouseEvent(null));
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

    @Override
    public void updateGraphic(Node graphic) {
        JComponent swingGraphic = toSwingComponent(graphic);
        if (swingGraphic instanceof JLabel)
            swingButtonBase.setIcon(((JLabel) swingGraphic).getIcon());
    }
}
