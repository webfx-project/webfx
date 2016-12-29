package naga.fx.spi.viewer.base;

import naga.fx.scene.layout.Background;
import naga.fx.scene.layout.Border;
import naga.fx.scene.layout.Region;

/**
 * @author Bruno Salmon
 */
public interface RegionViewerMixin
        <N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends NodeViewerMixin<N, NB, NM> {

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateBackground(Background background);

    void updateBorder(Border border);
}
