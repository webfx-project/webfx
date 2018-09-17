/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.web.activities.sharedends.MongooseWebApplicationModuleInitializer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import mongooses.backend.MongooseBackendApplicationModuleInitializer;

class GwtMongooseBackendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        registerService(ApplicationModuleInitializer.class, MongooseWebApplicationModuleInitializer::new, MongooseBackendApplicationModuleInitializer::new);
    }

}