// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.openjfx {

    // Direct dependencies modules
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.kit.javafxgraphics.peers;
    requires webfx.kit.javafxgraphics.peers.base;
    requires webfx.kit.launcher;
    requires webfx.platform.uischeduler;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.kit.launcher.spi.openjfx;
    exports dev.webfx.kit.mapper.peers.javafxcontrols.openjfx.skin;
    exports dev.webfx.kit.mapper.peers.javafxgraphics.openjfx;
    exports dev.webfx.platform.uischeduler.spi.impl.openjfx;

    // Provided services
    provides dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider with dev.webfx.kit.launcher.spi.openjfx.JavaFxWebFxKitLauncherProvider;
    provides dev.webfx.platform.uischeduler.spi.UiSchedulerProvider with dev.webfx.platform.uischeduler.spi.impl.openjfx.FxUiSchedulerProvider;

}