package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingRadioButtonViewer
        <N extends RadioButton, NB extends RadioButtonViewerBase<N, NB, NM>, NM extends RadioButtonViewerMixin<N, NB, NM>>

        extends SwingButtonBaseViewer<N, NB, NM>
        implements RadioButtonViewerMixin<N, NB, NM> {

    private final JRadioButton swingRadioButton;

    public SwingRadioButtonViewer() {
        this((NB) new RadioButtonViewerBase(), new JRadioButton());
    }

    public SwingRadioButtonViewer(NB base, AbstractButton swingButtonBase) {
        super(base, swingButtonBase);
        swingRadioButton = (JRadioButton) getSwingComponent();
        swingRadioButton.addChangeListener(event -> getNode().setSelected(swingRadioButton.isSelected()));
    }

    @Override
    public void updateSelected(Boolean selected) {
        swingRadioButton.setSelected(selected);
    }
}
