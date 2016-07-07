package mongoose.backend.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import naga.platform.providers.gwt.GwtBundle;

/**
 * @author Bruno Salmon
 */
public interface MongooseBackendGwtBundle extends ClientBundle {

    MongooseBackendGwtBundle R = GWT.create(MongooseBackendGwtBundle.class);

    @Source("mongoose/domainmodel/DomainModelSnapshot.lzb64json")
    TextResource lzBase64DomainModelSnapshot();

    GwtBundle B = resourcePath -> {
        switch (resourcePath) {
            case "mongoose/domainmodel/DomainModelSnapshot.lzb64json": return R.lzBase64DomainModelSnapshot();
            default: return null;
        }
    };
}
