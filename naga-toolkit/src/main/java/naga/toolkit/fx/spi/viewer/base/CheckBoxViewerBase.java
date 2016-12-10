package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.CheckBoxViewer;

/**
 * @author Bruno Salmon
 */
public class CheckBoxViewerBase
        extends ButtonBaseViewerBase<CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin>
        implements CheckBoxViewer {

    @Override
    public void bind(CheckBox checkBox, DrawingRequester drawingRequester) {
        super.bind(checkBox, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                node.selectedProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        CheckBox c = getNode();
        return super.updateProperty(changedProperty)
                || updateProperty(c.selectedProperty(), changedProperty, mixin::updateSelected);
    }
}
