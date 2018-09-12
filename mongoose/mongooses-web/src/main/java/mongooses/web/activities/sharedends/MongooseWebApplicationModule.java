package mongooses.web.activities.sharedends;

import webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import webfx.platforms.core.services.resource.ResourceService;

/**
 * @author Bruno Salmon
 */
public class MongooseWebApplicationModule implements ApplicationModule {

    @Override
    public void start() {
        ((GwtResourceServiceProviderImpl) ResourceService.getProvider()).register(MongooseSharedEndsWebBundle.B);
    }
}
