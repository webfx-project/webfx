package webfx.kit.mapper.peers.javafxcontrols.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Labeled;
import webfx.kit.mapper.peers.javafxgraphics.SceneRequester;

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
                || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
                || updateProperty(node.textAlignmentProperty(), changedProperty, mixin::updateTextAlignment)
                || updateProperty(node.textFillProperty(), changedProperty, mixin::updateTextFill)
                || updateProperty(node.graphicProperty(), changedProperty, mixin::updateGraphic)
                || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
                ;
    }
}
