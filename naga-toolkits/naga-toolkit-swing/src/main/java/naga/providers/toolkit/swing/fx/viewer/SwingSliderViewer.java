package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.viewer.base.SliderViewerBase;
import naga.toolkit.fx.spi.viewer.base.SliderViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSliderViewer
        extends SwingRegionViewer<Slider, SliderViewerBase, SliderViewerMixin>
        implements SliderViewerMixin, SwingEmbedComponentViewer<Slider>, SwingLayoutMeasurable<Slider> {

    private final JSlider swingSlider = new JSlider();

    public SwingSliderViewer() {
        super(new SliderViewerBase());
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
