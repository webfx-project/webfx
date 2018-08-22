/**
 * @author Bruno Salmon
 */
module webfx.platformport.vertx {

    requires webfx.platformports.java;
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