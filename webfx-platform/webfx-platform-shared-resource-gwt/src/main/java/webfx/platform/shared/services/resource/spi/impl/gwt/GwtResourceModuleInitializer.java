package webfx.platform.shared.services.resource.spi.impl.gwt;

import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.resource.ResourceService;
import webfx.platform.shared.util.collection.Collections;

import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class GwtResourceModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platform-shared-resource-gwt";
    }

    @Override
    public int getInitLevel() {
        return RESOURCE_BUNDLE_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        List<GwtResourceBundle> gwtResourceBundles = Collections.listOf(ServiceLoader.load(GwtResourceBundle.class));
        Logger.log(gwtResourceBundles.size() + " gwt resource bundles provided");
        for (GwtResourceBundle gwtResourceBundle : gwtResourceBundles) {
            ((GwtResourceServiceProvider) ResourceService.getProvider()).register(gwtResourceBundle);
            StringBuilder sb = new StringBuilder();
            for (String resourcePath : gwtResourceBundle.resourcePathsForLogging())
                sb.append(sb.length() == 0 ? gwtResourceBundle.getClass().getName() + " registered the following resources:\n" : "\n").append(resourcePath);
            Logger.log(sb);
        }
    }
}
