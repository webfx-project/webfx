package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public interface RegionViewerMixin
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>
        extends NodeViewerMixin<N, NV, NM> {

    void updateWidth(Double width);

    void updateHeight(Double height);
}
