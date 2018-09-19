package webfx.platform.gwt.services.resource;

import com.google.gwt.resources.client.TextResource;
import webfx.platforms.core.services.resource.spi.ResourceServiceProvider;
import webfx.platforms.core.util.async.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class GwtResourceServiceProviderImpl implements ResourceServiceProvider {

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
