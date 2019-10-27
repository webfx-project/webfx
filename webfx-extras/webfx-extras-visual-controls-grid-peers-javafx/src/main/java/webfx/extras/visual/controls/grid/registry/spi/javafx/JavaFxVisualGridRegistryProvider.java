package webfx.extras.visual.controls.grid.registry.spi.javafx;

import webfx.extras.cell.collator.grid.GridCollator;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.peers.javafx.FxVisualGridPeer;
import webfx.extras.visual.controls.grid.registry.spi.VisualGridRegistryProvider;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;

public class JavaFxVisualGridRegistryProvider implements VisualGridRegistryProvider {

    static {
        registerNodePeerFactory(GridCollator.class, GridCollator.GridCollatorPeer::new);
    }

    public void registerDataGrid() {
        registerNodePeerFactory(VisualGrid.class, FxVisualGridPeer::new);
    }

}
