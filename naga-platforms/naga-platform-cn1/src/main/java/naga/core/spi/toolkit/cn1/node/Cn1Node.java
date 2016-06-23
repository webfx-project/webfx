package naga.core.spi.toolkit.cn1.node;

import com.codename1.ui.Component;
import naga.core.spi.toolkit.node.GuiNode;


/**
 * @author Bruno Salmon
 */
public class Cn1Node<N extends Component> implements GuiNode<N> {

    protected final N node;

    public Cn1Node(N node) {
        this.node = node;
    }

    @Override
    public N unwrapToNativeNode() {
        return node;
    }

    @Override
    public void requestFocus() {
        node.requestFocus();
    }

}
