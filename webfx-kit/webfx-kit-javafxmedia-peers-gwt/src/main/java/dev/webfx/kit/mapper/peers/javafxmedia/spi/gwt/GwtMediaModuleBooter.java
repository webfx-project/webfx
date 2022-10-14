package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.platform.boot.spi.ApplicationModuleBooter;
import elemental2.dom.AddEventListenerOptions;
import elemental2.dom.DomGlobal;
import javafx.scene.media.Media;

/**
 *
 * The purpose of this module booter is to ensure that the sound will play ok on iOS and iPadOS after the first user
 * interaction.
 * ==========================
 * Description of the problem
 * ==========================
 * Other OS automatically unlock the sound on first user interaction, even if the application code doesn't request
 * playing sound at this time, it can still start playing sound any time later, even not necessarily during a user
 * interaction. On iOS and iPadOS however, this sound unlocking is not automatic, it happens only if the application
 * requests playing sound DURING the user interaction.
 * Because of this difference, if the JavaFX application code tries to start playing sound using setOnMouseClicked(),
 * this won't work (it will work however with setOnMousePressed() or setOnMouseReleased()). This is due to the way
 * WebFX emulates the JavaFX click event, which is not based on the JavaScript "click" event as opposed to the other
 * events, because JavaFX has its own way to fire it when detecting the mouse released, and WebFX postpones this process
 * (see HtmlScenePeer.java, installMouseListeners() and passHtmlMouseEventOnToFx() methods).
 * ===========================
 * Description of the solution
 * ===========================
 * This module booter will automatically detect the first user interaction and play a silent sound for a very short time
 * during that interaction, causing the sound unlocking even on iOS and iPadOS. Then, if the JavaFX application requests
 * playing sound using setOnMouseClicked(), it will work because the sound unlocking has previously been done.
 *
 * @author Bruno Salmon
 */
public class GwtMediaModuleBooter implements ApplicationModuleBooter {

    @Override
    public String getModuleName() {
        return "webfx-kit-javafxmedia-peer-gwt";
    }

    @Override
    public int getBootLevel() {
        return APPLICATION_BOOT_LEVEL;
    }

    @Override
    public void bootModule() {
        AddEventListenerOptions options = AddEventListenerOptions.create();
        options.setPassive(true); // We promise we won't call preventDefault()
        options.setCapture(true); // Our listener will be called first
        options.setOnce(true); // We need the listener to be called only once (will be automatically removed after that)
        DomGlobal.document.body.addEventListener("mousedown", e -> { // We use "mousedown" to detect the first user interaction
            String tinySilentMp3Data = "data:audio/mpeg;base64,SUQzBAAAAAABEVRYWFgAAAAtAAADY29tbWVudABCaWdTb3VuZEJhbmsuY29tIC8gTGFTb25vdGhlcXVlLm9yZwBURU5DAAAAHQAAA1N3aXRjaCBQbHVzIMKpIE5DSCBTb2Z0d2FyZQBUSVQyAAAABgAAAzIyMzUAVFNTRQAAAA8AAANMYXZmNTcuODMuMTAwAAAAAAAAAAAAAAD/80DEAAAAA0gAAAAATEFNRTMuMTAwVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQsRbAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQMSkAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
            new GwtMediaPlayerPeer(new Media(tinySilentMp3Data)).play(); // This will unlock the sound
        }, options);
    }

}
