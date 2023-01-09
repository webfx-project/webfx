// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.kit.platform.audio.openjfx {

    // Direct dependencies modules
    requires javafx.media;
    requires webfx.platform.audio;

    // Exported packages
    exports dev.webfx.kit.platform.audio.spi.impl.openjfx;

    // Provided services
    provides dev.webfx.platform.audio.spi.AudioServiceProvider with dev.webfx.kit.platform.audio.spi.impl.openjfx.OpenJFXAudioServiceProvider;

}