package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public interface FxNodeView<N extends Node, FxN extends javafx.scene.Node> extends NodeView<N> {

    FxN getFxNode();

}
