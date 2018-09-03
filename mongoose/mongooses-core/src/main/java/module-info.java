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

    exports mongooses.core.actions;
    exports mongooses.core.activities.backend.application;
    exports mongooses.core.activities.frontend.application;
    exports mongooses.core.activities.sharedends.application;
    exports mongooses.core.activities.backend.cloneevent;
    exports mongooses.core.domainmodel.loader;
    exports mongooses.core.domainmodel.formatters;
    exports mongooses.core.entities;
    exports mongooses.core.services.systemmetrics;
}