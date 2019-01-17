package mongoose.shared.domainmodel.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.shared.services.resource.spi.impl.gwt.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseDomainModelSnapshotGwtBundle extends ClientBundle {

    MongooseDomainModelSnapshotGwtBundle R = GWT.create(MongooseDomainModelSnapshotGwtBundle.class);

    @Source("mongoose/shared/domainmodel/DomainModelSnapshot.json")
    TextResource domainModelSnapshotJson();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("mongoose/shared/domainmodel/DomainModelSnapshot.json", R.domainModelSnapshotJson());
        }
    }
}
