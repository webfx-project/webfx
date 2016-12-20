package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public interface CheckBoxViewerMixin
        <N extends CheckBox, NV extends CheckBoxViewerBase<N, NV, NM>, NM extends CheckBoxViewerMixin<N, NV, NM>>

        extends ButtonBaseViewerMixin<N, NV, NM> {

    void updateSelected(Boolean selected);
}
