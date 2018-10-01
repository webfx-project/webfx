package webfx.platform.shared.services.resource.spi.impl.gwt;

import com.google.gwt.resources.client.TextResource;
import webfx.platform.shared.services.resource.spi.ResourceServiceProvider;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class GwtResourceServiceProvider implements ResourceServiceProvider {

    private List<GwtResourceBundle> bundles = new ArrayList<>();

    public void register(GwtResourceBundle bundle) {
        bundles.add(bundle);
    }

    @Override
    public Future<String> getText(String resourcePath) {
        for (GwtResourceBundle bundle : bundles) {
            TextResource textResource = bundle.getTextResource(resourcePath);
            if (textResource != null)
                return Future.succeededFuture(textResource.getText());
        }
        return Future.succeededFuture(null);
    }
}
