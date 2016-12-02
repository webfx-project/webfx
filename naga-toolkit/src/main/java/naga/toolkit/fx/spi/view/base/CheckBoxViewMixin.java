package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.view.CheckBoxView;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxViewMixin
        extends CheckBoxView,
        ButtonBaseViewMixin<CheckBox, CheckBoxViewBase, CheckBoxViewMixin> {

    void updateSelected(Boolean selected);
}
