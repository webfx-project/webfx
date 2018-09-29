package mongoose.shared.domain.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.GwtResourceBundleBase;

/**
 * @author Bruno Salmon
 */
public interface MongooseDomainSnapshotGwtBundle extends ClientBundle {

    MongooseDomainSnapshotGwtBundle R = GWT.create(MongooseDomainSnapshotGwtBundle.class);

    @Source("mongoose/shared/domainmodel/DomainModelSnapshot.json")
    TextResource domainModelSnapshotJson();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("mongoose/shared/domainmodel/DomainModelSnapshot.json", R.domainModelSnapshotJson());
        }
    }
}
