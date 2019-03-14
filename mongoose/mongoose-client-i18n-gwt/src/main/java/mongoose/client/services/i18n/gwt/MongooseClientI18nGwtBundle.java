package mongoose.client.services.i18n.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseClientI18nGwtBundle extends ClientBundle {

    MongooseClientI18nGwtBundle R = GWT.create(MongooseClientI18nGwtBundle.class);

    @Source("mongoose/client/services/i18n/dictionaries/en.json")
    TextResource englishDictionaryJson();

    @Source("mongoose/client/services/i18n/dictionaries/fr.json")
    TextResource frenchDictionaryJson();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("mongoose/client/services/i18n/dictionaries/en.json", R.englishDictionaryJson());
            registerResource("mongoose/client/services/i18n/dictionaries/fr.json", R.frenchDictionaryJson());
        }
    }
}
