package dev.webfx.platform.gwt.services.resource.spi.impl;

import com.google.gwt.resources.client.TextResource;
import dev.webfx.platform.shared.services.resource.spi.ResourceServiceProvider;
import dev.webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class GwtResourceServiceProvider implements ResourceServiceProvider {

    private final List<GwtResourceBundle> bundles = new ArrayList<>();

    public void register(GwtResourceBundle bundle) {
        bundles.add(bundle);
    }

    @Override
    public String toUrl(String resourcePath, Class<?> loadingClass) {
        if (resourcePath != null) {
            if (resourcePath.startsWith("/"))
                resourcePath = resourcePath.substring(1);
            else {
                String className = loadingClass.getName();
                resourcePath = className.replace(".", "/").substring(0, className.length() - loadingClass.getSimpleName().length()) + resourcePath;
            }
        }
        return resourcePath;
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
