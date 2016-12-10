package naga.toolkit.fx.spi.view.base;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.view.SliderView;

/**
 * @author Bruno Salmon
 */
public class SliderViewBase
        extends ControlViewBase<Slider, SliderViewBase, SliderViewMixin>
        implements SliderView {

    @Override
    public void bind(Slider s, DrawingRequester drawingRequester) {
        super.bind(s, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester,
                s.minProperty(),
                s.maxProperty(),
                s.valueProperty());
    }

    @Override
    public boolean updateProperty(Property changedProperty) {
        Slider s = node;
        return super.updateProperty(changedProperty)
                || updateProperty(s.minProperty(), changedProperty, mixin::updateMin)
                || updateProperty(s.maxProperty(), changedProperty, mixin::updateMax)
                || updateProperty(s.valueProperty(), changedProperty, mixin::updateValue);
    }
}
