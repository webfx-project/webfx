package webfx.framework.client.services.i18n.spi;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitter;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.I18nPart;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.collection.Collections;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public interface I18nProvider {

    default Collection<Object> getSupportedLanguages() {
        return Collections.map(getProvidedInstantiators(), i -> i.emitLanguageRequest().getLanguage());
    }

    default Collection<ChangeLanguageRequestEmitter> getProvidedInstantiators() {
        return Collections.listOf(ServiceLoader.load(ChangeLanguageRequestEmitter.class));
    }

    ObjectProperty<Object> languageProperty();
    default Object getLanguage() { return languageProperty().getValue(); }
    default void setLanguage(Object language) { languageProperty().setValue(language); }

    ObservableObjectValue<Dictionary> dictionaryProperty();
    default Dictionary getDictionary() {
        return dictionaryProperty().getValue();
    }

    Object getDefaultLanguage();
    Dictionary getDefaultDictionary();

    default String getI18nText(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.TEXT);
    }

    default ObservableStringValue i18nTextProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.TEXT);
    }

    default String getI18nGraphicUrl(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.GRAPHIC);
    }

    default ObservableStringValue i18nGraphicUrlProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.GRAPHIC);
    }

    default String getI18nPrompt(Object i18nKey) {
        return getI18nPartValue(i18nKey, I18nPart.PROMPT);
    }

    default ObservableStringValue i18nPromptProperty(Object i18nKey) {
        return i18nPartProperty(i18nKey, I18nPart.PROMPT);
    }

    ObservableStringValue i18nPartProperty(Object i18nKey, I18nPart part);

    default String getI18nPartValue(Object i18nKey, I18nPart part) {
        return getI18nPartValue(i18nKey, part, false);
    }

    default String getI18nPartValue(Object i18nKey, I18nPart part, boolean skipPrefixOrSuffix) {
        Dictionary dictionary = getDictionary();
        String partTranslation = getI18nPartValue(i18nKey, part, dictionary, skipPrefixOrSuffix);
        if (partTranslation == null) {
            Dictionary defaultDictionary = getDefaultDictionary();
            if (dictionary != defaultDictionary && defaultDictionary != null)
                partTranslation = getI18nPartValue(i18nKey, part, defaultDictionary, skipPrefixOrSuffix);
            if (partTranslation == null) {
                scheduleMessageLoading(i18nKey, true);
                if (part == I18nPart.TEXT)
                    partTranslation = whatToReturnWhenI18nTextIsNotFound(i18nKey);
            }
        }
        return partTranslation;
    }

    default String getI18nPartValue(Object i18nKey, I18nPart part, Dictionary dictionary, boolean skipPrefixOrSuffix) {
        String partTranslation = null;
        if (dictionary != null && i18nKey != null) {
            partTranslation = dictionary.getI18nPartValue(i18nKey, part);
            if (partTranslation == null && !skipPrefixOrSuffix) {
                String sKey = Strings.asString(i18nKey);
                int length = Strings.length(sKey);
                if (length > 1) {
                    int index = 0;
                    while (index < length && !Character.isLetterOrDigit(sKey.charAt(index)))
                        index++;
                    if (index > 0) {
                        String prefix = sKey.substring(0, index);
                        switch (prefix) {
                            case "<<":
                                partTranslation = getI18nPartValue(sKey.substring(prefix.length(), length), part, dictionary, true);
                                if (partTranslation != null && part == I18nPart.TEXT)
                                    partTranslation = getI18nPartValue(prefix, part, true) + partTranslation;
                        }
                    }
                }
                if (partTranslation == null && length > 1) {
                    int index = length;
                    while (index > 0 && !Character.isLetterOrDigit(sKey.charAt(index - 1)))
                        index--;
                    if (index < length) {
                        String suffix = sKey.substring(index, length);
                        switch (suffix) {
                            case ":":
                            case "?":
                            case ">>":
                            case "...":
                                partTranslation = getI18nPartValue(sKey.substring(0, length - suffix.length()), part, dictionary, true);
                                if (partTranslation != null && part == I18nPart.TEXT)
                                    partTranslation = partTranslation + getI18nPartValue(suffix, part, true);
                        }
                    }
                }
            }
        }
        return partTranslation;
    }

    default String whatToReturnWhenI18nTextIsNotFound(Object i18nKey) {
        return Strings.toString(i18nKey);
    }

    void scheduleMessageLoading(Object i18nKey, boolean inDefaultLanguage);

    default I18nProvider bindI18nTextProperty(Property<String> textProperty, Object i18nKey) {
        textProperty.bind(i18nTextProperty(i18nKey));
        return this;
    }

    default I18nProvider bindPromptProperty(Property<String> promptProperty, Object i18nKey) {
        promptProperty.bind(i18nPromptProperty(i18nKey));
        return this;
    }

    default Node createI18nGraphic(String graphicUrl) {
        return graphicUrl == null || "".equals(graphicUrl) ? null : new ImageView(graphicUrl);
    }

}
