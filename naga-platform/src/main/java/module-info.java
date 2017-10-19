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
    exports naga.platform.activity.impl;
    exports naga.platform.activity.application;
    exports naga.platform.activity.application.impl;
    exports naga.platform.activity.composition;
    exports naga.platform.activity.composition.impl;
    exports naga.platform.bus;
    exports naga.platform.bus.call;
    exports naga.platform.client.bus;
    exports naga.platform.client.url.history;
    exports naga.platform.client.url.history.baseimpl;
    exports naga.platform.client.url.history.memory;
    exports naga.platform.client.url.location;
    exports naga.platform.client.url.location.impl;
    exports naga.platform.client.websocket;
    exports naga.platform.compression.values;
    exports naga.platform.compression.values.repeat;
    exports naga.platform.json;
    exports naga.platform.json.codec;
    exports naga.platform.json.listmap;
    exports naga.platform.json.parser;
    exports naga.platform.json.spi;
    exports naga.platform.services.auth;
    exports naga.platform.services.auth.spi;
    exports naga.platform.services.datasource;
    exports naga.platform.services.log.spi;
    exports naga.platform.services.query;
    exports naga.platform.services.auth.remote;
    exports naga.platform.services.query.remote;
    exports naga.platform.services.query.spi;
    exports naga.platform.services.resource.spi;
    exports naga.platform.services.update;
    exports naga.platform.services.update.remote;
    exports naga.platform.services.update.spi;
    exports naga.platform.spi;
    exports naga.platform.spi.client;
    exports naga.platform.spi.server;

}