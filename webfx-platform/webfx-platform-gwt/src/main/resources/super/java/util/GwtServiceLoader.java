/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platforms.core.util.collection.Collections;
import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

class GwtServiceLoader {

    private final static Map<Class, List<Factory>> serviceContructorsLists = new HashMap<>();

    final static <S> void registerService(Class<S> serviceClass, Factory<S> serviceConstructor) {
        getConstructorsList(serviceClass, true).add(0, serviceConstructor);
    }

    private static List<Factory> getConstructorsList(Class serviceClass, boolean create) {
        List<Factory> list = serviceContructorsLists.get(serviceClass);
        if (list == null && create)
            serviceContructorsLists.put(serviceClass, list = new ArrayList<>());
        return list;
    }

    final <S> ServiceLoader<S> load(Class<S> serviceClass)  {
        return new ServiceLoader<S>() {
            @Override
            public Iterator<S> iterator() {
                ArrayList<S> instancesList = new ArrayList<>();
                List<Factory> constructorsList = getConstructorsList(serviceClass, false);
                if (constructorsList != null)
                    Collections.forEach(constructorsList, c -> instancesList.add((S) c.create()));
                else {
                    S serviceInstance = SingleServiceLoader.instantiateDefaultService(serviceClass);
                    if (serviceInstance != null)
                        instancesList.add(serviceInstance);
                }
                return instancesList.iterator();
            }
        };
    }

}