import webfx.platform.gwt.GwtPlatform;
import webfx.platforms.core.services.scheduler.spi.SchedulerProvider;
import webfx.platforms.web.WebPlatform;
import webfx.platform.gwt.services.scheduler.GwtSchedulerProviderImpl;

/**
 * @author Bruno Salmon
 */
module webfx.platform.gwt {

    requires webfx.platforms.web;
    requires webfx.platform;
    requires webfx.scheduler;
    requires webfx.util;

    requires gwt.user;
    requires jsinterop.annotations.HEAD.SNAPSHOT;

    exports webfx.platform.gwt;
    exports webfx.platform.gwt.services.resource;

    provides WebPlatform with GwtPlatform;
    provides SchedulerProvider with GwtSchedulerProviderImpl;
    //provides webfx.platform.json.spi.JsonProvider with webfx.providers.platform.client.gwt.json.GwtJsonObject; // protected constructor
}