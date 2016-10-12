package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLInputElement;
import elemental2.HTMLLabelElement;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.nodes.controls.CheckBox;


/**
 * @author Bruno Salmon
 */
public class HtmlCheckbox extends HtmlSelectableButton implements CheckBox<HTMLLabelElement> {

    public HtmlCheckbox() {
        this(HtmlUtil.createCheckBox());
    }

    public HtmlCheckbox(HTMLInputElement button) {
        super(button);
    }

}
