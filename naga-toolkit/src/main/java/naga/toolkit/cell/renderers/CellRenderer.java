package naga.toolkit.cell.renderers;

import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface CellRenderer<N> {

    GuiNode<N> renderCellValue(Object value);

}
