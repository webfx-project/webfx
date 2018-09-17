package webfx.platform.gwt.services.appcontainer;

import com.google.gwt.core.client.EntryPoint;
import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;

/**
 * @author Bruno Salmon
 */
public class GwtApplicationContainerProvider implements ApplicationContainerProvider, EntryPoint {

    @Override
    public void onModuleLoad() {
        ApplicationContainer.start(this, null);
    }
}
