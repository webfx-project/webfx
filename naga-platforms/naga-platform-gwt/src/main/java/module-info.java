/**
 * @author Bruno Salmon
 */
module naga.platform.gwt {

    requires naga.abstractplatform.web;
    requires naga.platform;
    requires naga.scheduler;
    requires naga.util;

    requires gwt.user;
    requires jsinterop.annotations.HEAD.SNAPSHOT;

    exports naga.providers.platform.client.gwt;
    exports naga.providers.platform.client.gwt.services.resource;

    provides naga.providers.platform.abstr.web.WebPlatform with naga.providers.platform.client.gwt.GwtPlatform;
    provides naga.scheduler.SchedulerProvider with naga.providers.platform.client.gwt.scheduler.GwtSchedulerProvider;
    //provides naga.platform.json.spi.JsonProvider with naga.providers.platform.client.gwt.json.GwtJsonObject; // protected constructor
}