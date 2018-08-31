package webfx.fx.spi.javafx.peer;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import webfx.fx.spi.peer.base.RegionPeerBase;
import webfx.fx.spi.peer.base.RegionPeerMixin;

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
    public void updateWidth(Double width) {
        if (width != getFxNode().getWidth())
            callRegionSetter("setWidth", width);
    }

    @Override
    public void updateHeight(Double height) {
        if (height != getFxNode().getHeight())
            callRegionSetter("setHeight", height);
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
