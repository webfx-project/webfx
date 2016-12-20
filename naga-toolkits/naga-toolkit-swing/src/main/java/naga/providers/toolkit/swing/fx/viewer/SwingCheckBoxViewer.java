package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBoxViewer
        <N extends CheckBox, NV extends CheckBoxViewerBase<N, NV, NM>, NM extends CheckBoxViewerMixin<N, NV, NM>>

        extends SwingButtonBaseViewer<N, NV, NM>
        implements CheckBoxViewerMixin<N, NV, NM> {

    private final JCheckBox swingCheckBox;

    public SwingCheckBoxViewer() {
        this((NV) new CheckBoxViewerBase(), new JCheckBox());
    }

    public SwingCheckBoxViewer(NV base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingCheckBox = (JCheckBox) getSwingComponent();
        swingCheckBox.addChangeListener(event -> getNode().setSelected(swingCheckBox.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingCheckBox.setSelected(selected);
    }
}
