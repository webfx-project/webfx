/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

package java.util;

import webfx.fxkit.gwt.GwtFxKit;
import webfx.fxkits.core.spi.FxKit;
import webfx.platforms.core.services.json.spi.JsonProvider;
import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;
import webfx.platforms.core.services.storage.spi.LocalStorageProvider;
import webfx.platforms.core.services.storage.spi.SessionStorageProvider;
import webfx.platform.gwt.services.json.GwtJsonObject;
import webfx.platform.gwt.services.shutdown.GwtShutdownProviderImpl;
import webfx.platform.gwt.services.storage.GwtLocalStorageProviderImpl;
import webfx.platform.gwt.services.storage.GwtSessionStorageProviderImpl;
import webfx.platforms.core.services.scheduler.spi.SchedulerProvider;
import webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl;
import webfx.platforms.core.services.resource.spi.ResourceServiceProvider;
import webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platforms.core.services.log.spi.LoggerProvider;
import webfx.platform.gwt.services.log.GwtLoggerProviderImpl;
import webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider;
import webfx.platform.gwt.services.websocket.GwtWebSocketServiceProviderImpl;
import webfx.platforms.core.util.numbers.providers.StandardNumbersProviderImpl;
import webfx.platforms.core.util.numbers.spi.NumbersProvider;
import webfx.platforms.core.spi.Platform;
import webfx.platform.gwt.GwtPlatform;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

public class ServiceLoader<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        if (serviceClass.equals(Platform.class))
            return new ServiceLoader<>(new GwtPlatform());
        if (serviceClass.equals(SchedulerProvider.class))
            return new ServiceLoader<>(new GwtSchedulerProviderImpl());
        if (serviceClass.equals(JsonProvider.class))
            return new ServiceLoader<>(GwtJsonObject.create());
        if (serviceClass.equals(ResourceServiceProvider.class))
            return new ServiceLoader<>(new GwtResourceServiceProviderImpl());
        if (serviceClass.equals(LoggerProvider.class))
            return new ServiceLoader<>(new GwtLoggerProviderImpl());
        if (serviceClass.equals(WebSocketServiceProvider.class))
            return new ServiceLoader<>(new GwtWebSocketServiceProviderImpl());
        if (serviceClass.equals(FxKit.class))
            return new ServiceLoader<>(new GwtFxKit());
        if (serviceClass.equals(NumbersProvider.class))
            return new ServiceLoader<>(new StandardNumbersProviderImpl());
        if (serviceClass.equals(ShutdownProvider.class))
            return new ServiceLoader<>(new GwtShutdownProviderImpl());
        if (serviceClass.equals(LocalStorageProvider.class))
            return new ServiceLoader<>(new GwtLocalStorageProviderImpl());
        if (serviceClass.equals(SessionStorageProvider.class))
            return new ServiceLoader<>(new GwtSessionStorageProviderImpl());
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