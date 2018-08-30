/**
 * @author Bruno Salmon
 */
module mongooses.java {

    requires naga.scheduler;
    requires naga.util;
    requires naga.platform;
    requires webfx.framework;
    requires mongooses.core;

    requires jdk.management;

    exports mongoose.activities.server;
}