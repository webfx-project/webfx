package naga.providers.toolkit.swing.fx.view;

import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.toolkit.fx.scene.control.ButtonBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewBase;
import naga.toolkit.fx.spi.view.base.ButtonBaseViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
class SwingButtonBaseView
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>
        extends SwingRegionView<N, NV, NM>
        implements ButtonBaseViewMixin<N, NV, NM>, SwingEmbedComponentView<N>, SwingLayoutMeasurable {

    private final AbstractButton swingButtonBase;

    SwingButtonBaseView(NV base, AbstractButton swingButtonBase) {
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
    public JComponent getEmbedSwingComponent() {
        return swingButtonBase;
    }

    @Override
    public JComponent getSwingComponent() {
        return swingButtonBase;
    }

    protected void updateSize() {
        swingButtonBase.setSize(getNode().getWidth().intValue(), getNode().getHeight().intValue());
    }

    @Override
    public void updateText(String text) {
        swingButtonBase.setText(text);
    }
}
