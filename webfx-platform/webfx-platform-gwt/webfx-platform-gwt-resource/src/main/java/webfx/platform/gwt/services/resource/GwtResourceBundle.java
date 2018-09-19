package webfx.platform.gwt.services.resource;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Bruno Salmon
 */
public interface GwtResourceBundle {

    TextResource getTextResource(String resourcePath);

    Iterable<String> resourcePathsForLogging();
}
