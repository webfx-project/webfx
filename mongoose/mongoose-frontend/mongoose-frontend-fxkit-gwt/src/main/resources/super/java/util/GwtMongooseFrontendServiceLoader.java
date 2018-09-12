/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.frontend.MongooseFrontendApplicationModule;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

class GwtMongooseFrontendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModule.class, MongooseWebApplicationModule::new);
        registerService(ApplicationModule.class, MongooseFrontendApplicationModule::new);
    }

}