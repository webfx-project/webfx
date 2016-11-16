package naga.toolkit.cell.collators;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface NodeCollator {

    GuiNode collateNodes(GuiNode[] nodes);

}
