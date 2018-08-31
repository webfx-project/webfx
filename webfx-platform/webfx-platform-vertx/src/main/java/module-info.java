/**
 * @author Bruno Salmon
 */
module webfx.platform.vertx {

    requires webfx.platforms.java;
    requires webfx.platform;

    requires webfx.scheduler;
    requires webfx.util;

    requires vertx.core;
    requires vertx.mysql.postgresql.client;
    requires vertx.jdbc.client;
    requires vertx.web;

    exports webfx.providers.platform.server.vertx.verticles;
    exports webfx.providers.platform.server.vertx.util;
}