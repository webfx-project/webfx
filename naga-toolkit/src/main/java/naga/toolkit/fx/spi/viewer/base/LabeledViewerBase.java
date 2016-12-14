package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.control.Labeled;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.LabeledViewer;

/**
 * @author Bruno Salmon
 */
public abstract class LabeledViewerBase
        <N extends Labeled, NV extends LabeledViewerBase<N, NV, NM>, NM extends LabeledViewerMixin<N, NV, NM>>

        extends ControlViewerBase<N, NV, NM>
        implements LabeledViewer<N> {

    @Override
    public void bind(N labeled, DrawingRequester drawingRequester) {
        super.bind(labeled, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.textProperty(),
                node.graphicProperty());
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        N c = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(c.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(c.graphicProperty(), changedProperty, mixin::updateGraphic)
                ;
    }
}
