package naga.core.spi.toolkit.node;

/**
 * @author Bruno Salmon
 */
public interface GuiNode<N> {

    N unwrapToNativeNode();

    void requestFocus();

}
