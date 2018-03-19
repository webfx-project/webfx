/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

package java.util;

import naga.platform.json.spi.JsonProvider;
import naga.platform.services.shutdown.spi.ShutdownProvider;
import naga.platform.services.storage.spi.LocalStorageProvider;
import naga.platform.services.storage.spi.SessionStorageProvider;
import naga.providers.platform.client.gwt.json.GwtJsonObject;
import naga.providers.platform.client.gwt.services.shutdown.GwtShutdownProvider;
import naga.providers.platform.client.gwt.services.storage.GwtLocalStorageProvider;
import naga.providers.platform.client.gwt.services.storage.GwtSessionStorageProvider;
import naga.scheduler.SchedulerProvider;
import naga.providers.platform.client.gwt.scheduler.GwtSchedulerProvider;
import naga.platform.services.resource.spi.ResourceServiceProvider;
import naga.providers.platform.client.gwt.services.resource.GwtResourceServiceProvider;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.platform.services.query.remote.RemoteQueryServiceProvider;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.platform.services.update.remote.RemoteUpdateServiceProvider;
import naga.platform.services.log.spi.LoggerProvider;
import naga.providers.platform.client.gwt.services.log.GwtLoggerProvider;
import naga.platform.client.websocket.spi.WebSocketFactoryProvider;
import naga.providers.platform.client.gwt.websocket.GwtWebSocketFactoryProvider;
import naga.util.numbers.spi.NumbersProvider;
import naga.util.numbers.providers.StandardPlatformNumbers;
import naga.platform.spi.Platform;
import naga.providers.platform.client.gwt.GwtPlatform;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.GwtToolkit;
import naga.util.serviceloader.ServiceLoaderHelper;

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        if (serviceClass.equals(Platform.class))
            return new ServiceLoader<>(new GwtPlatform());
        if (serviceClass.equals(SchedulerProvider.class))
            return new ServiceLoader<>(new GwtSchedulerProvider());
        if (serviceClass.equals(JsonProvider.class))
            return new ServiceLoader<>(GwtJsonObject.create());
        if (serviceClass.equals(ResourceServiceProvider.class))
            return new ServiceLoader<>(new GwtResourceServiceProvider());
        if (serviceClass.equals(QueryServiceProvider.class))
            return new ServiceLoader<>(new RemoteQueryServiceProvider());
        if (serviceClass.equals(UpdateServiceProvider.class))
            return new ServiceLoader<>(new RemoteUpdateServiceProvider());
        if (serviceClass.equals(LoggerProvider.class))
            return new ServiceLoader<>(new GwtLoggerProvider());
        if (serviceClass.equals(WebSocketFactoryProvider.class))
            return new ServiceLoader<>(new GwtWebSocketFactoryProvider());
        if (serviceClass.equals(Toolkit.class))
            return new ServiceLoader<>(new GwtToolkit());
        if (serviceClass.equals(NumbersProvider.class))
            return new ServiceLoader<>(new StandardPlatformNumbers());
        if (serviceClass.equals(ShutdownProvider.class))
            return new ServiceLoader<>(new GwtShutdownProvider());
        if (serviceClass.equals(LocalStorageProvider.class))
            return new ServiceLoader<>(new GwtLocalStorageProvider());
        if (serviceClass.equals(SessionStorageProvider.class))
            return new ServiceLoader<>(new GwtSessionStorageProvider());
        return new ServiceLoader<>(ServiceLoaderHelper.instantiateDefaultService(serviceClass));
    }

    private final Object service;

    public ServiceLoader(Object service) {
        this.service = service;
    }

    public Iterator<S> iterator() {
        ArrayList list = new ArrayList();
        if (service != null)
            list.add(service);
        return (Iterator<S>) list.iterator();
    }
}