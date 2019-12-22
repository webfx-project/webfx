package webfx.framework.client.services.i18n.spi.impl;

import webfx.framework.client.services.i18n.spi.HasDictionaryMessageKey;

public class I18nSubKey implements HasDictionaryMessageKey {

    private final Object subMessageKey;
    private final Object parentI18nKey;

    public I18nSubKey(Object subMessageKey, Object parentI18nKey) {
        this.subMessageKey = subMessageKey;
        this.parentI18nKey = parentI18nKey;
    }

    public Object getParentI18nKey() {
        return parentI18nKey;
    }

    @Override
    public Object getDictionaryMessageKey() {
        return subMessageKey;
    }
}
