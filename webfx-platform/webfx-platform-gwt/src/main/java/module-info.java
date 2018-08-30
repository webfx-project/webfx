import naga.platform.services.scheduler.spi.SchedulerProvider;
import naga.providers.platform.client.gwt.services.scheduler.GwtSchedulerProviderImpl;

/**
 * @author Bruno Salmon
 */
module webfx.platform.gwt {

    requires webfx.platforms.web;
    requires naga.platform;
    requires naga.scheduler;
    requires naga.util;

    requires gwt.user;
    requires jsinterop.annotations.HEAD.SNAPSHOT;

    exports naga.providers.platform.client.gwt;
    exports naga.providers.platform.client.gwt.services.resource;

    provides naga.providers.platform.abstr.web.WebPlatform with naga.providers.platform.client.gwt.GwtPlatform;
    provides SchedulerProvider with GwtSchedulerProviderImpl;
    //provides naga.platform.json.spi.JsonProvider with naga.providers.platform.client.gwt.json.GwtJsonObject; // protected constructor
}