package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.view.base.SliderViewBase;
import naga.toolkit.fx.spi.view.base.SliderViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxSliderView
        extends FxControlView<javafx.scene.control.Slider, Slider, SliderViewBase, SliderViewMixin>
        implements SliderViewMixin, FxLayoutMeasurable {

    public FxSliderView() {
        super(new SliderViewBase());
    }

    @Override
    javafx.scene.control.Slider createFxNode() {
        javafx.scene.control.Slider slider = new javafx.scene.control.Slider();
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(4);
        slider.setMajorTickUnit(500);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> updateNodeValue(newValue.doubleValue()));
        return slider;
    }

    @Override
    public void updateMin(Double min) {
        getFxNode().setMin(min);
    }

    @Override
    public void updateMax(Double max) {
        getFxNode().setMax(max);
    }

    @Override
    public void updateValue(Double value) {
        getFxNode().setValue(value);
    }
}
