package webfx.fxkit.extra.controls.mapper.spi.peer.impl.base;

import javafx.beans.value.ObservableValue;
import webfx.fxkit.extra.controls.html.HtmlText;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.RegionPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.SceneRequester;

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
