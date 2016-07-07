package naga.platform.providers.gwt;

import com.google.gwt.resources.client.TextResource;

/**
 * @author Bruno Salmon
 */
public interface GwtBundle {

    TextResource getTextResource(String resourcePath);

}
