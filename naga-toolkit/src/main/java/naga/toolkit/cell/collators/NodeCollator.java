package naga.toolkit.cell.collators;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface NodeCollator<N> {

    GuiNode<N> collateNodes(GuiNode<N>[] nodes);

    static NodeCollator hBoxCollator() {
        return Toolkit.get()::createHBox;
    }

    static NodeCollator vBoxCollator() {
        return Toolkit.get()::createVBox;
    }
}
