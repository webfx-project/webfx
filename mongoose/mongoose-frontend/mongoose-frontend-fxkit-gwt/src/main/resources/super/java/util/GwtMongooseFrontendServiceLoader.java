/**
 * @author Bruno Salmon
 */

package java.util;

import mongooses.frontend.MongooseFrontendApplicationModuleInitializer;
import mongooses.web.activities.sharedends.MongooseWebApplicationModuleInitializer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

class GwtMongooseFrontendServiceLoader extends GwtFxKitServiceLoader {

    static  {
        registerService(ApplicationModuleInitializer.class, MongooseWebApplicationModuleInitializer::new, MongooseFrontendApplicationModuleInitializer::new);
    }

}