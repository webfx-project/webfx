package webfx.extras.visual.controls.grid.registry;

import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.peers.gwt.html.HtmlVisualGridPeer;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public final class VisualGridRegistry {

    public static void registerDataGrid() {
        registerNodePeerFactory(VisualGrid.class, HtmlVisualGridPeer::new);
    }

}
