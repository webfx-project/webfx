package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.nodes.controls.CheckBox;


/**
 * @author Bruno Salmon
 */
public class HtmlCheckbox extends HtmlSelectableButton<HTMLInputElement> implements CheckBox<HTMLInputElement> {

    public HtmlCheckbox() {
        this(HtmlUtil.createCheckBox());
    }

    public HtmlCheckbox(HTMLInputElement button) {
        super(button);
    }

}
