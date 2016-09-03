package mongoose.client.frontend.html;

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

    GwtBundle B = resourcePath -> {
        switch (resourcePath) {
            case "mongoose/domainmodel/DomainModelSnapshot.lzb64json": return R.lzBase64DomainModelSnapshot();
            case "naga/platform/client/bus/BusOptions.json": return R.jsonClientBusOptions();
            default: return null;
        }
    };
}
