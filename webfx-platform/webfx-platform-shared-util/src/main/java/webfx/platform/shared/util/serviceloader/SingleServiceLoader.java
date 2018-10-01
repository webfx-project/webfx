package webfx.platform.shared.util.serviceloader;

import webfx.platform.shared.util.function.Factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class SingleServiceLoader {

    public enum NotFoundPolicy {RETURN_NULL, TRACE_AND_RETURN_NULL, THROW_EXCEPTION}

    private final static Map<Class, Factory> DEFAULT_SERVICE_FACTORIES = new HashMap<>();
    private final static Map<Class, Object> SERVICE_INSTANCES_CACHE = new HashMap<>();


    public static <T> void registerDefaultServiceFactory(Class<T> serviceClass, Factory<T> serviceFactory) {
        DEFAULT_SERVICE_FACTORIES.put(serviceClass, serviceFactory);
    }

    public static <T> T instantiateDefaultService(Class<T> serviceClass) {
        Factory factory = DEFAULT_SERVICE_FACTORIES.get(serviceClass);
        return factory == null ? null : (T) factory.create();
    }

    public static <T> void cacheServiceInstance(Class<T> serviceClass, T serviceInstance) {
        SERVICE_INSTANCES_CACHE.put(serviceClass, serviceInstance);
    }

    public static <T> T loadService(Class<T> serviceClass) {
        return loadService(serviceClass, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <T> T loadService(Class<T> serviceClass, NotFoundPolicy policy) {
        return loadService(serviceClass, policy, true);
    }

    public static <T> T loadService(Class<T> serviceClass, NotFoundPolicy policy, boolean cacheable) {
        T serviceInstance = (T) SERVICE_INSTANCES_CACHE.get(serviceClass);
        if (serviceInstance == null) {
            Iterator<T> it = ServiceLoader.load(serviceClass).iterator();
            serviceInstance =  it.hasNext() ? it.next() : instantiateDefaultService(serviceClass);
        }
        if (serviceInstance == null) {
            if (policy != NotFoundPolicy.RETURN_NULL) {
                String message = "Cannot find META-INF/services/" + serviceClass.getName() + " on classpath";
                if (policy == NotFoundPolicy.THROW_EXCEPTION)
                    throw new IllegalStateException(message);
                Logger.getGlobal().log(Level.WARNING, message);
            }
        } else if (cacheable)
            cacheServiceInstance(serviceClass, serviceInstance);
        return serviceInstance;
    }
}
