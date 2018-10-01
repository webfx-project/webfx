package webfx.platform.shared.services.resource.spi.impl.gwt;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Bruno Salmon
 */
public interface GwtResourceBundle {

    TextResource getTextResource(String resourcePath);

    Iterable<String> resourcePathsForLogging();
}
