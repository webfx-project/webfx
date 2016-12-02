package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.ButtonView;

/**
 * @author Bruno Salmon
 */
public class ButtonViewBase
        extends ButtonBaseViewBase<Button, ButtonViewBase, ButtonViewMixin>
        implements ButtonView {

    @Override
    public void bind(Button button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
