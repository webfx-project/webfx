package naga.providers.toolkit.html.fx.html.view;

import elemental2.HTMLInputElement;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.Slider;
import naga.toolkit.fx.spi.view.base.SliderViewBase;
import naga.toolkit.fx.spi.view.base.SliderViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlSliderView
        extends HtmlRegionView<Slider, SliderViewBase, SliderViewMixin>
        implements SliderViewMixin, HtmlLayoutMeasurable {

    public HtmlSliderView() {
        super(new SliderViewBase(), HtmlUtil.createInputElement("range"));
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
