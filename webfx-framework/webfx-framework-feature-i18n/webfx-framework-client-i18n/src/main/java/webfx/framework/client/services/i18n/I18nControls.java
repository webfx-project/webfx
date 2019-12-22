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

    public static Node getI18nGraphic(Object i18nKey) {
        return createI18nGraphic(I18n.getI18nGraphicUrl(i18nKey));
    }

    public static Node createI18nGraphic(String graphicUrl) {
        return I18n.getProvider().createI18nGraphic(graphicUrl);
    }

    public static ObservableObjectValue<Node> i18nGraphicProperty(Object i18nKey) {
        return new SimpleObjectProperty<Node/*GWT*/>() { // TODO: instance should be unique per key
            // Keeping a strong reference of the graphic url property (important to avoid GC since I18n uses weak references)
            private final ObservableStringValue graphicUrlProperty = I18n.i18nGraphicUrlProperty(i18nKey);
            {
                set(createI18nGraphic(graphicUrlProperty.get()));
                graphicUrlProperty.addListener((observableValue, oldUrl, newUrl) -> set(createI18nGraphic(newUrl)));
            }
        };
    }

    public static void bindI18nGraphicProperty(Property<Node> graphicNodeProperty, Object i18nKey) {
        graphicNodeProperty.bind(i18nGraphicProperty(i18nKey));
    }

    public static <T extends Text> T bindI18nProperties(T text, Object i18nKey) {
        I18n.bindI18nTextProperty(text.textProperty(), i18nKey);
        return text;
    }

    public static <T extends Labeled> T bindI18nProperties(T labeled, Object i18nKey) {
        I18n.bindI18nTextProperty(labeled.textProperty(), i18nKey);
        return labeled;
    }

    public static <T extends TextInputControl> T bindI18nProperties(T textInputControl, Object i18nKey) {
        I18n.bindI18nPromptProperty(textInputControl.promptTextProperty(), i18nKey);
        return textInputControl;
    }

    public static <T extends Tab> T bindI18nProperties(T tab, Object i18nKey) {
        I18n.bindI18nTextProperty(tab.textProperty(), i18nKey);
        bindI18nGraphicProperty(tab.graphicProperty(), i18nKey);
        return tab;
    }

    public static <T extends Labeled> T setI18nProperties(T labeled, Object i18nKey) {
        labeled.setText(I18n.getI18nText(i18nKey));
        labeled.setGraphic(getI18nGraphic(i18nKey));
        return labeled;
    }
}
