package naga.toolkit.spi.nodes;

/**
 * @author Bruno Salmon
 */
public interface GuiNode {

    <T> T unwrapToNativeNode();

    void requestFocus();

}
