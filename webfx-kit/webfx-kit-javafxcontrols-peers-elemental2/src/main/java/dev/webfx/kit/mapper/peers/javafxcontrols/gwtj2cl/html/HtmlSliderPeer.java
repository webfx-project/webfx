package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.SliderPeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.SliderPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.layoutmeasurable.HtmlMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.platform.util.Numbers;
import dev.webfx.platform.util.Strings;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;

/**
 * @author Bruno Salmon
 */
public final class HtmlSliderPeer
        <N extends Slider, NB extends SliderPeerBase<N, NB, NM>, NM extends SliderPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements SliderPeerMixin<N, NB, NM>, HtmlMeasurable {

    public HtmlSliderPeer() {
        this((NB) new SliderPeerBase(), HtmlUtil.createInputElement("range"));
    }

    public HtmlSliderPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.step = "0.01"; // To get a smoother step by default (otherwise step = 1 makes a discrete step)
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

    @Override
    public void updateOrientation(Orientation orientation) {
        setElementStyleAttribute("appearance", orientation == Orientation.HORIZONTAL ? "slider-horizontal" : "slider-vertical");
    }
}
