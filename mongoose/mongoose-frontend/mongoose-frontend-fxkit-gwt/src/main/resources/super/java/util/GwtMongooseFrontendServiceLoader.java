/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.frontend.MongooseFrontendApplicationModuleInitializer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

class GwtMongooseFrontendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        //registerService(ApplicationModuleInitializer.class, MongooseWebApplicationModuleInitializer::new);
        registerService(ApplicationModuleInitializer.class, MongooseFrontendApplicationModuleInitializer::new);
    }

}