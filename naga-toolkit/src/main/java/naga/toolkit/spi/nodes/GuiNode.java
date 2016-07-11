package naga.toolkit.spi.nodes;

/**
 * @author Bruno Salmon
 */
public interface GuiNode<N> {

    N unwrapToNativeNode();

    void requestFocus();

}
