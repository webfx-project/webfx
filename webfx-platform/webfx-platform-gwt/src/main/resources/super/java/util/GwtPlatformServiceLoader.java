/**
 * @author Bruno Salmon
 */

package java.util;

import webfx.platforms.core.MonolithModule;
import webfx.platforms.core.client.url.history.History;
import webfx.platforms.core.client.url.location.WindowLocation;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import webfx.platforms.core.services.buscall.BusCallModule;
import webfx.platforms.core.services.query.QueryModule;
import webfx.platforms.core.services.querypush.QueryPushModule;
import webfx.platforms.core.services.update.UpdateModule;
import webfx.platforms.web.BrowserHistory;
import webfx.platforms.web.WindowHistory;
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
import webfx.platform.gwt.url.location.GwtWindowLocation;
import webfx.platform.gwt.url.history.GwtWindowHistory;

class GwtPlatformServiceLoader extends GwtServiceLoader {

    static {
        registerService(BusServiceProvider.class, WebClientBusServiceProvider::new);
        registerService(SchedulerProvider.class, GwtSchedulerProviderImpl::new);
        registerService(UiSchedulerProvider.class, GwtSchedulerProviderImpl::new);
        registerService(WindowLocation.class, GwtWindowLocation::new);
        registerService(WindowHistory.class, GwtWindowHistory::new);
        registerService(History.class, BrowserHistory::new);
        registerService(JsonProvider.class, GwtJsonObject::create);
        registerService(ResourceServiceProvider.class, GwtResourceServiceProviderImpl::new);
        registerService(LoggerProvider.class, GwtLoggerProviderImpl::new);
        registerService(WebSocketServiceProvider.class, GwtWebSocketServiceProviderImpl::new);
        registerService(NumbersProvider.class, StandardNumbersProviderImpl::new);
        registerService(ShutdownProvider.class, GwtShutdownProviderImpl::new);
        registerService(LocalStorageProvider.class, GwtLocalStorageProviderImpl::new);
        registerService(SessionStorageProvider.class, GwtSessionStorageProviderImpl::new);
        registerService(ApplicationModule.class, MonolithModule::new, UpdateModule::new, QueryPushModule::new, QueryModule::new, BusCallModule::new);
    }

}