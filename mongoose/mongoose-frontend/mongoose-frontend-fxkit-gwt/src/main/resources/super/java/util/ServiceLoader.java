package java.util;

import java.util.Iterator;
import webfx.platforms.core.util.function.Factory;

public class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        if (serviceClass.equals(webfx.platforms.core.services.storage.spi.LocalStorageProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtLocalStorageProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.storage.spi.SessionStorageProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtSessionStorageProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.windowhistory.spi.WindowHistoryProvider.class)) return new ServiceLoader<S>(webfx.platforms.web.services.windowhistory.WebWindowHistoryProvider::new);
        if (serviceClass.equals(webfx.platforms.core.services.resource.spi.ResourceServiceProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl::new);
        if (serviceClass.equals(webfx.fxkits.core.spi.FxKitProvider.class)) return new ServiceLoader<S>(webfx.fxkit.gwt.GwtFxKitProvider::new);
        if (serviceClass.equals(webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.appcontainer.GwtApplicationContainerProvider::new);
        if (serviceClass.equals(webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer.class)) return new ServiceLoader<S>(mongooses.frontend.MongooseFrontendApplicationModuleInitializer::new, webfx.fxkits.core.FxKitModuleInitializer::new, webfx.platforms.core.services.buscall.BusCallModuleInitializer::new, webfx.platforms.core.services.json.codec.JsonCodecModuleInitializer::new, webfx.platform.gwt.services.resource.GwtResourceModuleInitializer::new);
        if (serviceClass.equals(webfx.platforms.core.services.shutdown.spi.ShutdownProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.shutdown.GwtShutdownProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.log.spi.LoggerProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.log.GwtLoggerProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.util.numbers.spi.NumbersProvider.class)) return new ServiceLoader<S>(webfx.platforms.core.util.numbers.providers.StandardNumbersProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.buscall.spi.BusCallEndpoint.class)) return new ServiceLoader<S>(webfx.platforms.core.services.querypush.ExecuteQueryPushBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBatchBusCallEndpoint::new, webfx.platforms.core.services.query.ExecuteQueryBusCallEndpoint::new, webfx.platforms.core.services.query.ExecuteQueryBatchBusCallEndpoint::new);
        if (serviceClass.equals(webfx.platforms.core.services.bus.spi.BusServiceProvider.class)) return new ServiceLoader<S>(webfx.platforms.web.services.clientbus.WebClientBusServiceProvider::new, webfx.platforms.core.services.bus.client.ClientBusServiceProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.windowlocation.spi.WindowLocationProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.windowlocation.GwtWindowLocationProvider::new);
        if (serviceClass.equals(webfx.platforms.core.services.json.spi.JsonProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.json.GwtJsonObject::create);
        if (serviceClass.equals(webfx.platforms.core.services.json.codec.JsonCodec.class)) return new ServiceLoader<S>(webfx.platforms.core.services.querypush.QueryPushArgument.Codec::new, webfx.platforms.core.services.querypush.QueryPushResult.Codec::new, webfx.platforms.core.services.querypush.diff.impl.QueryResultTranslation.Codec::new, webfx.platforms.core.services.update.UpdateArgument.Codec::new, webfx.platforms.core.services.update.UpdateResult.Codec::new, webfx.platforms.core.services.update.GeneratedKeyBatchIndex.Codec::new, webfx.platforms.core.services.buscall.BusCallArgument.Codec::new, webfx.platforms.core.services.buscall.BusCallResult.Codec::new, webfx.platforms.core.services.buscall.SerializableAsyncResult.Codec::new, webfx.platforms.core.services.json.codec.BatchJsonCodec::new, webfx.platforms.core.services.query.QueryArgument.Codec::new, webfx.platforms.core.services.query.QueryResult.Codec::new);
        if (serviceClass.equals(webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.core.services.scheduler.spi.SchedulerProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl::new);
        if (serviceClass.equals(webfx.platforms.web.services.windowhistory.JsWindowHistory.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.windowhistory.GwtJsWindowHistory::new);
        if (serviceClass.equals(webfx.platform.gwt.services.resource.GwtResourceBundle.class)) return new ServiceLoader<S>(mongooses.web.activities.sharedends.MongooseSharedEndsWebBundle.ResourceBundle::new);
        if (serviceClass.equals(webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider.class)) return new ServiceLoader<S>(webfx.platform.gwt.services.websocket.GwtWebSocketServiceProviderImpl::new);
        return new ServiceLoader<S>();
    }

    private final Factory[] factories;

    public ServiceLoader(Factory... factories) {
        this.factories = factories;
    }

    public Iterator<S> iterator() {
        return new Iterator<S>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < factories.length;
            }

            @Override
            public S next() {
                return (S) factories[index++].create();
            }
        };
    }
}