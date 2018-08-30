/**
 * @author Bruno Salmon
 */
module webfx.fxkit.javafx {
    requires naga.scheduler;
    requires naga.util;
    requires naga.type;
    requires webfx.fxkits.core;

    requires javafx.controls;
    requires javafx.web;
    requires jdk.jsobject;

    exports naga.fx.spi.javafx;

    provides naga.fx.spi.Toolkit with naga.fx.spi.javafx.JavaFxToolkit;
}