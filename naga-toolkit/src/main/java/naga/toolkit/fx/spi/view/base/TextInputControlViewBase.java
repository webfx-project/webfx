package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.TextInputControlView;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControlViewBase
        <N extends TextInputControl, NV extends TextInputControlViewBase<N, NV, NM>, NM extends TextInputControlViewMixin<N, NV, NM>>

        extends ControlViewBase<N, NV, NM>
        implements TextInputControlView<N> {

    @Override
    public void bind(N buttonBase, DrawingRequester drawingRequester) {
        super.bind(buttonBase, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.fontProperty(),
                node.textProperty(),
                node.promptTextProperty()
        );
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty)
                || updateProperty(node.fontProperty(), changedProperty, mixin::updateFont)
                || updateProperty(node.textProperty(), changedProperty, mixin::updateText)
                || updateProperty(node.promptTextProperty(), changedProperty, mixin::updatePrompt)
                ;
    }
}
