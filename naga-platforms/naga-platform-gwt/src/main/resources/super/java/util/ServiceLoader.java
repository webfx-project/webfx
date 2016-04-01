package java.util;

/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.gwt.GwtPlatform;

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(Platform.class))
            return new ServiceLoader<>();
        return null;
    }

    public Iterator<S> iterator() {
        ArrayList list = new ArrayList();
        list.add(new GwtPlatform());
        return (Iterator<S>) list.iterator();
    }
}