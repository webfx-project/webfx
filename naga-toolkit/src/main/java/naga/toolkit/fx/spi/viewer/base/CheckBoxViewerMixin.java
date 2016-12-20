package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxViewerMixin
        extends ButtonBaseViewerMixin<CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin> {

    void updateSelected(Boolean selected);
}
