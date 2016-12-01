package naga.toolkit.drawing.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.drawing.scene.control.CheckBox;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.CheckBoxView;

/**
 * @author Bruno Salmon
 */
public class CheckBoxViewBase
        extends ButtonBaseViewBase<CheckBox, CheckBoxViewBase, CheckBoxViewMixin>
        implements CheckBoxView {

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
