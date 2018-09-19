package webfx.platform.gwt.services.resource;

import com.google.gwt.resources.client.TextResource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class GwtResourceBundleBase implements GwtResourceBundle {

    private final static Map<String, TextResource> resources = new HashMap<>();

    protected void registerResource(String resourcePath, TextResource textResource) {
        resources.put(resourcePath, textResource);
    }

    @Override
    public TextResource getTextResource(String resourcePath) {
        return resources.get(resourcePath);
    }

    @Override
    public Iterable<String> resourcePathsForLogging() {
        return resources.keySet();
    }
}
