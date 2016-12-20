package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.viewer.base.SliderViewerBase;
import naga.toolkit.fx.spi.viewer.base.SliderViewerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSliderViewer
        <N extends Slider, NB extends SliderViewerBase<N, NB, NM>, NM extends SliderViewerMixin<N, NB, NM>>

        extends SwingRegionViewer<N, NB, NM>
        implements SliderViewerMixin<N, NB, NM>, SwingEmbedComponentViewer<N>, SwingLayoutMeasurable<N> {

    private final JSlider swingSlider = new JSlider();

    public SwingSliderViewer() {
        this((NB) new SliderViewerBase());
    }

    public SwingSliderViewer(NB base) {
        super(base);
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
