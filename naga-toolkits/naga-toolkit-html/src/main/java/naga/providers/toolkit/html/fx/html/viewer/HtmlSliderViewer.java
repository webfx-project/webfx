package naga.providers.toolkit.html.fx.html.viewer;

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
        extends HtmlRegionViewer<Slider, SliderViewerBase, SliderViewerMixin>
        implements SliderViewerMixin, HtmlLayoutMeasurable {

    public HtmlSliderViewer() {
        super(new SliderViewerBase(), HtmlUtil.createInputElement("range"));
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
