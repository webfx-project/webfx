// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.platform.visibility.openjfx {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.graphics;
    requires webfx.kit.launcher;
    requires webfx.platform.visibility;

    // Exported packages
    exports dev.webfx.kit.platform.visibility.spi.impl.openjfx;

    // Provided services
    provides dev.webfx.platform.visibility.spi.VisibilityProvider with dev.webfx.kit.platform.visibility.spi.impl.openjfx.OpenJFXVisibilityProvider;

}