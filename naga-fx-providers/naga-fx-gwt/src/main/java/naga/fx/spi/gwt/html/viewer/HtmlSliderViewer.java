package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.control.Slider;
import naga.fx.spi.viewer.base.SliderViewerBase;
import naga.fx.spi.viewer.base.SliderViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlSliderViewer
        <N extends Slider, NB extends SliderViewerBase<N, NB, NM>, NM extends SliderViewerMixin<N, NB, NM>>

        extends HtmlRegionViewer<N, NB, NM>
        implements SliderViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlSliderViewer() {
        this((NB) new SliderViewerBase(), HtmlUtil.createInputElement("range"));
    }

    public HtmlSliderViewer(NB base, HTMLElement element) {
        super(base, element);
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.onchange = e -> {
            updateNodeValue(Numbers.doubleValue(inputElement.value));
            return null;
        };
    }

    @Override
    public void updateMin(Double min) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.min = Strings.toString(min);
    }

    @Override
    public void updateMax(Double max) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.max = Strings.toString(max);
    }

    @Override
    public void updateValue(Double value) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.value = Strings.toString(value);
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }
}
