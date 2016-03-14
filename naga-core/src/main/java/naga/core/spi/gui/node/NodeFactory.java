package naga.core.spi.gui.node;

/**
 * @author Bruno Salmon
 */
public interface NodeFactory<T extends Node> {

   T createNode();

}
