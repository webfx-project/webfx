// Generated by WebFx

module webfx.kit.launcher {

    // Direct dependencies modules
    requires java.base;
    requires javafx.graphics;
    requires webfx.platform.client.uischeduler;
    requires webfx.platform.shared.appcontainer;
    requires webfx.platform.shared.log;
    requires webfx.platform.shared.util;

    // Exported packages
    exports webfx.kit.launcher;
    exports webfx.kit.launcher.spi;
    exports webfx.kit.launcher.spi.base;

    // Used services
    uses javafx.application.Application;
    uses webfx.kit.launcher.spi.WebFxKitLauncherProvider;

    // Provided services
    provides webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer with webfx.kit.launcher.WebFxKitLauncherModuleInitializer;

}