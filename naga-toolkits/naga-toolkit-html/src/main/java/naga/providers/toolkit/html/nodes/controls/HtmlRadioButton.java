package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLInputElement;
import naga.providers.toolkit.html.HtmlUtil;
import naga.toolkit.spi.nodes.controls.RadioButton;


/**
 * @author Bruno Salmon
 */
public class HtmlRadioButton extends HtmlSelectableButton<HTMLInputElement> implements RadioButton<HTMLInputElement> {

    public HtmlRadioButton() {
        this(HtmlUtil.createRadioButton());
    }

    public HtmlRadioButton(HTMLInputElement button) {
        super(button);
    }

}
