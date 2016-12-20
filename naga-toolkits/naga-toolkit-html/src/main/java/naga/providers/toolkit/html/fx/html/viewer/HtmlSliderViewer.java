package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.viewer.base.SliderViewerBase;
import naga.toolkit.fx.spi.viewer.base.SliderViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlSliderViewer
        <N extends Slider, NV extends SliderViewerBase<N, NV, NM>, NM extends SliderViewerMixin<N, NV, NM>>

        extends HtmlRegionViewer<N, NV, NM>
        implements SliderViewerMixin<N, NV, NM>, HtmlLayoutMeasurable {

    public HtmlSliderViewer() {
        this((NV) new SliderViewerBase(), HtmlUtil.createInputElement("range"));
    }

    public HtmlSliderViewer(NV base, HTMLElement element) {
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
}
