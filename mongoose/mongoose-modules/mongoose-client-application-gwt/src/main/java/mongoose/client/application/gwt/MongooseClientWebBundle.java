package mongoose.client.application.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseClientWebBundle extends ClientBundle {

    MongooseClientWebBundle R = GWT.create(MongooseClientWebBundle.class);

    @Source("webfx/platforms/core/services/bus/client/conf/BusOptions.json")
    TextResource clientBusOptionsJson();

    final class ResourceBundle extends GwtResourceBundleBase {
        public ResourceBundle() {
            registerResource("webfx/platforms/core/services/bus/client/conf/BusOptions.json", R.clientBusOptionsJson());
        }
    }
}
