import webfx.platform.services.scheduler.spi.SchedulerProvider;
import webfx.providers.platform.abstr.java.services.scheduler.JavaSchedulerProviderImpl;

/**
 * @author Bruno Salmon
 */
module webfx.platforms.java {

    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.platform;

    requires java.sql;

    requires Java.WebSocket;
    requires static HikariCP;

    exports webfx.providers.platform.abstr.java;
    exports webfx.providers.platform.abstr.java.client;

    provides SchedulerProvider with JavaSchedulerProviderImpl;
}