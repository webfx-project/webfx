package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLayoutViewer
        <N extends Region, NV extends RegionViewerBase<N, NV, NM>, NM extends RegionViewerMixin<N, NV, NM>>
        extends FxRegionViewer<javafx.scene.layout.Region, N, NV, NM> {

    public FxLayoutViewer() {
        super((NV) new RegionViewerBase<N, NV, NM>());
    }

    @Override
    javafx.scene.layout.Region createFxNode() {
        return new javafx.scene.layout.Region() {
            @Override
            protected void layoutChildren() {
                // We disable the Region children layout (autosize) as the layout is now done by NagaFx (and not JavaFx)
            }
        };
    }
}
