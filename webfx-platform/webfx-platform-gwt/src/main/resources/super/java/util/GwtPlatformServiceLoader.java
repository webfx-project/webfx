/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platform.gwt.services.windowhistory.GwtJsWindowHistory;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.services.windowhistory.spi.WindowHistoryProvider;
import webfx.platforms.core.services.windowlocation.spi.WindowLocationProvider;
import webfx.platforms.core.services.buscall.BusCallModuleInitializer;
import webfx.platforms.core.services.query.QueryModuleInitializer;
import webfx.platforms.core.services.querypush.QueryPushModuleInitializer;
import webfx.platforms.core.services.update.UpdateModuleInitializer;
import webfx.platforms.web.services.windowhistory.JsWindowHistory;
import webfx.platforms.web.services.windowhistory.WebWindowHistoryProvider;
import webfx.platforms.core.services.bus.spi.BusServiceProvider;
import webfx.platforms.core.services.json.spi.JsonProvider;
import webfx.platforms.core.services.shutdown.spi.ShutdownProvider;
import webfx.platforms.core.services.storage.spi.LocalStorageProvider;
import webfx.platforms.core.services.storage.spi.SessionStorageProvider;
import webfx.platform.gwt.services.json.GwtJsonObject;
import webfx.platform.gwt.services.shutdown.GwtShutdownProviderImpl;
import webfx.platform.gwt.services.storage.GwtLocalStorageProviderImpl;
import webfx.platform.gwt.services.storage.GwtSessionStorageProviderImpl;
import webfx.platforms.core.services.scheduler.spi.SchedulerProvider;
import webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider;
import webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl;
import webfx.platforms.core.services.resource.spi.ResourceServiceProvider;
import webfx.platform.gwt.services.resource.GwtResourceServiceProviderImpl;
import webfx.platforms.core.services.log.spi.LoggerProvider;
import webfx.platform.gwt.services.log.GwtLoggerProviderImpl;
import webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider;
import webfx.platform.gwt.services.websocket.GwtWebSocketServiceProviderImpl;
import webfx.platforms.core.util.numbers.providers.StandardNumbersProviderImpl;
import webfx.platforms.core.util.numbers.spi.NumbersProvider;
import webfx.platforms.web.services.bus.WebClientBusServiceProvider;
import webfx.platform.gwt.services.windowlocation.GwtWindowLocationProvider;

class GwtPlatformServiceLoader extends GwtServiceLoader {

    static {
        registerService(BusServiceProvider.class, WebClientBusServiceProvider::new);
        registerService(SchedulerProvider.class, GwtSchedulerProviderImpl::new);
        registerService(UiSchedulerProvider.class, GwtSchedulerProviderImpl::new);
        registerService(WindowLocationProvider.class, GwtWindowLocationProvider::new);
        registerService(WindowHistoryProvider.class, WebWindowHistoryProvider::new);
        registerService(JsWindowHistory.class, GwtJsWindowHistory::new);
        registerService(JsonProvider.class, GwtJsonObject::create);
        registerService(ResourceServiceProvider.class, GwtResourceServiceProviderImpl::new);
        registerService(LoggerProvider.class, GwtLoggerProviderImpl::new);
        registerService(WebSocketServiceProvider.class, GwtWebSocketServiceProviderImpl::new);
        registerService(NumbersProvider.class, StandardNumbersProviderImpl::new);
        registerService(ShutdownProvider.class, GwtShutdownProviderImpl::new);
        registerService(LocalStorageProvider.class, GwtLocalStorageProviderImpl::new);
        registerService(SessionStorageProvider.class, GwtSessionStorageProviderImpl::new);
        registerService(ApplicationModuleInitializer.class, UpdateModuleInitializer::new, QueryPushModuleInitializer::new, QueryModuleInitializer::new, BusCallModuleInitializer::new);
    }

}