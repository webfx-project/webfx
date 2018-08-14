/**
 * @author Bruno Salmon
 */
module mongoose.logic {

    requires naga.scheduler;
    requires naga.util;
    requires naga.type;
    requires naga.platform;
    requires naga.fx;
    requires naga.framework;
    requires naga.javalib.rxjava;
    requires naga.javalib.controlsfx.validation;
    requires naga.javalib.mvvmfx.validation;

    requires static javafx.controls;

    exports mongoose.actions;
    exports mongoose.activities.backend.application;
    exports mongoose.activities.frontend.application;
    exports mongoose.activities.bothends.application;
    exports mongoose.activities.backend.cloneevent;
    exports mongoose.domainmodel.loader;
    exports mongoose.domainmodel.formatters;
    exports mongoose.entities;
    exports mongoose.services.systemmetrics;
}