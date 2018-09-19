package webfx.fxkits.extra.mapper.spi.peer.impl;

import javafx.beans.value.ObservableValue;
import webfx.fxkits.core.mapper.spi.impl.peer.RegionPeerBase;
import webfx.fxkits.extra.control.HtmlText;
import webfx.fxkits.core.mapper.spi.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class HtmlTextPeerBase
        <N extends HtmlText, NB extends HtmlTextPeerBase<N, NB, NM>, NM extends HtmlTextPeerMixin<N, NB, NM>>

        extends RegionPeerBase<N, NB, NM> {

    @Override
    public void bind(N t, SceneRequester sceneRequester) {
        super.bind(t, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , t.textProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N n = node;
        return super.updateProperty(changedProperty)
                || updateProperty(n.textProperty(), changedProperty, mixin::updateText)
                ;
    }
}
