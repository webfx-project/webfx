package naga.providers.toolkit.html.drawing.html.view;

import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.scene.control.CheckBox;
import naga.toolkit.drawing.spi.view.base.CheckBoxViewBase;
import naga.toolkit.drawing.spi.view.base.CheckBoxViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCheckBoxView
        extends HtmlButtonBaseView<CheckBox, CheckBoxViewBase, CheckBoxViewMixin>
        implements CheckBoxViewMixin, HtmlLayoutMeasurable {

    private final HTMLInputElement checkBox;

    public HtmlCheckBoxView() {
        super(new CheckBoxViewBase(), HtmlUtil.createLabelElement());
        checkBox = HtmlUtil.createCheckBox();
        checkBox.onclick = event -> {
            getNode().setSelected(checkBox.checked);
            return null;
        };
        HtmlUtil.setStyleAttribute(getElement(), "margin-top", "0");
        HtmlUtil.setStyleAttribute(getElement(), "margin-bottom", "0");
        HtmlUtil.setStyleAttribute(checkBox, "vertical-align", "middle");
        HtmlUtil.setStyleAttribute(checkBox, "margin", " 0 5px 0 0");
    }

    @Override
    public void updateSelected(Boolean selected) {
        checkBox.checked = selected;
    }

    @Override
    public void updateText(String text) {
        super.updateText(text);
        HtmlUtil.appendFirstChild(getElement(), checkBox);
    }
}
