package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingRadioButtonViewer
        extends SwingButtonBaseViewer<RadioButton, RadioButtonViewerBase, RadioButtonViewerMixin>
        implements RadioButtonViewerMixin {

    private final JRadioButton swingRadioButton;

    public SwingRadioButtonViewer() {
        super(new RadioButtonViewerBase(), new JRadioButton());
        swingRadioButton = (JRadioButton) getSwingComponent();
        swingRadioButton.addChangeListener(event -> getNode().setSelected(swingRadioButton.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingRadioButton.setSelected(selected);
    }
}
