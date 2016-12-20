package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingRadioButtonViewer
        <N extends RadioButton, NV extends RadioButtonViewerBase<N, NV, NM>, NM extends RadioButtonViewerMixin<N, NV, NM>>

        extends SwingButtonBaseViewer<N, NV, NM>
        implements RadioButtonViewerMixin<N, NV, NM> {

    private final JRadioButton swingRadioButton;

    public SwingRadioButtonViewer() {
        this((NV) new RadioButtonViewerBase(), new JRadioButton());
    }

    public SwingRadioButtonViewer(NV base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingRadioButton = (JRadioButton) getSwingComponent();
        swingRadioButton.addChangeListener(event -> getNode().setSelected(swingRadioButton.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingRadioButton.setSelected(selected);
    }
}
