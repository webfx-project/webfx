package dev.webfx.platform.gwt.services.appcontainer.spi.impl;

import com.google.gwt.core.client.EntryPoint;
import dev.webfx.platform.shared.services.appcontainer.ApplicationContainer;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationContainerProvider;

/**
 * @author Bruno Salmon
 */
public final class GwtApplicationContainerProvider implements ApplicationContainerProvider, EntryPoint {

    @Override
    public void onModuleLoad() {
        ApplicationContainer.start(this, null);
    }
}
