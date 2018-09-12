/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.web.activities.sharedends.MongooseWebApplicationModule;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import mongooses.backend.MongooseBackendApplicationModule;

class GwtMongooseBackendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModule.class, MongooseWebApplicationModule::new);
        registerService(ApplicationModule.class, MongooseBackendApplicationModule::new);
    }

}