package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public interface FxNodeView<N extends Node, FxN extends javafx.scene.Node> extends NodeView<N> {

    FxN getFxNode();

}
