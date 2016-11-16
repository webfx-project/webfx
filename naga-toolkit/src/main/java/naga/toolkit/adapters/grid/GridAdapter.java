package naga.toolkit.adapters.grid;

import naga.toolkit.display.DisplayColumn;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface GridAdapter<C> {

    void setCellContent(C cell, GuiNode content, DisplayColumn displayColumn);

}
