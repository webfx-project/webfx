package naga.toolkit.providers.swing.nodes.layouts;


import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.providers.swing.nodes.SwingParent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingHBox extends SwingParent<Box> implements HBox<Box, Component> {

    public SwingHBox() {
        this(Box.createHorizontalBox());
    }

    public SwingHBox(Box box) {
        super(box);
    }

    @Override
    protected Component prepareChildComponent(Component childComponent) {
        if (childComponent instanceof JComponent)
            ((JComponent) childComponent).setAlignmentX(Component.CENTER_ALIGNMENT);
        return childComponent;
    }
}
