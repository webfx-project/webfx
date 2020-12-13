package dev.webfx.platform.shared.util.serviceloader;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class SingleServiceProvider {

    public enum NotFoundPolicy {RETURN_NULL, TRACE_AND_RETURN_NULL, THROW_EXCEPTION}

    private final static Map<Class, ServiceInfo> SERVICE_INFOS = new HashMap<>();

    private static <S>  ServiceInfo<S>  getOrCreateServiceInfo(Class<S> serviceClass) {
        ServiceInfo<S> serviceInfo = SERVICE_INFOS.get(serviceClass);
        if (serviceInfo == null)
            SERVICE_INFOS.put(serviceClass, serviceInfo = new ServiceInfo<>());
        return serviceInfo;
    }

    public static <S> void registerServiceSupplier(Class<S> serviceClass, Supplier<ServiceLoader<S>> serviceLoaderSupplier) {
        registerServiceSupplier(serviceClass, serviceLoaderSupplier, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <S> void registerServiceSupplier(Class<S> serviceClass, Supplier<ServiceLoader<S>> serviceLoaderSupplier, NotFoundPolicy notFoundPolicy) {
        getOrCreateServiceInfo(serviceClass).setServiceLoaderSupplier(serviceLoaderSupplier, notFoundPolicy);
    }

    public static <S> void registerServiceProvider(Class<S> serviceClass, S serviceInstance) {
        getOrCreateServiceInfo(serviceClass).setProvider(serviceInstance);
    }

    public static <S> void registerServiceInterceptor(Class<S> serviceClass, Function<S, S> interceptor) {
        getOrCreateServiceInfo(serviceClass).addInterceptor(interceptor);
    }

    public static <S> S getProvider(Class<S> serviceClass) {
        return getProvider(serviceClass, null);
    }

    public static <S> S getProvider(Class<S> serviceClass, Supplier<ServiceLoader<S>> serviceLoaderSupplier) {
        return getProvider(serviceClass, serviceLoaderSupplier, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <S> S getProvider(Class<S> serviceClass, Supplier<ServiceLoader<S>> serviceLoaderSupplier, NotFoundPolicy notFoundPolicy) {
        ServiceInfo<S> serviceInfo = getOrCreateServiceInfo(serviceClass)
                .setServiceLoaderSupplier(serviceLoaderSupplier, notFoundPolicy);

        S provider = serviceInfo.getProxy();
        if (provider != null)
            return provider;

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
        private Supplier<ServiceLoader<S>> serviceLoaderSupplier;
        private NotFoundPolicy notFoundPolicy;
        private S provider;
        private List<Function<S, S>> interceptors;
        private S proxy;

        ServiceInfo<S> setServiceLoaderSupplier(Supplier<ServiceLoader<S>> serviceLoaderSupplier, NotFoundPolicy notFoundPolicy) {
            if (serviceLoaderSupplier != null)
                this.serviceLoaderSupplier = serviceLoaderSupplier;
            if (notFoundPolicy != null)
                this.notFoundPolicy = notFoundPolicy;
            return this;
        }

        void setProvider(S provider) {
            this.provider = provider;
        }

        void addInterceptor(Function<S, S> interceptor) {
            if (interceptors == null)
                interceptors = new ArrayList<>();
            interceptors.add(interceptor);
            proxy = null;
        }

        S getProvider() {
            if (provider == null) {
                Iterator<S> it = serviceLoaderSupplier.get().iterator();
                if (it.hasNext())
                    provider = it.next();
            }
            return provider;
        }

        S getProxy() {
            if (proxy == null) {
                proxy = getProvider();
                if (interceptors != null)
                    for (Function<S, S> interceptor : interceptors)
                        proxy = interceptor.apply(proxy);
            }
            return proxy;
        }
    }
}
