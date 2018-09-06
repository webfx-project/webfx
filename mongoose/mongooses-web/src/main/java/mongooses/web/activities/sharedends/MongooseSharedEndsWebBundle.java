package mongooses.web.activities.sharedends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtBundle;

/**
 * @author Bruno Salmon
 */
public interface MongooseSharedEndsWebBundle extends ClientBundle {

    MongooseSharedEndsWebBundle R = GWT.create(MongooseSharedEndsWebBundle.class);

    @Source("mongooses/core/domainmodel/DomainModelSnapshot.json")
    TextResource domainModelSnapshotJson();

    @Source("webfx/platforms/core/services/bus/client/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    @Source("mongooses/core/dictionaries/en.json")
    TextResource englishDictionaryJson();

    @Source("mongooses/core/dictionaries/fr.json")
    TextResource frenchDictionaryJson();

    @Source("images/svg/mono/certificate.svg")
    TextResource certificateMonoSvg();

    @Source("images/svg/mono/calendar.svg")
    TextResource calendarMonoSvg();

    @Source("images/svg/mono/price-tag.svg")
    TextResource priceTagMonoSvg();

    @Source("images/svg/color/price-tag.svg")
    TextResource priceTagColorSvg();

    GwtBundle B = resourcePath -> {
        switch (resourcePath) {
            case "mongooses/core/domainmodel/DomainModelSnapshot.json": return R.domainModelSnapshotJson();
            case "webfx/platforms/core/services/bus/client/conf/BusOptions.json": return R.clientBusOptionsJson();
            case "mongooses/core/dictionaries/en.json": return R.englishDictionaryJson();
            case "mongooses/core/dictionaries/fr.json": return R.frenchDictionaryJson();
            case "images/svg/mono/certificate.svg": return R.certificateMonoSvg();
            case "images/svg/mono/calendar.svg": return R.calendarMonoSvg();
            case "images/svg/mono/price-tag.svg": return R.priceTagMonoSvg();
            case "images/svg/color/price-tag.svg": return R.priceTagColorSvg();
            default: return null;
        }
    };
}
