package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.TextInputControlViewer;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControlViewerBase
        <N extends TextInputControl, NV extends TextInputControlViewerBase<N, NV, NM>, NM extends TextInputControlViewerMixin<N, NV, NM>>

        extends ControlViewerBase<N, NV, NM>
        implements TextInputControlViewer<N> {

    @Override
    public void bind(N buttonBase, DrawingRequester drawingRequester) {
        super.bind(buttonBase, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester
                , node.fontProperty()
                , node.textProperty()
                , node.promptTextProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
                || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(node.promptTextProperty(), changedProperty, mixin::updatePrompt)
                ;
    }
}
