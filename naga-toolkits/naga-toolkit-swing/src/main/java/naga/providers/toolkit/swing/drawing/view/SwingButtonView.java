package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.events.SwingMouseEvent;
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
        implements ButtonViewMixin, SwingEmbedComponentView<Button> {

    private JButton swingButton = new JButton();

    public SwingButtonView() {
        super(new ButtonViewBase());
    }

    @Override
    public void bind(Button button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public void updateOnMouseClicked(UiEventHandler<? super MouseEvent> onMouseClicked) {
        swingButton.setAction(onMouseClicked == null ? null : new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                onMouseClicked.handle(new SwingMouseEvent(null));
            }
        });
    }

    @Override
    public JComponent getEmbedSwingComponent() {
        return swingButton;
    }

    @Override
    public void updateText(String text) {
        swingButton.setText(text);
    }
}
