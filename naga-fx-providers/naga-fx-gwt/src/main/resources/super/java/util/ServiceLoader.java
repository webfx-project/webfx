/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

package java.util;

import naga.platform.json.spi.JsonProvider;
import naga.providers.platform.client.gwt.json.GwtJsonObject;
import naga.scheduler.SchedulerProvider;
import naga.providers.platform.client.gwt.scheduler.GwtSchedulerProvider;
import naga.platform.services.resource.spi.ResourceServiceProvider;
import naga.providers.platform.client.gwt.services.resource.GwtResourceServiceProvider;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.platform.services.query.remote.RemoteQueryServiceProvider;
import naga.util.numbers.spi.NumbersProvider;
import naga.util.numbers.providers.StandardPlatformNumbers;
import naga.platform.spi.Platform;
import naga.providers.platform.client.gwt.GwtPlatform;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.GwtToolkit;

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> service) {
        if (service.equals(Platform.class))
            return new ServiceLoader<>(new GwtPlatform());
        if (service.equals(SchedulerProvider.class))
            return new ServiceLoader<>(new GwtSchedulerProvider());
        if (service.equals(JsonProvider.class))
            return new ServiceLoader<>(GwtJsonObject.create());
        if (service.equals(ResourceServiceProvider.class))
            return new ServiceLoader<>(new GwtResourceServiceProvider());
        if (service.equals(QueryServiceProvider.class))
            return new ServiceLoader<>(new RemoteQueryServiceProvider());
        if (service.equals(Toolkit.class))
            return new ServiceLoader<>(new GwtToolkit());
        if (service.equals(NumbersProvider.class))
            return new ServiceLoader<>(new StandardPlatformNumbers());
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