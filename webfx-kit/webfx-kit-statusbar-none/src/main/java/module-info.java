// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.statusbar.none {

    // Direct dependencies modules
    requires javafx.graphics;
    requires webfx.kit.statusbar;

    // Exported packages
    exports dev.webfx.kit.statusbar.spi.impl.none;

    // Provided services
    provides dev.webfx.kit.statusbar.spi.StatusBarProvider with dev.webfx.kit.statusbar.spi.impl.none.NoneStatusBarProvide;

}