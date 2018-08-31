import webfx.platform.services.scheduler.spi.SchedulerProvider;
import webfx.providers.platform.client.gwt.services.scheduler.GwtSchedulerProviderImpl;

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

    exports webfx.providers.platform.client.gwt;
    exports webfx.providers.platform.client.gwt.services.resource;

    provides webfx.providers.platform.abstr.web.WebPlatform with webfx.providers.platform.client.gwt.GwtPlatform;
    provides SchedulerProvider with GwtSchedulerProviderImpl;
    //provides webfx.platform.json.spi.JsonProvider with webfx.providers.platform.client.gwt.json.GwtJsonObject; // protected constructor
}