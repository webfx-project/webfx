package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.CheckBoxViewer;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxViewerMixin
        extends CheckBoxViewer,
        ButtonBaseViewerMixin<CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin> {

    void updateSelected(Boolean selected);
}
