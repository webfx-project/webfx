package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.scene.control.CheckBox;
import naga.toolkit.drawing.spi.view.CheckBoxView;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxViewMixin
        extends CheckBoxView,
        ButtonBaseViewMixin<CheckBox, CheckBoxViewBase, CheckBoxViewMixin> {

    void updateSelected(Boolean selected);
}
