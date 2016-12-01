package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.toolkit.drawing.scene.control.ButtonBase;
import naga.toolkit.drawing.spi.view.base.ButtonBaseViewBase;
import naga.toolkit.drawing.spi.view.base.ButtonBaseViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonBaseView
        <N extends ButtonBase, NV extends ButtonBaseViewBase<N, NV, NM>, NM extends ButtonBaseViewMixin<N, NV, NM>>
        extends SwingNodeView<N, NV, NM>
        implements ButtonBaseViewMixin<N, NV, NM>, SwingEmbedComponentView<N>, SwingLayoutMeasurable {

    private final AbstractButton swingButtonBase;

    public SwingButtonBaseView(NV base, AbstractButton swingButtonBase) {
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

    @Override
    public void updateText(String text) {
        swingButtonBase.setText(text);
    }
}
