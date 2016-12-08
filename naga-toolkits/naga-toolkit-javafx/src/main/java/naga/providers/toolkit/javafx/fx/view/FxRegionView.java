package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.base.RegionViewBase;
import naga.toolkit.fx.spi.view.base.RegionViewMixin;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
abstract class FxRegionView
        <FxN extends javafx.scene.layout.Region, N extends Region, NV extends RegionViewBase<N, NV, NM>, NM extends RegionViewMixin<N, NV, NM>>

        extends FxNodeView<FxN, N, NV, NM>
        implements RegionViewMixin<N, NV, NM> {


    FxRegionView(NV base) {
        super(base);
/*
        FxN region = getFxNode();
        fxNode.minWidthProperty().bind(region.minWidthProperty());
        fxNode.maxWidthProperty().bind(region.maxWidthProperty());
        fxNode.minHeightProperty().bind(region.minHeightProperty());
        fxNode.maxHeightProperty().bind(region.maxHeightProperty());
        fxNode.prefWidthProperty().bind(region.prefWidthProperty());
        fxNode.prefHeightProperty().bind(region.prefHeightProperty());
*/
    }

    @Override
    public void updateWidth(Double width) {
        if (width != getFxNode().getWidth())
            callRegionSetter("setWidth", width);
    }

    @Override
    public void updateHeight(Double height) {
        if (height != getFxNode().getHeight())
            callRegionSetter("setHeight", height);
    }

    private void callRegionSetter(String setterName, Double value) {
        try {
            Method regionSetter = javafx.scene.layout.Region.class.getDeclaredMethod(setterName, double.class);
            regionSetter.setAccessible(true);
            try {
                regionSetter.invoke(getFxNode(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
