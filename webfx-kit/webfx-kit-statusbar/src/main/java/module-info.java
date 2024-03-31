// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.statusbar {

    // Direct dependencies modules
    requires javafx.graphics;
    requires webfx.platform.service;

    // Exported packages
    exports dev.webfx.kit.statusbar;
    exports dev.webfx.kit.statusbar.spi;

    // Used services
    uses dev.webfx.kit.statusbar.spi.StatusBarProvider;

}