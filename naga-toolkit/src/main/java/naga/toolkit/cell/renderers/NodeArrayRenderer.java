package naga.toolkit.cell.renderers;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface NodeArrayRenderer<N> {

    GuiNode<N> renderNodeArray(GuiNode<N>[] nodeArray);

    static NodeArrayRenderer hBoxNodeArrayRenderer() {
        return Toolkit.get()::createHBox;
    }

    static NodeArrayRenderer vBoxNodeArrayRenderer() {
        return Toolkit.get()::createVBox;
    }
}
