// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.launcher {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.graphics;
    requires webfx.platform.boot;
    requires webfx.platform.console;
    requires webfx.platform.service;
    requires webfx.platform.uischeduler;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.kit.launcher;
    exports dev.webfx.kit.launcher.aria;
    exports dev.webfx.kit.launcher.spi;
    exports dev.webfx.kit.launcher.spi.impl.base;

    // Used services
    uses dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;
    uses javafx.application.Application;

    // Provided services
    provides dev.webfx.platform.boot.spi.ApplicationModuleBooter with dev.webfx.kit.launcher.WebFxKitLauncherModuleBooter;

}