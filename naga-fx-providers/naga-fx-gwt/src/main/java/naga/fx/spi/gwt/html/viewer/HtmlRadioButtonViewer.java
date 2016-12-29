package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.control.RadioButton;
import naga.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRadioButtonViewer
        <N extends RadioButton, NB extends RadioButtonViewerBase<N, NB, NM>, NM extends RadioButtonViewerMixin<N, NB, NM>>

        extends HtmlButtonBaseViewer<N, NB, NM>
        implements RadioButtonViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    private final HTMLInputElement radioButtonElement;

    public HtmlRadioButtonViewer() {
        this((NB) new RadioButtonViewerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlRadioButtonViewer(NB base, HTMLElement element) {
        super(base, element);
        radioButtonElement = HtmlUtil.createRadioButton();
        radioButtonElement.onclick = event -> {
            getNode().setSelected(radioButtonElement.checked);
            return null;
        };
        HtmlUtil.setStyleAttribute(getElement(), "margin-top", "0");
        HtmlUtil.setStyleAttribute(getElement(), "margin-bottom", "0");
        HtmlUtil.setStyleAttribute(radioButtonElement, "vertical-align", "middle");
        HtmlUtil.setStyleAttribute(radioButtonElement, "margin", " 0 5px 0 0");
    }

    @Override
    public void updateSelected(Boolean selected) {
        radioButtonElement.checked = selected;
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), radioButtonElement);
    }
}
