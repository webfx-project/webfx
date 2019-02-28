package webfx.platform.shared.util.serviceloader;

import webfx.platform.shared.util.function.Callable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class SingleServiceProvider {

    public enum NotFoundPolicy {RETURN_NULL, TRACE_AND_RETURN_NULL, THROW_EXCEPTION};

    private final static Map<Class, ServiceInfo> SERVICE_INFOS = new HashMap<>();

    public static <S> void register(Class<S> serviceClass, Callable<ServiceLoader<S>> serviceLoaderCallable) {
        register(serviceClass, serviceLoaderCallable, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <S> void register(Class<S> serviceClass, Callable<ServiceLoader<S>> serviceLoaderCallable, NotFoundPolicy notFoundPolicy) {
        SERVICE_INFOS.put(serviceClass, new ServiceInfo<>(serviceLoaderCallable, notFoundPolicy));
    }

    public static <T> void cacheServiceInstance(Class<T> serviceClass, T serviceInstance) {
        SERVICE_INFOS.put(serviceClass, new ServiceInfo<>(serviceInstance));
    }

    public static <S> S getProvider(Class<S> serviceClass) {
        return getProvider(serviceClass, null);
    }

    public static <S> S getProvider(Class<S> serviceClass, Callable<ServiceLoader<S>> serviceLoaderCallable) {
        return getProvider(serviceClass, serviceLoaderCallable, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <S> S getProvider(Class<S> serviceClass, Callable<ServiceLoader<S>> serviceLoaderCallable, NotFoundPolicy notFoundPolicy) {
        ServiceInfo<S> serviceInfo = SERVICE_INFOS.get(serviceClass);
        if (serviceInfo == null && serviceLoaderCallable != null) {
            register(serviceClass, serviceLoaderCallable, notFoundPolicy);
            serviceInfo = SERVICE_INFOS.get(serviceClass);
        }
        if (serviceInfo != null) {
            if (serviceInfo.provider == null) {
                Iterator<S> it = serviceInfo.serviceLoaderCallable.call().iterator();
                if (it.hasNext())
                    serviceInfo.provider = it.next();
            }
            if (serviceInfo.provider != null)
                return serviceInfo.provider;
        }

        if (notFoundPolicy == null && serviceInfo != null)
            notFoundPolicy = serviceInfo.notFoundPolicy;
        if (notFoundPolicy != NotFoundPolicy.RETURN_NULL) {
            String message = "Cannot find META-INF/services/" + serviceClass.getName() + " on classpath";
            if (notFoundPolicy == NotFoundPolicy.THROW_EXCEPTION)
                throw new IllegalStateException(message);
            Logger.getGlobal().log(Level.WARNING, message);
        }
        return null;
    }

    private static final class ServiceInfo<S> {
        private final Callable<ServiceLoader<S>> serviceLoaderCallable;
        private final NotFoundPolicy notFoundPolicy;
        private S provider;

        ServiceInfo(Callable<ServiceLoader<S>> serviceLoaderCallable, NotFoundPolicy notFoundPolicy) {
            this.serviceLoaderCallable = serviceLoaderCallable;
            this.notFoundPolicy = notFoundPolicy;
        }

        public ServiceInfo(S provider) {
            this(null, null);
            this.provider = provider;
        }
    }
}
