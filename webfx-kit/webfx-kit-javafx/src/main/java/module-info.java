// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.javafx {

    // Direct dependencies modules
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.kit.javafxgraphics.peers;
    requires webfx.kit.javafxgraphics.peers.base;
    requires webfx.kit.launcher;
    requires webfx.platform.client.uischeduler;
    requires webfx.platform.shared.util;

    // Exported packages
    exports dev.webfx.kit.launcher.spi.javafx;
    exports dev.webfx.kit.mapper.peers.javafxcontrols.javafx.skin;
    exports dev.webfx.kit.mapper.peers.javafxgraphics.javafx;
    exports dev.webfx.platform.client.services.uischeduler.spi.impl.javafx;

    // Provided services
    provides dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider with dev.webfx.kit.launcher.spi.javafx.JavaFxWebFxKitLauncherProvider;
    provides dev.webfx.platform.client.services.uischeduler.spi.UiSchedulerProvider with dev.webfx.platform.client.services.uischeduler.spi.impl.javafx.FxUiSchedulerProvider;

}