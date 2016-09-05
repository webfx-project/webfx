package naga.toolkit.adapters.grid;

import naga.toolkit.display.DisplayColumn;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface GridAdapter<C, N> {

    void setCellContent(C cell, GuiNode<N> content, DisplayColumn displayColumn);

}
