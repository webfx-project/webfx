/**
 * @author Bruno Salmon
 */
module mongooses.core {

    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.type;
    requires webfx.platform;
    requires webfx.fxkits.core;
    requires webfx.framework;
    requires webfx.lib.rxjava;
    requires webfx.lib.controlsfx.validation;
    requires webfx.lib.mvvmfx.validation;

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