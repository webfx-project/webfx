// Generated by WebFx

module webfx.platform.shared.json.vertx {

    // Direct dependencies modules
    requires java.base;
    requires vertx.core;
    requires webfx.platform.shared.json;
    requires webfx.platform.shared.util;

    // Exported packages
    exports webfx.platform.shared.services.json.spi.impl.vertx;

    // Provided services
    provides webfx.platform.shared.services.json.spi.JsonProvider with webfx.platform.shared.services.json.spi.impl.vertx.VertxJsonObject;

}