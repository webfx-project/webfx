package java.util;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.cn1.CodenameOneToolkit;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.cn1.CodenameOnePlatform;

/**
 * ServiceLoader CN1 implementation that just works to provide the CN1 Platform and Toolkit
 *
 * @author Bruno Salmon
 */

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(Platform.class))
            return new ServiceLoader<>(new CodenameOnePlatform());
        if (service.equals(GuiToolkit.class))
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