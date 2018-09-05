/**
 * @author Bruno Salmon
 */

package java.util;

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
import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.numbers.providers.StandardNumbersProviderImpl;
import webfx.platforms.core.util.numbers.spi.NumbersProvider;
import webfx.platforms.core.spi.Platform;
import webfx.platform.gwt.GwtPlatform;

class GwtPlatformServiceLoader extends GwtServiceLoader {

    static {
        registerService(Platform.class, GwtPlatform::new);
        registerService(SchedulerProvider.class, GwtSchedulerProviderImpl::new);
        registerService(JsonProvider.class, GwtJsonObject::create);
        registerService(ResourceServiceProvider.class, GwtResourceServiceProviderImpl::new);
        registerService(LoggerProvider.class, GwtLoggerProviderImpl::new);
        registerService(WebSocketServiceProvider.class, GwtWebSocketServiceProviderImpl::new);
        registerService(NumbersProvider.class, StandardNumbersProviderImpl::new);
        registerService(ShutdownProvider.class, GwtShutdownProviderImpl::new);
        registerService(LocalStorageProvider.class, GwtLocalStorageProviderImpl::new);
        registerService(SessionStorageProvider.class, GwtSessionStorageProviderImpl::new);
    }

}