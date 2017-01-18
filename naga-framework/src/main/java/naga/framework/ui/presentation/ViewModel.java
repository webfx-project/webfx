package naga.framework.ui.presentation;

import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface ViewModel<N extends Node> {

    N getContentNode();

}
