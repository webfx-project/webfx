package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.viewer.base.RegionViewerBase;
import naga.toolkit.fx.spi.viewer.base.RegionViewerMixin;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
abstract class FxRegionViewer
        <FxN extends javafx.scene.layout.Region, N extends Region, NB extends RegionViewerBase<N, NB, NM>, NM extends RegionViewerMixin<N, NB, NM>>

        extends FxNodeViewer<FxN, N, NB, NM>
        implements RegionViewerMixin<N, NB, NM> {


    FxRegionViewer(NB base) {
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
