// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.statusbar {

    // Direct dependencies modules
    requires java.base;
    requires javafx.graphics;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.kit.statusbar;
    exports dev.webfx.kit.statusbar.spi;

    // Used services
    uses dev.webfx.kit.statusbar.spi.StatusBarProvider;

}