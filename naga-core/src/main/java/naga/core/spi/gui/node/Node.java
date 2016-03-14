package naga.core.spi.gui.node;

/**
 * @author Bruno Salmon
 */
public interface Node<N> {

    N unwrapToToolkitNode();

    void requestFocus();

}
