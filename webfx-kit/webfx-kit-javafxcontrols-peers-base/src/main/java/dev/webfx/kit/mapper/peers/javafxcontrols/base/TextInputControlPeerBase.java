package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControlPeerBase
    <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

    extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N buttonBase, SceneRequester sceneRequester) {
        super.bind(buttonBase, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
            , node.fontProperty()
            , node.textProperty()
            , node.promptTextProperty()
            , node.editableProperty()
            , node.anchorProperty()
            , node.caretPositionProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
               || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
               || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
               || updateProperty(node.promptTextProperty(), changedProperty, mixin::updatePromptText)
               || updateProperty(node.editableProperty(), changedProperty, mixin::updateEditable)
               || updateProperty(node.anchorProperty(), changedProperty, mixin::updateAnchorPosition)
               || updateProperty(node.caretPositionProperty(), changedProperty, mixin::updateCaretPosition)
            ;
    }
}
