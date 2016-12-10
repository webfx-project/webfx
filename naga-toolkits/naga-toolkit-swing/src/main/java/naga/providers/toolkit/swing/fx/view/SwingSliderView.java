package naga.providers.toolkit.swing.fx.view;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.view.base.SliderViewBase;
import naga.toolkit.fx.spi.view.base.SliderViewMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSliderView
        extends SwingRegionView<Slider, SliderViewBase, SliderViewMixin>
        implements SliderViewMixin, SwingEmbedComponentView<Slider>, SwingLayoutMeasurable<Slider> {

    private final JSlider swingSlider = new JSlider();

    public SwingSliderView() {
        super(new SliderViewBase());
        swingSlider.addChangeListener(e -> updateNodeValue((double) swingSlider.getValue()));
    }

    @Override
    public JComponent getSwingComponent() {
        return swingSlider;
    }

    @Override
    public void updateMin(Double min) {
        swingSlider.setMinimum(min.intValue());
    }

    @Override
    public void updateMax(Double max) {
        swingSlider.setMaximum(max.intValue());
    }

    @Override
    public void updateValue(Double value) {
        swingSlider.setValue(value.intValue());
    }

}
