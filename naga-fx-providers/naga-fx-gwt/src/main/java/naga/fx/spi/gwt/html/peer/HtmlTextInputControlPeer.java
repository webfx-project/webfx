package naga.fx.spi.gwt.html.peer;

import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLTextAreaElement;
import emul.javafx.scene.control.TextInputControl;
import emul.javafx.scene.text.Font;
import naga.commons.util.Objects;
import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.TextInputControlPeerBase;
import naga.fx.spi.peer.base.TextInputControlPeerMixin;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlTextInputControlPeer
        <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements TextInputControlPeerMixin<N, NB, NM> {

    public HtmlTextInputControlPeer(NB base, HTMLElement element) {
        super(base, element);
        getElement().oninput = e -> {
            getNode().setText(getValue());
            return null;
        };
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }

    @Override
    public void updateText(String text) {
        String safeText = Strings.toSafeString(text);
        if (!Objects.areEquals(getValue(), safeText)) // To avoid caret position reset
            setValue(safeText);
    }

    @Override
    public void updatePrompt(String prompt) {
        setPlaceholder(Strings.toSafeString(prompt));
    }

    protected String getValue() {
        HTMLElement element = getElement();
        if (element instanceof HTMLInputElement)
            return ((HTMLInputElement) element).value;
        if (element instanceof HTMLTextAreaElement)
            return ((HTMLTextAreaElement) element).value;
        return null;
    }

    protected void setValue(String value) {
        HTMLElement element = getElement();
        if (element instanceof HTMLInputElement)
            ((HTMLInputElement) element).value = value;
        else if (element instanceof HTMLTextAreaElement)
            ((HTMLTextAreaElement) element).value = value;
    }

    protected void setPlaceholder(String placeholder) {
        HTMLElement element = getElement();
        if (element instanceof HTMLInputElement)
            ((HTMLInputElement) element).placeholder = placeholder;
        else if (element instanceof HTMLTextAreaElement)
            setElementAttribute("placeholder", placeholder);
    }

    /*
    The behaviour when setting the style width/height on a text input seems different than on other html elements.
    On other html elements (ex: a button) this will size the outer visual box (including padding and border) to the
    specified width/height. On a text input, this will size the inner visual box (excluding the padding and border).
*/

    @Override
    public void updateWidth(Double width) { // The width parameter actually refers to the outer width
        // To get the desired visual width, we need to actually set the style width to the inner width (and not the
        // outer width as usual) by removing the padding and border (on left and right).
        CSSStyleDeclaration cs = HtmlUtil.getComputedStyle(getElement());
        double innerWidth = width - sumPx(cs.paddingLeft, cs.paddingRight, cs.borderLeft, cs.borderRight);
        super.updateWidth(innerWidth);
    }

    @Override
    public void updateHeight(Double height) { // The height parameter actually refers to the outer height
        // To get the desired visual height, we need to actually set the style width to the inner height (and not the
        // outer height as usual) by removing the padding and border (on top and bottom).
        CSSStyleDeclaration cs = HtmlUtil.getComputedStyle(getElement());
        double innerHeight = height - sumPx(cs.paddingTop, cs.paddingBottom, cs.borderTop, cs.borderBottom);
        super.updateHeight(innerHeight);
    }

    private static double sumPx(Object... values) {
        double result = 0;
        for (Object value : values) {
            String s = value.toString();
            int i = s.indexOf("px");
            if (i > 0)
                result += Double.parseDouble(s.substring(0, i));
        }
        return result;
    }

}
