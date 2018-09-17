package mongooses.web.activities.sharedends;

import webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public class MongooseWebApplicationModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "mongooses-web";
    }

    @Override
    public int getInitLevel() {
        return RESOURCE_BUNDLE_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        ((GwtResourceServiceProviderImpl) ResourceService.getProvider()).register(MongooseSharedEndsWebBundle.B);
    }
}
