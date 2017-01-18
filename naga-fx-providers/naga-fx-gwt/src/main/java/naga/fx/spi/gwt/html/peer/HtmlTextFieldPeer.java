package naga.fx.spi.gwt.html.peer;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import elemental2.HTMLInputElement;
import naga.commons.util.Objects;
import naga.commons.util.Strings;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.scene.control.TextField;
import emul.javafx.scene.text.Font;
import naga.fx.spi.peer.base.TextFieldPeerBase;
import naga.fx.spi.peer.base.TextFieldPeerMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextFieldPeer
        <N extends TextField, NB extends TextFieldPeerBase<N, NB, NM>, NM extends TextFieldPeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements TextFieldPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlTextFieldPeer() {
        this((NB) new TextFieldPeerBase(), HtmlUtil.createTextInput());
    }

    public HtmlTextFieldPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        inputElement.oninput = e -> {
            getNode().setText(inputElement.value);
            return null;
        };
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

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }

    @Override
    public void updateText(String text) {
        HTMLInputElement inputElement = (HTMLInputElement) getElement();
        String safeText = Strings.toSafeString(text);
        if (!Objects.areEquals(inputElement.value, safeText)) // To avoid caret position reset
            inputElement.value = safeText;
    }

    @Override
    public void updatePrompt(String prompt) {
        ((HTMLInputElement) getElement()).placeholder = Strings.toSafeString(prompt);
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }
}
