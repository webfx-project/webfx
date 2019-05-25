package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLTextAreaElement;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextInputControlPeerBase;
import webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base.TextInputControlPeerMixin;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.Strings;

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
        // Restoring pointer events (were disabled by prepareDomForAdditionalSkinChildren()) in case the graphic is clickable (ex: radio button)
        HtmlUtil.setStyleAttribute(getChildrenContainer(), "pointer-events", "auto");
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
        if (!getNode().getStyleClass().contains("material"))
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
