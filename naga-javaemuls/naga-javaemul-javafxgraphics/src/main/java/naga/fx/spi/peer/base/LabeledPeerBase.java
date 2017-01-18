package naga.fx.spi.peer.base;

import emul.javafx.beans.value.ObservableValue;
import naga.fx.scene.SceneRequester;
import emul.javafx.scene.control.Labeled;

/**
 * @author Bruno Salmon
 */
public abstract class LabeledPeerBase
        <N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends ControlPeerBase<N, NB, NM> {

    @Override
    public void bind(N labeled, SceneRequester sceneRequester) {
        super.bind(labeled, sceneRequester);
        requestUpdateOnPropertiesChange(sceneRequester
                , node.textProperty()
                , node.graphicProperty()
                , node.fontProperty()
                , node.textAlignmentProperty()
                , node.textFillProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(node.graphicProperty(), changedProperty, mixin::updateGraphic)
                || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
                || updateProperty(node.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
                || updateProperty(node.textFillProperty(), changedProperty, mixin::updateTextFill)
                ;
    }
}
