package naga.fx.spi.javafx.peer;

import naga.fx.scene.control.Slider;
import naga.fx.spi.peer.base.SliderPeerBase;
import naga.fx.spi.peer.base.SliderPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxSliderPeer
        <FxN extends javafx.scene.control.Slider, N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends FxControlPeer<FxN, N, NB, NM>
        implements SliderPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxSliderPeer() {
        super((NB) new SliderPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        javafx.scene.control.Slider slider = new javafx.scene.control.Slider();
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMinorTickCount(4);
        slider.setMajorTickUnit(500);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> updateNodeValue(newValue.doubleValue()));
        return (FxN) slider;
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
