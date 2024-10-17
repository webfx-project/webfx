package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.beans.value.ObservableValue;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import javafx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public class TextPeerBase
    <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>

    extends ShapePeerBase<N, NB, NM> {

    @Override
    public void bind(N t, SceneRequester sceneRequester) {
        super.bind(t, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
            , t.textOriginProperty()
            , t.xProperty()
            , t.yProperty()
            , t.wrappingWidthProperty()
            , t.lineSpacingProperty()
            , t.strikethroughProperty()
            , t.textAlignmentProperty()
            , t.fontProperty()
            , t.textProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        Text ts = node;
        return super.updateProperty(changedProperty)
               || updateProperty(ts.textProperty(), changedProperty, mixin::updateText)
               || updateProperty(ts.xProperty(), changedProperty, p -> mixin.updateX(p.doubleValue()))
               || updateProperty(ts.yProperty(), changedProperty, p -> mixin.updateY(p.doubleValue()))
               || updateProperty(ts.wrappingWidthProperty(), changedProperty, p -> mixin.updateWrappingWidth(p.doubleValue()))
               || updateProperty(ts.lineSpacingProperty(), changedProperty, mixin::updateLineSpacing)
               || updateProperty(ts.strikethroughProperty(), changedProperty, mixin::updateStrikethrough)
               || updateProperty(ts.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
               || updateProperty(ts.textOriginProperty(), changedProperty, mixin::updateTextOrigin)
               || updateProperty(ts.fontProperty(), changedProperty, mixin::updateFont)
            ;
    }
}
