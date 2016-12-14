package naga.toolkit.fx.spi.viewer.base;

import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.DrawingRequester;
import naga.toolkit.fx.spi.viewer.SliderViewer;

/**
 * @author Bruno Salmon
 */
public class SliderViewerBase
        extends ControlViewerBase<Slider, SliderViewerBase, SliderViewerMixin>
        implements SliderViewer {

    @Override
    public void bind(Slider s, DrawingRequester drawingRequester) {
        super.bind(s, drawingRequester);
        requestUpdateOnPropertiesChange(drawingRequester
                , s.minProperty()
                , s.maxProperty()
                , s.valueProperty()
        );
    }

    @Override
    public boolean updateProperty(ObservableValue changedProperty) {
        Slider s = node;
        return super.updateProperty(changedProperty)
                || updateProperty(s.minProperty(), changedProperty, mixin::updateMin)
                || updateProperty(s.maxProperty(), changedProperty, mixin::updateMax)
                || updateProperty(s.valueProperty(), changedProperty, mixin::updateValue)
                ;
    }
}
