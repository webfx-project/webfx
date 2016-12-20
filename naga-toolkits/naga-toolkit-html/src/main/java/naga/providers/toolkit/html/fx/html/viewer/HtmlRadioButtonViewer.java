package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRadioButtonViewer
        <N extends RadioButton, NV extends RadioButtonViewerBase<N, NV, NM>, NM extends RadioButtonViewerMixin<N, NV, NM>>

        extends HtmlButtonBaseViewer<N, NV, NM>
        implements RadioButtonViewerMixin<N, NV, NM>, HtmlLayoutMeasurable {

    private final HTMLInputElement radioButtonElement;

    public HtmlRadioButtonViewer() {
        this((NV) new RadioButtonViewerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlRadioButtonViewer(NV base, HTMLElement element) {
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
