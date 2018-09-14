/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import mongooses.backend.MongooseBackendApplicationModuleInitializer;

class GwtMongooseBackendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModuleInitializer.class, MongooseWebApplicationModuleInitializer::new);
        registerService(ApplicationModuleInitializer.class, MongooseBackendApplicationModuleInitializer::new);
    }

}