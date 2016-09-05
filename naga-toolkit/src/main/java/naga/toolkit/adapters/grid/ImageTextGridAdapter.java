package naga.toolkit.adapters.grid;

import naga.toolkit.display.DisplayColumn;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface ImageTextGridAdapter<C, N> extends GridAdapter<C, N> {

    default void setCellContent(C cell, GuiNode<N> content, DisplayColumn displayColumn) {
        setCellImageAndTextContent(cell, content, null, displayColumn);
    }

    default void setCellTextContent(C cell, String text, DisplayColumn displayColumn) {
        setCellImageAndTextContent(cell, null, text, displayColumn);
    }

    void setCellImageAndTextContent(C cell, GuiNode<N> image, String text, DisplayColumn displayColumn);

}
