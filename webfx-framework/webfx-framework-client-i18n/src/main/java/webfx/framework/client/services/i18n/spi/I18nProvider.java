package webfx.framework.client.services.i18n.spi;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import webfx.framework.client.operations.i18n.ChangeLanguageRequestEmitter;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.framework.client.services.i18n.TranslationPart;
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

    default String instantTranslateText(Object i18nKey) {
        return instantTranslatePart(i18nKey, TranslationPart.TEXT);
    }

    default ObservableStringValue observableText(Object i18nKey) {
        return observablePart(i18nKey, TranslationPart.TEXT);
    }

    default String instantTranslateGraphic(Object i18nKey) {
        return instantTranslatePart(i18nKey, TranslationPart.GRAPHIC);
    }

    default ObservableStringValue observableGraphic(Object i18nKey) {
        return observablePart(i18nKey, TranslationPart.GRAPHIC);
    }

    default String instantTranslatePrompt(Object i18nKey) {
        return instantTranslatePart(i18nKey, TranslationPart.PROMPT);
    }

    default ObservableStringValue observablePrompt(Object i18nKey) {
        return observablePart(i18nKey, TranslationPart.PROMPT);
    }

    ObservableStringValue observablePart(Object i18nKey, TranslationPart part);

    default String instantTranslatePart(Object i18nKey, TranslationPart part) {
        return instantTranslatePart(i18nKey, part, false);
    }

    default String instantTranslatePart(Object i18nKey, TranslationPart part, boolean skipPrefixOrSuffix) {
        Dictionary dictionary = getDictionary();
        String partTranslation = instantTranslatePart(i18nKey, part, dictionary, skipPrefixOrSuffix);
        if (partTranslation == null) {
            Dictionary defaultDictionary = getDefaultDictionary();
            if (dictionary != defaultDictionary && defaultDictionary != null)
                partTranslation = instantTranslatePart(i18nKey, part, defaultDictionary, skipPrefixOrSuffix);
            if (partTranslation == null) {
                scheduleMessageLoading(i18nKey, true);
                if (part == TranslationPart.TEXT)
                    partTranslation = whatToReturnWhenTextTranslationIsNotFound(i18nKey);
            }
        }
        return partTranslation;
    }

    default String instantTranslatePart(Object i18nKey, TranslationPart part, Dictionary dictionary, boolean skipPrefixOrSuffix) {
        String partTranslation = null;
        if (dictionary != null && i18nKey != null) {
            partTranslation = dictionary.getPartTranslation(i18nKey, part);
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
                                partTranslation = instantTranslatePart(sKey.substring(prefix.length(), length), part, dictionary, true);
                                if (partTranslation != null && part == TranslationPart.TEXT)
                                    partTranslation = instantTranslatePart(prefix, part, true) + partTranslation;
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
                                partTranslation = instantTranslatePart(sKey.substring(0, length - suffix.length()), part, dictionary, true);
                                if (partTranslation != null && part == TranslationPart.TEXT)
                                    partTranslation = partTranslation + instantTranslatePart(suffix, part, true);
                        }
                    }
                }
            }
        }
        return partTranslation;
    }

    default String whatToReturnWhenTextTranslationIsNotFound(Object i18nKey) {
        return Strings.toString(i18nKey);
    }

    void scheduleMessageLoading(Object i18nKey, boolean inDefaultLanguage);

    default I18nProvider translateTextProperty(Property<String> textProperty, Object i18nKey) {
        textProperty.bind(observableText(i18nKey));
        return this;
    }

    default I18nProvider translatePromptProperty(Property<String> promptProperty, Object i18nKey) {
        promptProperty.bind(observablePrompt(i18nKey));
        return this;
    }

    default Node createGraphicNode(String graphicUrl) {
        return graphicUrl == null || "".equals(graphicUrl) ? null : new ImageView(graphicUrl);
    }

}
