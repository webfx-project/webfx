/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import mongooses.backend.MongooseBackendApplicationModule;

class GwtMongooseBackendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModule.class, MongooseWebApplicationModule::new);
        registerService(ApplicationModule.class, MongooseBackendApplicationModule::new);
    }

}