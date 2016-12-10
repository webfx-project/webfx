package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.toolkit.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCheckBoxViewer
        extends HtmlButtonBaseViewer<CheckBox, CheckBoxViewerBase, CheckBoxViewerMixin>
        implements CheckBoxViewerMixin, HtmlLayoutMeasurable {

    private final HTMLInputElement checkBox;

    public HtmlCheckBoxViewer() {
        super(new CheckBoxViewerBase(), HtmlUtil.createLabelElement());
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
