import naga.platform.services.scheduler.spi.SchedulerProvider;

/**
 * @author Bruno Salmon
 */
module naga.abstractplatform.java {

    requires naga.scheduler;
    requires naga.util;
    requires naga.platform;

    requires java.sql;

    requires Java.WebSocket;
    requires static HikariCP;

    exports naga.providers.platform.abstr.java;
    exports naga.providers.platform.abstr.java.client;

    provides SchedulerProvider with naga.providers.platform.abstr.java.services.scheduler.JavaSchedulerProvider;
}