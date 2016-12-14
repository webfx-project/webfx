package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.viewer.base.SliderViewerBase;
import naga.toolkit.fx.spi.viewer.base.SliderViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxSliderViewer
        extends FxControlViewer<javafx.scene.control.Slider, Slider, SliderViewerBase, SliderViewerMixin>
        implements SliderViewerMixin, FxLayoutMeasurable {

    public FxSliderViewer() {
        super(new SliderViewerBase());
    }

    @Override
    protected javafx.scene.control.Slider createFxNode() {
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
