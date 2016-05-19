package naga.core.spi.toolkit;

/**
 * @author Bruno Salmon
 */
public interface GuiNode<N> {

    N unwrapToNativeNode();

    void requestFocus();

}
