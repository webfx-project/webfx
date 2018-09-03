package webfx.fxkits.core.spi.peer.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import webfx.fxkits.core.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class TextFieldPeerBase
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends TextInputControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N buttonBase, SceneRequester sceneRequester) {
        super.bind(buttonBase, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.alignmentProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                    || updateProperty(node.alignmentProperty(), changedProperty, mixin::updateAlignment)
                ;
    }
}
