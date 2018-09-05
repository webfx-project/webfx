/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

class GwtServiceLoader {

    private final static Map<Class, Factory> serviceContructors = new HashMap<>();

    final static <S> void registerService(Class<S> serviceClass, Factory<S> serviceConstructor) {
        serviceContructors.put(serviceClass, serviceConstructor);
    }

    final <S> ServiceLoader<S> load(Class<S> serviceClass)  {
        return new ServiceLoader<S>() {
            @Override
            public Iterator<S> iterator() {
                S serviceInstance;
                Factory serviceConstructor = serviceContructors.get(serviceClass);
                if (serviceConstructor != null)
                    serviceInstance = (S) serviceConstructor.create();
                else
                    serviceInstance = ServiceLoaderHelper.instantiateDefaultService(serviceClass);
                ArrayList<S> list = new ArrayList<>();
                if (serviceInstance != null)
                    list.add(serviceInstance);
                return list.iterator();
            }
        };
    }

}