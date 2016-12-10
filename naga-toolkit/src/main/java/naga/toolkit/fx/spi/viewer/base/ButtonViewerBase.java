package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.ButtonViewer;

/**
 * @author Bruno Salmon
 */
public class ButtonViewerBase
        extends ButtonBaseViewerBase<Button, ButtonViewerBase, ButtonViewerMixin>
        implements ButtonViewer {

    @Override
    public void bind(Button button, DrawingRequester drawingRequester) {
        super.bind(button, drawingRequester);
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        return super.updateProperty(changedProperty);
    }
}
