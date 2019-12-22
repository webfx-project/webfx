package webfx.platform.gwt.services.appcontainer.spi.impl;

import com.google.gwt.core.client.EntryPoint;
import webfx.platform.shared.services.appcontainer.ApplicationContainer;
import webfx.platform.shared.services.appcontainer.spi.ApplicationContainerProvider;

/**
 * @author Bruno Salmon
 */
public final class GwtApplicationContainerProvider implements ApplicationContainerProvider, EntryPoint {

    @Override
    public void onModuleLoad() {
        ApplicationContainer.start(this, null);
    }
}
