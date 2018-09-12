/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.frontend.MongooseFrontendApplicationModule;
import mongooses.web.activities.sharedends.MongooseWebApplicationModule;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

class GwtMongooseFrontendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModule.class, MongooseWebApplicationModule::new);
        registerService(ApplicationModule.class, MongooseFrontendApplicationModule::new);
    }

}