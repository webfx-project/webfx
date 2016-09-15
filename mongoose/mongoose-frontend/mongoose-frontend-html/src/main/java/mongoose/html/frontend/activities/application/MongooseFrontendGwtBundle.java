package mongoose.html.frontend.activities.application;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import naga.providers.platform.client.gwt.services.resource.GwtBundle;

/**
 * @author Bruno Salmon
 */
public interface MongooseFrontendGwtBundle extends ClientBundle {

    MongooseFrontendGwtBundle R = GWT.create(MongooseFrontendGwtBundle.class);

    @Source("mongoose/domainmodel/DomainModelSnapshot.lzb64json")
    TextResource lzBase64DomainModelSnapshot();

    @Source("naga/platform/client/bus/BusOptions.json")
    TextResource jsonClientBusOptions();

    @Source("mongoose/html/frontend/activities/event/fees/fees.html")
    TextResource feesHtml();

    @Source("mongoose/html/frontend/activities/event/program/program.html")
    TextResource programHtml();

    @Source("mongoose/html/frontend/activities/event/options/options.html")
    TextResource optionsHtml();

    GwtBundle B = resourcePath -> {
        switch (resourcePath) {
            case "mongoose/domainmodel/DomainModelSnapshot.lzb64json": return R.lzBase64DomainModelSnapshot();
            case "naga/platform/client/bus/BusOptions.json": return R.jsonClientBusOptions();
            default: return null;
        }
    };
}
