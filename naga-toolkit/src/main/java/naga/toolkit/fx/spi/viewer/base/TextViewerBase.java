package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public class TextViewerBase
        <N extends Text, NV extends TextViewerBase<N, NV, NM>, NM extends TextViewerMixin<N, NV, NM>>

        extends ShapeViewerBase<N, NV, NM> {

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
