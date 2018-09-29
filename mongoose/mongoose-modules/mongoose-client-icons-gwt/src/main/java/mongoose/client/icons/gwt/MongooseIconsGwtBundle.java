package mongoose.client.icons.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseIconsGwtBundle extends ClientBundle {

    MongooseIconsGwtBundle R = GWT.create(MongooseIconsGwtBundle.class);

    @Source("images/svg/mono/certificate.svg")
    TextResource certificateMonoSvg();

    @Source("images/svg/mono/calendar.svg")
    TextResource calendarMonoSvg();

    @Source("images/svg/mono/price-tag.svg")
    TextResource priceTagMonoSvg();

    @Source("images/svg/color/price-tag.svg")
    TextResource priceTagColorSvg();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("images/svg/mono/certificate.svg", R.certificateMonoSvg());
            registerResource("images/svg/mono/calendar.svg", R.calendarMonoSvg());
            registerResource("images/svg/mono/price-tag.svg", R.priceTagMonoSvg());
            registerResource("images/svg/color/price-tag.svg", R.priceTagColorSvg());
        }
    }
}
