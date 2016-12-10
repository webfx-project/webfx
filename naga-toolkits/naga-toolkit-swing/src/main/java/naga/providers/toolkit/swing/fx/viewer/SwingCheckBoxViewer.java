package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBoxViewer
        extends SwingButtonBaseViewer<CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin>
        implements CheckBoxViewerMixin {

    private final JCheckBox swingCheckBox;

    public SwingCheckBoxViewer() {
        super(new CheckBoxViewerBase(), new JCheckBox());
        swingCheckBox = (JCheckBox) getSwingComponent();
        swingCheckBox.addChangeListener(event -> getNode().setSelected(swingCheckBox.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingCheckBox.setSelected(selected);
    }
}
