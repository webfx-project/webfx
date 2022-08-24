package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLAudioElement;
import elemental2.dom.Response;
import elemental2.media.AudioBuffer;
import elemental2.media.AudioBufferSourceNode;
import elemental2.media.AudioContext;
import elemental2.media.GainNode;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    // Creating one single audio context for the whole application
    // If the user has not yet interacted with the page, the audio context will be in "suspended" mode
    private static final AudioContext AUDIO_CONTEXT = new AudioContext();
    private final Media media;
    private AudioBuffer audioBuffer;
    private AudioBufferSourceNode bufferSource;
    private GainNode gainNode;
    private double volume = 1;
    private final HTMLAudioElement audio; // alternative option for local files (as window.fetch() raises a CORS exception)
    boolean fetched, playWhenReady, loopWhenReady;

    public GwtMediaPlayerPeer(Media media) {
        this.media = media;
        String url = media.getUrl();
        if (url == null)
            audio = null;
        else if (url.startsWith("file") || !url.startsWith("http") && DomGlobal.window.location.protocol.equals("file:")) {
            audio = (HTMLAudioElement) DomGlobal.document.createElement("audio");
            audio.src = url;
        } else {
            audio = null;
            fetch(false);
        }
    }

    private void fetch(boolean resumeIfSuspended) {
        if (AUDIO_CONTEXT.state.equals("suspended")) {
            if (!resumeIfSuspended)
                return;
            AUDIO_CONTEXT.resume();
        }
        DomGlobal.window.fetch(media.getUrl())
                .then(Response::arrayBuffer)
                .then(AUDIO_CONTEXT::decodeAudioData)
                .then(buffer -> {
                    audioBuffer = buffer;
                    onAudioBufferReady();
                    return null;
                });
        fetched = true;
    }

    private void onAudioBufferReady() {
        if (bufferSource != null) {
            bufferSource.buffer = audioBuffer;
            if (loopWhenReady)
                bufferSource.loop = true;
            if (playWhenReady)
                bufferSource.start();
        }
    }

    private boolean isBackupAudioApi() {
        return audio != null;
    }

    @Override
    public void setCycleCount(int cycleCount) {
        boolean loop = cycleCount == -1;
        if (isBackupAudioApi())
            audio.loop = loop;
        else if (bufferSource != null)
            bufferSource.loop = loop;
        else
            loopWhenReady = true;
    }

    @Override
    public void play() {
        if (isBackupAudioApi())
            audio.play();
        else if (bufferSource != null)
            bufferSource.start();
        else {
            if (!fetched)
                fetch(true);
            bufferSource = AUDIO_CONTEXT.createBufferSource();
            gainNode = AUDIO_CONTEXT.createGain();
            setVolume(volume);
            bufferSource.connect(gainNode);
            gainNode.connect(AUDIO_CONTEXT.destination);
            playWhenReady = true;
            if (audioBuffer != null)
                onAudioBufferReady();
        }
    }

    @Override
    public void pause() {
        if (isBackupAudioApi())
            audio.pause();
        else
            AUDIO_CONTEXT.suspend();
    }

    @Override
    public void stop() {
        if (isBackupAudioApi()) {
            audio.pause();
            audio.currentTime = 0;
        } else {
            if (bufferSource != null)
                bufferSource.stop();
            playWhenReady = false;
        }
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        if (isBackupAudioApi())
            audio.volume = volume;
        else if (gainNode != null)
            gainNode.gain.value = volume;
    }
}
