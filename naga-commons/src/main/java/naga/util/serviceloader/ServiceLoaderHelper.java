package naga.util.serviceloader;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ServiceLoaderHelper {

    public enum NotFoundPolicy {RETURN_NULL, TRACE_AND_RETURN_NULL, THROW_EXCEPTION}

    public static <T> T loadService(Class<T> serviceClass) {
        return loadService(serviceClass, NotFoundPolicy.THROW_EXCEPTION);
    }

    public static <T> T loadService(Class<T> serviceClass, NotFoundPolicy policy) {
        ServiceLoader<T> factories = ServiceLoader.load(serviceClass);
        if (factories != null && factories.iterator().hasNext())
            return factories.iterator().next();
        if (policy != NotFoundPolicy.RETURN_NULL) {
            String message = "Cannot find META-INF/services/" + serviceClass.getName() + " on classpath";
            if (policy == NotFoundPolicy.THROW_EXCEPTION)
                throw new IllegalStateException(message);
            System.out.println(message);
        }
        return null;
    }

}
