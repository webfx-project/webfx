package mongoose.activities.shared.application.web;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import naga.providers.platform.client.gwt.services.resource.GwtBundle;

/**
 * @author Bruno Salmon
 */
public interface WebMongooseBundle extends ClientBundle {

    WebMongooseBundle R = GWT.create(WebMongooseBundle.class);

    @Source("mongoose/domainmodel/DomainModelSnapshot.json")
    TextResource domainModelSnapshotJson();

    @Source("naga/platform/client/bus/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    @Source("mongoose/dictionaries/en.json")
    TextResource englishDictionaryJson();

    @Source("mongoose/dictionaries/fr.json")
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
            case "mongoose/domainmodel/DomainModelSnapshot.json": return R.domainModelSnapshotJson();
            case "naga/platform/client/bus/conf/BusOptions.json": return R.clientBusOptionsJson();
            case "mongoose/dictionaries/en.json": return R.englishDictionaryJson();
            case "mongoose/dictionaries/fr.json": return R.frenchDictionaryJson();
            case "images/svg/mono/certificate.svg": return R.certificateMonoSvg();
            case "images/svg/mono/calendar.svg": return R.calendarMonoSvg();
            case "images/svg/mono/price-tag.svg": return R.priceTagMonoSvg();
            case "images/svg/color/price-tag.svg": return R.priceTagColorSvg();
            default: return null;
        }
    };
}
