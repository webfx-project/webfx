package webfx.extras.visual.controls.grid.registry;

import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.peers.gwt.html.HtmlVisualGridPeer;

import static webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

public final class VisualGridRegistry {

    public static void registerDataGrid() {
        registerNodePeerFactory(VisualGrid.class, HtmlVisualGridPeer::new);
    }

}
