package naga.platform.providers.gwt;

import com.google.gwt.resources.client.TextResource;
import naga.commons.services.resource.spi.ResourceService;
import naga.commons.util.async.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class GwtResourceService implements ResourceService {

    public static GwtResourceService SINGLETON = new GwtResourceService();

    private List<GwtBundle> bundles = new ArrayList<>();

    private GwtResourceService() {
    }

    void register(GwtBundle bundle) {
        bundles.add(bundle);
    }

    @Override
    public Future<String> getText(String resourcePath) {
        for (GwtBundle bundle : bundles) {
            TextResource textResource = bundle.getTextResource(resourcePath);
            if (textResource != null)
                return Future.succeededFuture(textResource.getText());
        }
        return null;
    }
}
