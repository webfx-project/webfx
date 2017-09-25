/**
 * @author Bruno Salmon
 */
module mongoose.logic {

    requires naga.commons;
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
    exports mongoose.activities.shared.application;
    exports mongoose.activities.backend.event.clone;
    exports mongoose.domainmodel.loader;
    exports mongoose.domainmodel.format;
    exports mongoose.entities;
    exports mongoose.spi.metrics;
}