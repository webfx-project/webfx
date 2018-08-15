/**
 * @author Bruno Salmon
 */
module naga.platform {
    requires naga.scheduler;
    requires naga.util;
    requires naga.noreflect;
    requires naga.javalib.javacupruntime;

    requires static javafx.base;

    exports naga.platform.activity;
    exports naga.framework.activity.impl;
    exports naga.framework.activity.application;
    exports naga.framework.activity.application.impl;
    exports naga.framework.activity.composition;
    exports naga.framework.activity.composition.impl;
    exports naga.platform.bus;
    exports naga.platform.bus.call;
    exports naga.platform.client.bus;
    exports naga.platform.client.url.history;
    exports naga.platform.client.url.history.baseimpl;
    exports naga.platform.client.url.history.memory;
    exports naga.platform.client.url.location;
    exports naga.platform.client.url.location.impl;
    exports naga.platform.services.websocket.spi;
    exports naga.platform.services.query.compression.values;
    exports naga.platform.services.query.compression.repeat;
    exports naga.platform.services.json;
    exports naga.platform.services.json.codec;
    exports naga.platform.services.json.spi.impl.listmap;
    exports naga.platform.services.json.parser;
    exports naga.platform.services.json.spi;
    exports naga.platform.services.auth;
    exports naga.platform.services.auth.spi;
    exports naga.platform.services.datasource;
    exports naga.platform.services.log.spi;
    exports naga.platform.services.query;
    exports naga.platform.services.auth.remote;
    exports naga.platform.services.query.spi.remote;
    exports naga.platform.services.query.spi;
    exports naga.platform.services.resource.spi;
    exports naga.platform.services.update;
    exports naga.platform.services.update.spi.remote;
    exports naga.platform.services.update.spi;
    exports naga.platform.spi;
    exports naga.platform.spi.client;
    exports naga.platform.spi.server;

}