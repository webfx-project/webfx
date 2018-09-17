/**
 * ServiceLoader GWT implementation
 *
 * @author Bruno Salmon
 */

package java.util;

import mongooses.web.activities.sharedends.MongooseWebApplicationModuleInitializer;

public abstract class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        return new GwtMongooseBackendServiceLoader().load(serviceClass);
    }
}