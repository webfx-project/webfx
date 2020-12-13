package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.shape.SVGPath;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class SVGPathPeerBase
        <N extends SVGPath, NB extends SVGPathPeerBase<N, NB, NM>, NM extends SVGPathPeerMixin<N, NB, NM>>

        extends ShapePeerBase<N, NB, NM> {

    @Override
    public void bind(N p, SceneRequester sceneRequester) {
        super.bind(p, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , p.fillRuleProperty()
                , p.contentProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = node;
        return super.updateProperty(changedProperty)
                || updateProperty(c.fillRuleProperty(), changedProperty, p -> mixin.updateFillRule(p))
                || updateProperty(c.contentProperty(), changedProperty, p -> mixin.updateContent(p))
                ;
    }

}
