package naga.toolkit.providers.swing.nodes.layouts;


import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.providers.swing.nodes.SwingParent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingVBox extends SwingParent<Box> implements VBox<Box, Component> {

    public SwingVBox() {
        this(Box.createVerticalBox());
    }

    public SwingVBox(Box box) {
        super(box);
    }

    @Override
    protected Component prepareChildComponent(Component childComponent) {
        if (childComponent instanceof JComponent)
            ((JComponent) childComponent).setAlignmentX(Component.CENTER_ALIGNMENT);
        return childComponent;
    }
}
