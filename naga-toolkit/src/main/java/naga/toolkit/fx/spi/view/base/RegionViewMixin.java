package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public interface RegionViewMixin
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>
        extends NodeViewMixin<N, NV, NM> {

    void updateWidth(Double width);

    void updateHeight(Double height);
}
