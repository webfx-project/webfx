package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class CheckBoxPeerBase
        <N extends CheckBox, NB extends CheckBoxPeerBase<N, NB, NM>, NM extends CheckBoxPeerMixin<N, NB, NM>>
        extends ButtonBasePeerBase<N, NB, NM> {

    @Override
    public void bind(N checkBox, SceneRequester sceneRequester) {
        super.bind(checkBox, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.selectedProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        CheckBox c = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(c.selectedProperty(), changedProperty, mixin::updateSelected)
                ;
    }
}
