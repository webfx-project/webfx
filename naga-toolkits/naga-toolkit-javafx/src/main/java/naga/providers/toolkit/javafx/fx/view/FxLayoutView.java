package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxLayoutView
        <N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>
        extends FxRegionView<javafx.scene.layout.Region, N, NV, NM> {

    public FxLayoutView() {
        super((NV) new RegionViewBase<N, NV, NM>());
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
