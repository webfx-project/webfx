package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.scene.control.CheckBox;
import naga.toolkit.drawing.spi.view.base.CheckBoxViewBase;
import naga.toolkit.drawing.spi.view.base.CheckBoxViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBoxView
        extends SwingButtonBaseView<CheckBox, CheckBoxViewBase, CheckBoxViewMixin>
        implements CheckBoxViewMixin {

    private final JCheckBox swingCheckBox;

    public SwingCheckBoxView() {
        super(new CheckBoxViewBase(), new JCheckBox());
        swingCheckBox = (JCheckBox) getSwingComponent();
        swingCheckBox.addChangeListener(event -> getNode().setSelected(swingCheckBox.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingCheckBox.setSelected(selected);
    }
}
