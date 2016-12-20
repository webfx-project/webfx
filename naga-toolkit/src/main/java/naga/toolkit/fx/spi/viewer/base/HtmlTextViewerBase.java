package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.ext.control.HtmlText;
import naga.toolkit.fx.scene.SceneRequester;

/**
 * @author Bruno Salmon
 */
public class HtmlTextViewerBase
        <N extends HtmlText, NB extends HtmlTextViewerBase<N, NB, NM>, NM extends HtmlTextViewerMixin<N, NB, NM>>

        extends RegionViewerBase<N, NB, NM> {

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
