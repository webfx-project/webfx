package naga.providers.toolkit.html.nodes.controls;

import elemental2.Element;
import elemental2.HTMLInputElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.nodes.controls.SelectableButton;

/**
 * @author Bruno Salmon
 */
public class HtmlSelectableButton<N extends Element> extends HtmlButtonBase<N> implements SelectableButton<N> {

    public HtmlSelectableButton(N button) {
        super(button);
        if (button instanceof HTMLInputElement) {
            HTMLInputElement input = (HTMLInputElement) button;
            selectedProperty.setValue(input.checked);
            button.onclick = event -> {
                selectedProperty.setValue(input.checked);
                return null;
            };
            selectedProperty.addListener((observable, oldValue, newValue) -> input.checked = newValue);
        }
    }

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }

}
