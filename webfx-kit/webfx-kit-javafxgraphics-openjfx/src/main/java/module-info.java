// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.javafxgraphics.openjfx {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.kit.javafxgraphics.peers;
    requires webfx.kit.javafxgraphics.peers.base;
    requires webfx.kit.launcher;
    requires webfx.kit.util;
    requires webfx.platform.console;
    requires webfx.platform.os;
    requires webfx.platform.scheduler;
    requires webfx.platform.uischeduler;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.kit.launcher.spi.impl.openjfx;
    exports dev.webfx.kit.mapper.peers.javafxcontrols.openjfx.skin;
    exports dev.webfx.kit.mapper.peers.javafxgraphics.openjfx;
    exports dev.webfx.platform.uischeduler.spi.impl.openjfx;

    // Provided services
    provides dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider with dev.webfx.kit.launcher.spi.impl.openjfx.OpenJFXWebFxKitLauncherProvider;
    provides dev.webfx.platform.uischeduler.spi.UiSchedulerProvider with dev.webfx.platform.uischeduler.spi.impl.openjfx.OpenJFXUiSchedulerProvider;

}