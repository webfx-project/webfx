// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.platform.browser.openjfx {

    // Direct dependencies modules
    requires javafx.graphics;
    requires webfx.kit.launcher;
    requires webfx.platform.browser;

    // Exported packages
    exports dev.webfx.kit.platform.browser.spi.impl.openjfx;

    // Provided services
    provides dev.webfx.platform.browser.spi.BrowserProvider with dev.webfx.kit.platform.browser.spi.impl.openjfx.OpenJFXBrowserProvider;

}