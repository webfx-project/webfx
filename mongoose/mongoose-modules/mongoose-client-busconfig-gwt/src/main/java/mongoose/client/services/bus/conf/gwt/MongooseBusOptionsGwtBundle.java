package mongoose.client.services.bus.conf.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseBusOptionsGwtBundle extends ClientBundle {

    MongooseBusOptionsGwtBundle R = GWT.create(MongooseBusOptionsGwtBundle.class);

    @Source("webfx/platform/client/services/websocketbus/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("webfx/platform/client/services/websocketbus/conf/BusOptions.json", R.clientBusOptionsJson());
        }
    }
}
