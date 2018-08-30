/**
 * @author Bruno Salmon
 */
module webfx.platform.vertx {

    requires webfx.platforms.java;
    requires naga.platform;

    requires naga.scheduler;
    requires naga.util;

    requires vertx.core;
    requires vertx.mysql.postgresql.client;
    requires vertx.jdbc.client;
    requires vertx.web;

    exports naga.providers.platform.server.vertx.verticles;
    exports naga.providers.platform.server.vertx.util;
}