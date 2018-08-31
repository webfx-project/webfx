/**
 * @author Bruno Salmon
 */
module webfx.platforms.core.monolith {
    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.noreflect;
    requires webfx.lib.javacupruntime;

    requires static javafx.base;

    exports webfx.platform.activity;
    exports webfx.framework.activity.base;
    exports webfx.framework.activity.base.elementals.application;
    exports webfx.framework.activity.base.elementals.application.impl;
    exports webfx.framework.activity.base.composition;
    exports webfx.framework.activity.base.composition.impl;
    exports webfx.platform.bus;
    exports webfx.platform.bus.call;
    exports webfx.platform.client.bus;
    exports webfx.platform.client.url.history;
    exports webfx.platform.client.url.history.baseimpl;
    exports webfx.platform.client.url.history.memory;
    exports webfx.platform.client.url.location;
    exports webfx.platform.client.url.location.impl;
    exports webfx.platform.services.websocket.spi;
    exports webfx.platform.services.query.compression.values;
    exports webfx.platform.services.query.compression.repeat;
    exports webfx.platform.services.json;
    exports webfx.platform.services.json.codec;
    exports webfx.platform.services.json.spi.impl.listmap;
    exports webfx.platform.services.json.parser;
    exports webfx.platform.services.json.spi;
    exports webfx.platform.services.auth;
    exports webfx.platform.services.auth.spi;
    exports webfx.platform.services.datasource;
    exports webfx.platform.services.log.spi;
    exports webfx.platform.services.query;
    exports webfx.platform.services.auth.remote;
    exports webfx.platform.services.query.spi.remote;
    exports webfx.platform.services.query.spi;
    exports webfx.platform.services.resource.spi;
    exports webfx.platform.services.update;
    exports webfx.platform.services.update.spi.remote;
    exports webfx.platform.services.update.spi;
    exports webfx.platform.spi;
    exports webfx.platform.spi.client;
    exports webfx.platform.spi.server;

}