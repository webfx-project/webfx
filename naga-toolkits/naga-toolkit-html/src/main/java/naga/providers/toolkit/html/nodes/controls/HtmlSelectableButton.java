package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLInputElement;
import elemental2.HTMLLabelElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.spi.nodes.controls.SelectableButton;

/**
 * @author Bruno Salmon
 */
 class HtmlSelectableButton extends HtmlButtonBase<HTMLLabelElement> implements SelectableButton {

    private final HTMLInputElement button;

    HtmlSelectableButton(HTMLInputElement button) {
        this(button, HtmlUtil.createLabelElement());
    }

    HtmlSelectableButton(HTMLInputElement button, HTMLLabelElement label) {
        super(label);
        this.button = button;
        HtmlUtil.appendStyle(label, "margin-top: 0; margin-bottom: 0");
        HtmlUtil.appendStyle(button, "vertical-align: middle; margin: 0 5px 0 0");
        selectedProperty.setValue(button.checked);
        button.onclick = event -> {
            selectedProperty.setValue(button.checked);
            return null;
        };
        selectedProperty.addListener((observable, oldValue, newValue) -> button.checked = newValue);
    }

    @Override
    protected void updateHtmlContent() {
        super.updateHtmlContent();
        HtmlUtil.appendFirstChild(node, button);
    }

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

}
