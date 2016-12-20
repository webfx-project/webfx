package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingCheckBoxViewer
        <N extends CheckBox, NB extends CheckBoxViewerBase<N, NB, NM>, NM extends CheckBoxViewerMixin<N, NB, NM>>

        extends SwingButtonBaseViewer<N, NB, NM>
        implements CheckBoxViewerMixin<N, NB, NM> {

    private final JCheckBox swingCheckBox;

    public SwingCheckBoxViewer() {
        this((NB) new CheckBoxViewerBase(), new JCheckBox());
    }

    public SwingCheckBoxViewer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingCheckBox = (JCheckBox) getSwingComponent();
        swingCheckBox.addChangeListener(event -> getNode().setSelected(swingCheckBox.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingCheckBox.setSelected(selected);
    }
}
