/**
 * ServiceLoader GWT implementation that just works to provide the GwtPlatform on automatic registration Platform.get()
 *
 * @author Bruno Salmon
 */

package java.util;

import webfx.platform.services.json.spi.JsonProvider;
import webfx.platform.services.shutdown.spi.ShutdownProvider;
import webfx.platform.services.storage.spi.LocalStorageProvider;
import webfx.platform.services.storage.spi.SessionStorageProvider;
import webfx.providers.platform.client.gwt.services.json.GwtJsonObject;
import webfx.providers.platform.client.gwt.services.shutdown.GwtShutdownProviderImpl;
import webfx.providers.platform.client.gwt.services.storage.GwtLocalStorageProviderImpl;
import webfx.providers.platform.client.gwt.services.storage.GwtSessionStorageProviderImpl;
import webfx.platform.services.scheduler.spi.SchedulerProvider;
import webfx.providers.platform.client.gwt.services.scheduler.GwtSchedulerProviderImpl;
import webfx.platform.services.resource.spi.ResourceServiceProvider;
import webfx.providers.platform.client.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platform.services.log.spi.LoggerProvider;
import webfx.providers.platform.client.gwt.services.log.GwtLoggerProviderImpl;
import webfx.platform.services.websocket.spi.WebSocketServiceProvider;
import webfx.providers.platform.client.gwt.services.websocket.GwtWebSocketServiceProviderImpl;
import webfx.util.numbers.providers.StandardNumbersProviderImpl;
import webfx.util.numbers.spi.NumbersProvider;
import webfx.platform.spi.Platform;
import webfx.providers.platform.client.gwt.GwtPlatform;
import webfx.fx.spi.Toolkit;
import webfx.fx.spi.gwt.GwtToolkit;
import webfx.util.serviceloader.ServiceLoaderHelper;

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
        if (serviceClass.equals(Toolkit.class))
            return new ServiceLoader<>(new GwtToolkit());
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