package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.SceneRequester;
import naga.toolkit.fx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public class CheckBoxViewerBase
        <N extends CheckBox, NB extends CheckBoxViewerBase<N, NB, NM>, NM extends CheckBoxViewerMixin<N, NB, NM>>
        extends ButtonBaseViewerBase<N, NB, NM> {

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
