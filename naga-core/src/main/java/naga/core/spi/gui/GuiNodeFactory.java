package naga.core.spi.gui;

/**
 * @author Bruno Salmon
 */
public interface GuiNodeFactory<T extends GuiNode> {

   T createNode();

}
