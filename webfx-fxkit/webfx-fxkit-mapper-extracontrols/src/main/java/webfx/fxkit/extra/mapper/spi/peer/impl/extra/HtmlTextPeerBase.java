package webfx.fxkit.extra.mapper.spi.peer.impl.extra;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.RegionPeerBase;
import webfx.fxkit.mapper.spi.SceneRequester;

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
