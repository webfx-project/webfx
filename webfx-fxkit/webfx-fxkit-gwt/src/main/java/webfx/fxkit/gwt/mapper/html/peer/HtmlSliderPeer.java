package webfx.fxkit.gwt.mapper.html.peer;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import webfx.fxkits.core.mapper.spi.impl.peer.SliderPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.SliderPeerMixin;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Strings;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import emul.javafx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public final class HtmlSliderPeer
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements SliderPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlSliderPeer() {
        this((NB) new SliderPeerBase(), HtmlUtil.createInputElement("range"));
    }

    public HtmlSliderPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.oninput = e -> {
            updateNodeValue(Numbers.doubleValue(inputElement.value));
            return null;
        };
    }

    @Override
    public void updateMin(Number min) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.min = Strings.toString(min);
    }

    @Override
    public void updateMax(Number max) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.max = Strings.toString(max);
    }

    @Override
    public void updateValue(Number value) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.value = Strings.toString(value);
    }

}
