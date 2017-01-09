package naga.fx.spi.swing.peer;

import naga.fx.scene.control.Slider;
import naga.fx.spi.peer.base.SliderPeerBase;
import naga.fx.spi.peer.base.SliderPeerMixin;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingSliderPeer
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends SwingRegionPeer<N, NB, NM>
        implements SliderPeerMixin<N, NB, NM>, SwingEmbedComponentPeer<N>, SwingLayoutMeasurable<N> {

    private final JSlider swingSlider = new JSlider();

    public SwingSliderPeer() {
        this((NB) new SliderPeerBase());
    }

    public SwingSliderPeer(NB base) {
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
