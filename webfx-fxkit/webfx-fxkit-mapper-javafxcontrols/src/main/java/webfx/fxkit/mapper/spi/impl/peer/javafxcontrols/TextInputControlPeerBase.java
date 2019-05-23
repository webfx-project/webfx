package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;

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
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
                || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(node.promptTextProperty(), changedProperty, mixin::updatePrompt)
                || updateProperty(node.editableProperty(), changedProperty, mixin::updateEditable)
                ;
    }
}
