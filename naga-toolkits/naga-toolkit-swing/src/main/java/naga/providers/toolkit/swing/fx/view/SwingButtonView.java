package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.view.base.ButtonViewBase;
import naga.toolkit.fx.spi.view.base.ButtonViewMixin;

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
