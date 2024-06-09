// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.cssprotocol.openjfx.plugin {

    // Direct dependencies modules
    requires webfx.kit.launcher;

    // Exported packages
    exports dev.webfx.kit.css.protocol;

    // Provided services
    provides java.net.spi.URLStreamHandlerProvider with dev.webfx.kit.css.protocol.WebFXCssProtocol;

}