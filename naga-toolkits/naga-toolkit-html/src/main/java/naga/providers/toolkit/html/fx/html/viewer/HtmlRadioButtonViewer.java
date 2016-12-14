package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.RadioButton;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerBase;
import naga.toolkit.fx.spi.viewer.base.RadioButtonViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlRadioButtonViewer
        extends HtmlButtonBaseViewer<RadioButton, RadioButtonViewerBase, RadioButtonViewerMixin>
        implements RadioButtonViewerMixin, HtmlLayoutMeasurable {

    private final HTMLInputElement radioButtonElement;

    public HtmlRadioButtonViewer() {
        super(new RadioButtonViewerBase(), HtmlUtil.createLabelElement());
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
