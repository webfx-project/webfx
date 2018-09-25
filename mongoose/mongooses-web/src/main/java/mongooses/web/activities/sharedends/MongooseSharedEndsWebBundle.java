package mongooses.web.activities.sharedends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseSharedEndsWebBundle extends ClientBundle {

    MongooseSharedEndsWebBundle R = GWT.create(MongooseSharedEndsWebBundle.class);

    @Source("mongooses/core/shared/domainmodel/DomainModelSnapshot.json")
    TextResource domainModelSnapshotJson();

    @Source("webfx/platforms/core/services/bus/client/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    @Source("mongooses/core/sharedends/i18n/dictionaries/en.json")
    TextResource englishDictionaryJson();

    @Source("mongooses/core/sharedends/i18n/dictionaries/fr.json")
    TextResource frenchDictionaryJson();

    @Source("images/svg/mono/certificate.svg")
    TextResource certificateMonoSvg();

    @Source("images/svg/mono/calendar.svg")
    TextResource calendarMonoSvg();

    @Source("images/svg/mono/price-tag.svg")
    TextResource priceTagMonoSvg();

    @Source("images/svg/color/price-tag.svg")
    TextResource priceTagColorSvg();

    class ResourceBundle extends GwtResourceBundleBase {
        public ResourceBundle() {
            registerResource("mongooses/core/shared/domainmodel/DomainModelSnapshot.json", R.domainModelSnapshotJson());
            registerResource("webfx/platforms/core/services/bus/client/conf/BusOptions.json", R.clientBusOptionsJson());
            registerResource("mongooses/core/sharedends/i18n/dictionaries/en.json", R.englishDictionaryJson());
            registerResource("mongooses/core/sharedends/i18n/dictionaries/fr.json", R.frenchDictionaryJson());
            registerResource("images/svg/mono/certificate.svg", R.certificateMonoSvg());
            registerResource("images/svg/mono/calendar.svg", R.calendarMonoSvg());
            registerResource("images/svg/mono/price-tag.svg", R.priceTagMonoSvg());
            registerResource("images/svg/color/price-tag.svg", R.priceTagColorSvg());
        }
    }
}
