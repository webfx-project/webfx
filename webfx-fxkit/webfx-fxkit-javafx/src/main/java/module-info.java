import webfx.fxkit.javafx.JavaFxFxKit;
import webfx.fxkits.core.spi.FxKit;

/**
 * @author Bruno Salmon
 */
module webfx.fxkit.javafx {
    requires webfx.scheduler;
    requires webfx.util;
    requires webfx.type;
    requires webfx.fxkits.core;

    requires javafx.controls;
    requires javafx.web;
    requires jdk.jsobject;

    exports webfx.fx.spi.javafx;

    provides FxKit with JavaFxFxKit;
}