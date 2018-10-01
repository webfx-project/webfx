package webfx.fxkit.javafx.mapper.peer;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import webfx.fxkits.core.mapper.spi.impl.peer.RegionPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.RegionPeerMixin;
import webfx.platform.shared.util.Numbers;

import java.lang.reflect.Method;

/**
 * @author Bruno Salmon
 */
abstract class FxRegionPeer
        <FxN extends javafx.scene.layout.Region, N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends FxNodePeer<FxN, N, NB, NM>
        implements RegionPeerMixin<N, NB, NM> {


    FxRegionPeer(NB base) {
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
    public void updateWidth(Number width) {
        double w = Numbers.doubleValue(width);
        if (w != getFxNode().getWidth())
            callRegionSetter("setWidth", w);
    }

    @Override
    public void updateHeight(Number height) {
        double h = Numbers.doubleValue(height);
        if (h != getFxNode().getHeight())
            callRegionSetter("setHeight", h);
    }

    @Override
    public void updateBackground(Background background) {
        if (background != null)
            getFxNode().setBackground(toFxBackground(background));
    }

    @Override
    public void updateBorder(Border border) {
        getFxNode().setBorder(toFxBorder(border));
    }

    @Override
    public void updatePadding(Insets padding) {
        getFxNode().setPadding(padding);
    }

    private static javafx.scene.layout.Background toFxBackground(Background bg) {
        return bg;
    }

    private static javafx.scene.layout.CornerRadii toFxRadii(CornerRadii radii) {
        return radii;
    }

    private static javafx.geometry.Insets toFxInsets(Insets insets) {
        return insets;
    }

    private static javafx.scene.image.Image toFxImage(Image image) {
        return image;
    }

    private static javafx.scene.layout.Border toFxBorder(Border border) {
        return border;
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
