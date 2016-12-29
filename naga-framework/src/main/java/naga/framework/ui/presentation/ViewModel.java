package naga.framework.ui.presentation;

import naga.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface ViewModel<N extends Node> {

    N getContentNode();

}
