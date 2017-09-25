/**
 * @author Bruno Salmon
 */
module naga.platform.gwt {

    requires naga.abstractplatform.web;
    requires naga.platform;
    requires naga.commons;

    requires gwt.user.HEAD.SNAPSHOT;
    requires jsinterop.annotations.HEAD.SNAPSHOT;

    exports naga.providers.platform.client.gwt;
    exports naga.providers.platform.client.gwt.services.resource;

    provides naga.providers.platform.abstr.web.WebPlatform with naga.providers.platform.client.gwt.GwtPlatform;

}