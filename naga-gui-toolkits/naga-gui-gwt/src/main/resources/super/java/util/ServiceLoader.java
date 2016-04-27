package java.util;

/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.gwt.GwtPlatform;
import naga.core.spi.gui.gwt.GwtToolkit;

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(Platform.class))
            return new ServiceLoader<>(new GwtPlatform());
        if (service.equals(GuiToolkit.class))
            return new ServiceLoader<>(new GwtToolkit());
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