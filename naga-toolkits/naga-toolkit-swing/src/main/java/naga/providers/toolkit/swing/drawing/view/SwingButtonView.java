package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.scene.control.Button;
import naga.toolkit.drawing.spi.view.base.ButtonViewBase;
import naga.toolkit.drawing.spi.view.base.ButtonViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingButtonView
        extends SwingButtonBaseView<Button, ButtonViewBase, ButtonViewMixin>
        implements ButtonViewMixin {

    public SwingButtonView() {
        super(new ButtonViewBase(), new JButton());
    }
}
