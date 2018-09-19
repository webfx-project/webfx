package webfx.platform.gwt.services.resource;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.resource.ResourceService;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class GwtResourceModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platform-gwt-resource";
    }

    @Override
    public int getInitLevel() {
        return RESOURCE_BUNDLE_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        StringBuilder sb = new StringBuilder();
        for (GwtResourceBundle gwtResourceBundle : ServiceLoader.load(GwtResourceBundle.class)) {
            ((GwtResourceServiceProviderImpl) ResourceService.getProvider()).register(gwtResourceBundle);
            for (String resourcePath : gwtResourceBundle.resourcePathsForLogging())
                sb.append(sb.length() == 0 ? gwtResourceBundle.getClass().getName() + " registered the following resources:\n" : "\n").append(resourcePath);
        }
        Logger.log(sb);
    }
}
