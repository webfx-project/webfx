package naga.toolkit.cell.collators;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface NodeCollator<N> {

    GuiNode<N> collateNodes(GuiNode<N>[] nodes);

}
