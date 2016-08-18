package naga.toolkit.cell.renderers;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface ValueRenderer<N> {

    GuiNode<N> renderCellValue(Object value);

}
