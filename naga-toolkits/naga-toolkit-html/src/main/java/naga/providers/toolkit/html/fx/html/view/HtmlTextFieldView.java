package naga.providers.toolkit.html.fx.html.view;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLInputElement;
import naga.commons.util.Strings;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.spi.view.base.TextFieldViewBase;
import naga.toolkit.fx.spi.view.base.TextFieldViewMixin;

/**
 * @author Bruno Salmon
 */
public class HtmlTextFieldView
        extends HtmlRegionView<TextField, TextFieldViewBase, TextFieldViewMixin>
        implements TextFieldViewMixin, HtmlLayoutMeasurable {

    public HtmlTextFieldView() {
        super(new TextFieldViewBase(), HtmlUtil.createTextInput());
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
        inputElement.value = Strings.toSafeString(text);
    }

    @Override
    public void updatePrompt(String prompt) {
        ((HTMLInputElement) getElement()).placeholder = prompt;
    }
}
