// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.launcher {

    // Direct dependencies modules
    requires java.base;
    requires javafx.graphics;
    requires webfx.platform.client.uischeduler;
    requires webfx.platform.shared.appcontainer;
    requires webfx.platform.shared.log;
    requires webfx.platform.shared.util;

    // Exported packages
    exports dev.webfx.kit.launcher;
    exports dev.webfx.kit.launcher.spi;
    exports dev.webfx.kit.launcher.spi.base;

    // Used services
    uses dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;
    uses javafx.application.Application;

    // Provided services
    provides dev.webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer with dev.webfx.kit.launcher.WebFxKitLauncherModuleInitializer;

}