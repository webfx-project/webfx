// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.statusbar.gluon {

    // Direct dependencies modules
    requires com.gluonhq.attach.statusbar;
    requires javafx.graphics;
    requires webfx.kit.statusbar;
    requires webfx.platform.console;
    requires webfx.platform.uischeduler;

    // Exported packages
    exports dev.webfx.kit.statusbar.spi.impl.gluon;

    // Provided services
    provides dev.webfx.kit.statusbar.spi.StatusBarProvider with dev.webfx.kit.statusbar.spi.impl.gluon.GluonStatusBarProvider;

}