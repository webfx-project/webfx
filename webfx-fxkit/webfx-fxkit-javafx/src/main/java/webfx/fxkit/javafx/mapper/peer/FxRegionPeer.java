package webfx.fxkit.javafx.mapper.peer;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerMixin;

/**
 * @author Bruno Salmon
 */
public abstract class FxRegionPeer
        <FxN extends javafx.scene.layout.Region, N extends Region, NB extends RegionPeerBase<N, NB, NM>, NM extends RegionPeerMixin<N, NB, NM>>

        extends FxNodePeer<FxN, N, NB, NM>
        implements RegionPeerMixin<N, NB, NM> {


    protected FxRegionPeer(NB base) {
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
        double w = width.doubleValue();
        FxN fxNode = getFxNode();
        fxNode.resize(w, fxNode.getHeight());
    }

    @Override
    public void updateHeight(Number height) {
        double h = height.doubleValue();
        FxN fxNode = getFxNode();
        fxNode.resize(fxNode.getWidth(), h);
    }

    @Override
    public void updateBackground(Background background) {
        if (background != null)
            getFxNode().setBackground(background);
    }

    @Override
    public void updateBorder(Border border) {
        getFxNode().setBorder(border);
    }

    @Override
    public void updatePadding(Insets padding) {
        getFxNode().setPadding(padding);
    }
}
