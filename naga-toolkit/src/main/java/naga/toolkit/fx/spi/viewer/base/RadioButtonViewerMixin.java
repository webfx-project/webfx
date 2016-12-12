package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.RadioButtonViewer;

/**
 * @author Bruno Salmon
 */
public interface RadioButtonViewerMixin
        extends ToggleButtonViewerMixin<RadioButton, RadioButtonViewerBase, RadioButtonViewerMixin>,
        RadioButtonViewer {
}
