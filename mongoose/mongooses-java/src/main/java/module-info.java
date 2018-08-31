/**
 * @author Bruno Salmon
 */
module mongooses.java {

    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.platform;
    requires webfx.framework;
    requires mongooses.core;

    requires jdk.management;

    exports mongoose.activities.server;
}