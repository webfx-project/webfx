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

    @Source("images/svg/mono/certificate.svg")
    TextResource certificateMonoSvg();

    @Source("images/svg/mono/calendar.svg")
    TextResource calendarMonoSvg();

    @Source("images/svg/mono/price-tag.svg")
    TextResource priceTagMonoSvg();

    @Source("images/svg/color/price-tag.svg")
    TextResource priceTagColorSvg();

    final class ResourceBundle extends GwtResourceBundleBase {
        public ResourceBundle() {
            registerResource("webfx/platforms/core/services/bus/client/conf/BusOptions.json", R.clientBusOptionsJson());
            registerResource("images/svg/mono/certificate.svg", R.certificateMonoSvg());
            registerResource("images/svg/mono/calendar.svg", R.calendarMonoSvg());
            registerResource("images/svg/mono/price-tag.svg", R.priceTagMonoSvg());
            registerResource("images/svg/color/price-tag.svg", R.priceTagColorSvg());
        }
    }
}
