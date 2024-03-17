// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.webgl {

    // Direct dependencies modules
    requires javafx.graphics;
    requires javafx.media;
    requires webfx.platform.typedarray;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.kit.webgl;
    exports dev.webfx.kit.webgl.spi;

    // Used services
    uses dev.webfx.kit.webgl.spi.WebGLProvider;

}