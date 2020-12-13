package dev.webfx.platform.gwt.services.resource.spi.impl;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Bruno Salmon
 */
public interface GwtResourceBundle {

    TextResource getTextResource(String resourcePath);

    Iterable<String> resourcePathsForLogging();
}
