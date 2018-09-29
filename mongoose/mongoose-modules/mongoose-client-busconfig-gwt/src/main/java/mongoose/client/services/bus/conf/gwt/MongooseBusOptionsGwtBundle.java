package mongoose.client.services.bus.conf.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseBusOptionsGwtBundle extends ClientBundle {

    MongooseBusOptionsGwtBundle R = GWT.create(MongooseBusOptionsGwtBundle.class);

    @Source("webfx/platforms/core/services/bus/client/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("webfx/platforms/core/services/bus/client/conf/BusOptions.json", R.clientBusOptionsJson());
        }
    }
}
