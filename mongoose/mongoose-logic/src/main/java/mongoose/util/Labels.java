package mongoose.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Labeled;
import mongoose.entities.Label;
import mongoose.entities.impl.LabelImpl;
import mongoose.entities.markers.HasItem;
import mongoose.entities.markers.HasLabel;
import mongoose.entities.markers.HasName;
import naga.commons.util.Objects;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class Labels {

    public static Label bestLabel(Object o) {
        Label label = null;
        if (o instanceof HasLabel)
            label = ((HasLabel) o).getLabel();
        if (label == null && o instanceof HasItem)
            label = bestLabel(((HasItem) o).getItem());
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

    public static Property<String> translateLabel(Label label, I18n i18n) {
        Property<String> translation = new SimpleObjectProperty<>(instantTranslateLabel(label, i18n));
        i18n.languageProperty().addListener((observable, oldValue, newValue) -> translation.setValue(instantTranslateLabel(label, i18n)));
        return translation;
    }

    public static <T extends Labeled> T translateLabel(T labeled, Label label, I18n i18n) {
        labeled.textProperty().bind(translateLabel(label, i18n));
        return labeled;
    }

    public static String instantTranslateLabel(Label label, I18n i18n) {
        return instantTranslateLabel(label, i18n, null);
    }

    public static String instantTranslateLabel(Label label, Object language) {
        return instantTranslateLabel(label, language, null);
    }

    public static String instantTranslateLabel(Label label, I18n i18n, String keyIfNull) {
        return instantTranslateLabel(label, i18n.getLanguage(), i18n.instantTranslate(keyIfNull));
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
