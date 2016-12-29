package naga.fx.spi.gwt.html.viewer;

import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.scene.control.CheckBox;
import naga.fx.spi.viewer.base.CheckBoxViewerBase;
import naga.fx.spi.viewer.base.CheckBoxViewerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlCheckBoxViewer
        <N extends CheckBox, NB extends CheckBoxViewerBase<N, NB, NM>, NM extends CheckBoxViewerMixin<N, NB, NM>>

        extends HtmlButtonBaseViewer<N, NB, NM>
        implements CheckBoxViewerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    private final HTMLInputElement checkBox;

    public HtmlCheckBoxViewer() {
        this((NB) new CheckBoxViewerBase(), HtmlUtil.createLabelElement());
    }

    public HtmlCheckBoxViewer(NB base, HTMLElement element) {
        super(base, element);
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
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(getElement(), checkBox);
    }
}
