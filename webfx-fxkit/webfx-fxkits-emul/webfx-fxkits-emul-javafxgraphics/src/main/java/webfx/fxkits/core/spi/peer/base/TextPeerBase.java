package webfx.fxkits.core.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import webfx.fxkits.core.scene.SceneRequester;
import emul.javafx.scene.text.Text;

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
                , t.textProperty()
                , t.textOriginProperty()
                , t.wrappingWidthProperty()
                , t.textAlignmentProperty()
                , t.fontProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        Text ts = node;
        return super.updateProperty(changedProperty)
                || updateProperty(ts.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(ts.xProperty(), changedProperty, mixin::updateX)
                || updateProperty(ts.yProperty(), changedProperty, mixin::updateY)
                || updateProperty(ts.wrappingWidthProperty(), changedProperty, mixin::updateWrappingWidth)
                || updateProperty(ts.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
                || updateProperty(ts.textOriginProperty(), changedProperty, mixin::updateTextOrigin)
                || updateProperty(ts.fontProperty(), changedProperty, mixin::updateFont);
    }
}
