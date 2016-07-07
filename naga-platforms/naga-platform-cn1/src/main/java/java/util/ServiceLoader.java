package java.util;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.providers.cn1.CodenameOneToolkit;
import naga.platform.spi.Platform;
import naga.platform.providers.cn1.CodenameOnePlatform;

/**
 * ServiceLoader CN1 implementation that just works to provide the CN1 Platform and Toolkit
 *
 * @author Bruno Salmon
 */

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(Platform.class))
            return new ServiceLoader<>(new CodenameOnePlatform());
        if (service.equals(Toolkit.class))
            return new ServiceLoader<>(new CodenameOneToolkit());
        return null;
    }

    private final Object service;

    public ServiceLoader(Object service) {
        this.service = service;
    }

    public Iterator<S> iterator() {
        ArrayList list = new ArrayList();
        list.add(service);
        return (Iterator<S>) list.iterator();
    }
}