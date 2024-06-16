package dev.webfx.kit.mapper.peers.javafxcontrols.base;

import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 * @author Bruno Salmon
 */
public class TextAreaPeerBase
        <N extends TextArea, NB extends TextAreaPeerBase<N, NB, NM>, NM extends TextAreaPeerMixin<N, NB, NM>>

        extends TextInputControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N buttonBase, SceneRequester sceneRequester) {
        super.bind(buttonBase, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.wrapTextProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
               || updateProperty(node.wrapTextProperty(), changedProperty, mixin::updateWrapText)
                ;
    }


}
