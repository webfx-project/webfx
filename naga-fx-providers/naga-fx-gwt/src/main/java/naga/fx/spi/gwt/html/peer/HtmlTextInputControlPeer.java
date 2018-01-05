package naga.fx.spi.gwt.html.peer;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLTextAreaElement;
import emul.javafx.scene.control.TextInputControl;
import emul.javafx.scene.text.Font;
import naga.fx.spi.peer.base.TextInputControlPeerBase;
import naga.fx.spi.peer.base.TextInputControlPeerMixin;
import naga.util.Booleans;
import naga.util.Objects;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlTextInputControlPeer
        <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

        extends HtmlControlPeer<N, NB, NM>
        implements TextInputControlPeerMixin<N, NB, NM> {

    public HtmlTextInputControlPeer(NB base, HTMLElement textInputElement) {
        super(base, textInputElement);
        prepareDomForAdditionalSkinChildren();
        textInputElement.oninput = e -> {
            getNode().setText(getValue());
            return null;
        };
    /*
    The behaviour when setting the style width/height on a text input seems different than on other html elements.
    On other html elements (ex: a button) this will size the outer visual box (including padding and border) to the
    specified width/height. On a text input, this will size the inner visual box (excluding the padding and border).
*/
        subtractCssPaddingBorderWhenUpdatingSize = true;
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

    @Override
    public void updateEditable(Boolean editable) {
        setElementAttribute(getElement(),"readonly", Booleans.isFalse(editable) ? "true" : null);
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
            setElementAttribute(element, "placeholder", placeholder);
    }
}
