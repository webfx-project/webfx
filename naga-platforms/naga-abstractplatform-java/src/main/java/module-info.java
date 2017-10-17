/**
 * @author Bruno Salmon
 */
module naga.abstractplatform.java {

    requires naga.commons;
    requires naga.util;
    requires naga.platform;

    requires java.sql;

    requires Java.WebSocket;
    requires static HikariCP;

    exports naga.providers.platform.abstr.java;
    exports naga.providers.platform.abstr.java.client;
}