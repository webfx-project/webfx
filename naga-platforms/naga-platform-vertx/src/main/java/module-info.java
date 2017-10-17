/**
 * @author Bruno Salmon
 */
module naga.platform.vertx {

    requires naga.abstractplatform.java;
    requires naga.platform;

    requires naga.commons;
    requires naga.util;

    requires vertx.core;
    requires vertx.mysql.postgresql.client;
    requires vertx.jdbc.client;
    requires vertx.web;

    exports naga.providers.platform.server.vertx.verticles;
    exports naga.providers.platform.server.vertx.util;
}