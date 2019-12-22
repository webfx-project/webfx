package mongoose.client.entities.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Labeled;
import mongoose.shared.entities.Label;
import mongoose.shared.entities.impl.LabelImpl;
import mongoose.shared.entities.markers.HasItem;
import mongoose.shared.entities.markers.HasLabel;
import mongoose.shared.entities.markers.HasName;
import webfx.framework.client.services.i18n.I18n;
import webfx.platform.shared.util.Objects;

/**
 * @author Bruno Salmon
 */
public final class Labels {

    public static Label bestLabel(Object o) {
        Label label = null;
        if (o instanceof HasLabel)
            label = ((HasLabel) o).getLabel();
        if (label == null && o instanceof HasItem)
            label = bestLabel(((HasItem) o).getItem());
        if (label == null && o instanceof Label)
            label = (Label) o;
        return label;
    }

    public static String bestName(Object o) {
        String name = null;
        if (o instanceof HasName)
            name = ((HasName) o).getName();
        if (name == null && o instanceof HasItem)
            name = bestName(((HasItem) o).getItem());
        return name;
    }

    public static Label bestLabelOrName(Object o) {
        Label label = bestLabel(o);
        String name = bestName(o);
        if (name != null) {
            if (label == null)
                label = new LabelImpl(null, null);
            label.setFieldValue("name", name);
        }
        return label;
    }

    public static Property<String> translateLabel(Label label) {
        Property<String> translation = new SimpleObjectProperty<>(instantTranslateLabel(label));
        I18n.languageProperty().addListener((observable, oldValue, newValue) -> translation.setValue(instantTranslateLabel(label)));
        return translation;
    }

    public static <T extends Labeled> T translateLabel(T labeled, Label label) {
        labeled.textProperty().bind(translateLabel(label));
        return labeled;
    }

    public static String instantTranslate(Object o) {
        return instantTranslateLabel(bestLabelOrName(o));
    }

    public static String instantTranslateLabel(Label label) {
        return instantTranslateLabel(label, null);
    }

    public static String instantTranslateLabel(Label label, Object language) {
        return instantTranslateLabel(label, language, null);
    }

    public static String instantTranslateLabel(Label label, String keyIfNull) {
        return instantTranslateLabel(label, I18n.getLanguage(), I18n.getI18nText(keyIfNull));
    }

    public static String instantTranslateLabel(Label label, Object language, String translationIfNull) {
        if (label == null)
            return translationIfNull;
        String translation = label.getStringFieldValue(language);
        if (translation == null)
            translation = Objects.coalesce(label.getStringFieldValue("name"), label.getEn(), label.getFr(), label.getEs(), label.getPt(), label.getDe());
        return translation != null ? translation : translationIfNull;
    }
}
