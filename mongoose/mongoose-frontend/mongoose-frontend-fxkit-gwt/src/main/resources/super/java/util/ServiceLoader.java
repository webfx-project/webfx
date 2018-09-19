package java.util;

import java.util.Iterator;
import java.util.logging.Logger;
import webfx.platforms.core.util.function.Factory;

public class ServiceLoader<S> implements Iterable<S> {

    public static <S> ServiceLoader<S> load(Class<S> serviceClass) {
        switch (serviceClass.getName()) {
            // Single SPI providers
            case "webfx.platforms.core.services.storage.spi.LocalStorageProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtLocalStorageProviderImpl::new);
            case "webfx.platforms.core.services.storage.spi.SessionStorageProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.storage.GwtSessionStorageProviderImpl::new);
            case "webfx.platforms.core.services.windowhistory.spi.WindowHistoryProvider": return new ServiceLoader<S>(webfx.platforms.web.services.windowhistory.WebWindowHistoryProvider::new);
            case "webfx.platforms.core.services.resource.spi.ResourceServiceProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl::new);
            case "webfx.fxkits.core.spi.FxKitProvider": return new ServiceLoader<S>(webfx.fxkit.gwt.GwtFxKitProvider::new);
            case "webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.appcontainer.GwtApplicationContainerProvider::new);
            case "webfx.platforms.core.services.shutdown.spi.ShutdownProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.shutdown.GwtShutdownProviderImpl::new);
            case "webfx.platforms.core.services.log.spi.LoggerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.log.GwtLoggerProviderImpl::new);
            case "webfx.platforms.core.util.numbers.spi.NumbersProvider": return new ServiceLoader<S>(webfx.platforms.core.util.numbers.providers.StandardNumbersProviderImpl::new);
            case "webfx.platforms.core.services.bus.spi.BusServiceProvider": return new ServiceLoader<S>(webfx.platforms.web.services.clientbus.WebClientBusServiceProvider::new);
            case "webfx.platforms.core.services.windowlocation.spi.WindowLocationProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.windowlocation.GwtWindowLocationProvider::new);
            case "webfx.platforms.core.services.json.spi.JsonProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.json.GwtJsonObject::create);
            case "webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl::new);
            case "webfx.platforms.core.services.scheduler.spi.SchedulerProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl::new);
            case "webfx.platforms.web.services.windowhistory.JsWindowHistory": return new ServiceLoader<S>(webfx.platform.gwt.services.windowhistory.GwtJsWindowHistory::new);
            case "webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider": return new ServiceLoader<S>(webfx.platform.gwt.services.websocket.GwtWebSocketServiceProviderImpl::new);
            // Multiple SPI providers
            case "webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer": return new ServiceLoader<S>(mongooses.frontend.MongooseFrontendApplicationModuleInitializer::new, webfx.fxkits.core.FxKitModuleInitializer::new, webfx.platforms.core.services.buscall.BusCallModuleInitializer::new, webfx.platforms.core.services.json.codec.JsonCodecModuleInitializer::new, webfx.platform.gwt.services.resource.GwtResourceModuleInitializer::new);
            case "webfx.platforms.core.services.buscall.spi.BusCallEndpoint": return new ServiceLoader<S>(webfx.platforms.core.services.querypush.ExecuteQueryPushBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBusCallEndpoint::new, webfx.platforms.core.services.update.ExecuteUpdateBatchBusCallEndpoint::new, webfx.platforms.core.services.query.ExecuteQueryBusCallEndpoint::new, webfx.platforms.core.services.query.ExecuteQueryBatchBusCallEndpoint::new);
            case "webfx.platforms.core.services.json.codec.JsonCodec": return new ServiceLoader<S>(webfx.platforms.core.services.querypush.QueryPushArgument.Codec::new, webfx.platforms.core.services.querypush.QueryPushResult.Codec::new, webfx.platforms.core.services.querypush.diff.impl.QueryResultTranslation.Codec::new, webfx.platforms.core.services.update.UpdateArgument.Codec::new, webfx.platforms.core.services.update.UpdateResult.Codec::new, webfx.platforms.core.services.update.GeneratedKeyBatchIndex.Codec::new, webfx.platforms.core.services.buscall.BusCallArgument.Codec::new, webfx.platforms.core.services.buscall.BusCallResult.Codec::new, webfx.platforms.core.services.buscall.SerializableAsyncResult.Codec::new, webfx.platforms.core.services.json.codec.BatchJsonCodec::new, webfx.platforms.core.services.query.QueryArgument.Codec::new, webfx.platforms.core.services.query.QueryResult.Codec::new);
            case "webfx.platform.gwt.services.resource.GwtResourceBundle": return new ServiceLoader<S>(mongooses.web.activities.sharedends.MongooseSharedEndsWebBundle.ResourceBundle::new);
            // SPI NOT FOUND
            default:
               Logger.getLogger(ServiceLoader.class.getName()).warning("SPI not found for " + serviceClass);
               return new ServiceLoader<S>();
        }
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