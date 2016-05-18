package naga.core.spi.gui;

/**
 * @author Bruno Salmon
 */
public interface GuiNode<N> {

    N unwrapToNativeNode();

    void requestFocus();

}
