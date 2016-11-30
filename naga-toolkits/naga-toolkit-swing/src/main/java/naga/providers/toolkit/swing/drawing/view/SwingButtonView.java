package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.events.SwingMouseEvent;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.toolkit.drawing.scene.control.Button;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.base.ButtonViewBase;
import naga.toolkit.drawing.spi.view.base.ButtonViewMixin;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonView
        extends SwingNodeView<Button, ButtonViewBase, ButtonViewMixin>
        implements ButtonViewMixin, SwingEmbedComponentView<Button>, SwingLayoutMeasurable {

    private JButton swingButton = new JButton();

    public SwingButtonView() {
        super(new ButtonViewBase());
        swingButton.setFont(StyleUtil.getFont(false, false));
        swingButton.addActionListener(e -> {
            UiEventHandler<? super MouseEvent> onMouseClicked = getNode().getOnMouseClicked();
            if (onMouseClicked != null)
                onMouseClicked.handle(new SwingMouseEvent(null));
        });
    }

    @Override
    public void bind(Button button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
    }

    @Override
    public JComponent getEmbedSwingComponent() {
        return swingButton;
    }

    @Override
    public JComponent getSwingComponent() {
        return swingButton;
    }

    @Override
    public void updateText(String text) {
        swingButton.setText(text);
    }
}
