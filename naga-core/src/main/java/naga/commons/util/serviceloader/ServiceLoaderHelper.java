package naga.commons.util.serviceloader;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class ServiceLoaderHelper {

    public static <T> T loadService(Class<T> clazz) {
        ServiceLoader<T> factories = ServiceLoader.load(clazz);
        if (factories != null && factories.iterator().hasNext())
            return factories.iterator().next();
        throw new IllegalStateException("Cannot find META-INF/services/" + clazz.getName() + " on classpath");
    }

}
