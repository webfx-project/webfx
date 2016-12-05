package naga.providers.toolkit.javafx.fx.view;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.layout.Region;
import naga.toolkit.fx.spi.view.RegionView;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
abstract class FxRegionView<N extends Region, FxN extends javafx.scene.layout.Region> extends FxNodeViewImpl<N, FxN> implements RegionView<N> {

    @Override
    void setAndBindNodeProperties(N buttonBase, FxN fxButtonBase) {
        super.setAndBindNodeProperties(buttonBase, fxButtonBase);
        fxButtonBase.minWidthProperty().bind(buttonBase.minWidthProperty());
        fxButtonBase.maxWidthProperty().bind(buttonBase.maxWidthProperty());
        fxButtonBase.minHeightProperty().bind(buttonBase.minHeightProperty());
        fxButtonBase.maxHeightProperty().bind(buttonBase.maxHeightProperty());
        fxButtonBase.prefWidthProperty().bind(buttonBase.prefWidthProperty());
        fxButtonBase.prefHeightProperty().bind(buttonBase.prefHeightProperty());
        bindRegionSetterToProperty("setWidth", buttonBase.widthProperty());
        bindRegionSetterToProperty("setHeight", buttonBase.heightProperty());
    }

    private void bindRegionSetterToProperty(String setterName, Property<Double> property) {
        try {
            Method regionSetter = javafx.scene.layout.Region.class.getDeclaredMethod(setterName, double.class);
            regionSetter.setAccessible(true);
            property.addListener((observable, oldValue, newWidth) -> {
                try {
                    regionSetter.invoke(getFxNode(), newWidth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
