/**
 * @author Bruno Salmon
 */
module mongoose.logic.java {

    requires naga.scheduler;
    requires naga.util;
    requires naga.platform;
    requires naga.framework;
    requires mongoose.logic;

    requires jdk.management;

    exports mongoose.activities.server;
}