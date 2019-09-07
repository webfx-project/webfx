package webfx.framework.client.services.i18n;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.Tab;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

public final class I18nControls {

    public static Node instantTranslateGraphicNode(Object i18nKey) {
        return createGraphicNode(I18n.instantTranslateGraphic(i18nKey));
    }

    public static Node createGraphicNode(String graphicUrl) {
        return I18n.getProvider().createGraphicNode(graphicUrl);
    }

    public static ObservableObjectValue<Node> observableGraphicNode(Object i18nKey) {
        return new SimpleObjectProperty<Node/*GWT*/>() { // TODO: instance should be unique per key
            // Keeping a strong reference of the graphic url property (important to avoid GC since I18n uses weak references)
            private final ObservableStringValue graphicUrlProperty = I18n.observableGraphic(i18nKey);
            {
                set(createGraphicNode(graphicUrlProperty.get()));
                graphicUrlProperty.addListener((observableValue, oldUrl, newUrl) -> set(createGraphicNode(newUrl)));
            }
        };
    }

    public static void translateGraphicNodeProperty(Property<Node> graphicNodeProperty, Object i18nKey) {
        graphicNodeProperty.bind(observableGraphicNode(i18nKey));
    }

    public static <T extends Text> T translateText(T text, Object i18nKey) {
        I18n.translateTextProperty(text.textProperty(), i18nKey);
        return text;
    }

    public static <T extends Labeled> T translateLabeled(T labeled, Object i18nKey) {
        I18n.translateTextProperty(labeled.textProperty(), i18nKey);
        return labeled;
    }

    public static <T extends TextInputControl> T translatePromptText(T textInputControl, Object i18nKey) {
        I18n.translateTextProperty(textInputControl.promptTextProperty(), i18nKey);
        return textInputControl;
    }

    public static <T extends Tab> T translateTab(T tab, Object i18nKey) {
        I18n.translateTextProperty(tab.textProperty(), i18nKey);
        translateGraphicNodeProperty(tab.graphicProperty(), i18nKey);
        return tab;
    }

    public static <T extends Labeled> T instantTranslateLabeled(T labeled, Object i18nKey) {
        labeled.setText(I18n.instantTranslateText(i18nKey));
        labeled.setGraphic(instantTranslateGraphicNode(i18nKey));
        return labeled;
    }
}
