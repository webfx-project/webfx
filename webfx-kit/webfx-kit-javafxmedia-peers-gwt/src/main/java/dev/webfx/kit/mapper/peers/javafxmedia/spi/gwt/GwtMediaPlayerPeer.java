package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLAudioElement;
import elemental2.dom.Response;
import elemental2.media.AudioBufferSourceNode;
import elemental2.media.AudioContext;
import javafx.scene.media.Media;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    private static final AudioContext[] AUDIO_CONTEXTS = new AudioContext[7];
    private final AudioContext audioContext;
    private static int INDEX = -1;
    private int index;
    private AudioBufferSourceNode bufferSource;
    private final HTMLAudioElement audio; // alternative option for local files (as window.fetch() raises a CORS exception)
    boolean playWhenReady, loopWhenReady;

    public GwtMediaPlayerPeer(Media media) {
        String url = media.getUrl();
        if (url == null) {
            audio = null;
            audioContext = null;
        } else if (url.startsWith("file") || !url.startsWith("http") && DomGlobal.window.location.protocol.equals("file:")) {
            audio = (HTMLAudioElement) DomGlobal.document.createElement("audio");
            audio.src = url;
            audioContext = null;
        } else {
            audio = null;
            INDEX++;
            if (INDEX >= AUDIO_CONTEXTS.length)
                INDEX = 0;
            AudioContext ac = AUDIO_CONTEXTS[index = INDEX];
            if (ac == null)
                ac = AUDIO_CONTEXTS[INDEX] = new AudioContext();
            audioContext = ac;
            DomGlobal.window.fetch(url)
                    .then(Response::arrayBuffer)
                    .then(audioContext::decodeAudioData)
                    .then(buffer -> {
                        bufferSource = audioContext.createBufferSource();
                        bufferSource.buffer = buffer;
                        bufferSource.connect(audioContext.destination);
                        if (loopWhenReady)
                            bufferSource.loop = true;
                        if (playWhenReady)
                            bufferSource.start();
                        return null;
                    });
        }
    }

    private boolean isWebAudioApi() {
        return audioContext != null;
    }

    @Override
    public void setCycleCount(int cycleCount) {
        boolean loop = cycleCount == -1;
        if (!isWebAudioApi())
            audio.loop = loop;
        else if (bufferSource != null)
            bufferSource.loop = loop;
        else
            loopWhenReady = true;

    }

    @Override
    public void play() {
        if (!isWebAudioApi())
            audio.play();
        else if (bufferSource != null)
            bufferSource.start();
        else
            playWhenReady = true;
    }

    @Override
    public void pause() {
        if (!isWebAudioApi())
            audio.pause();
        else
            audioContext.suspend();
    }

    @Override
    public void stop() {
        if (!isWebAudioApi()) {
            audio.pause();
            audio.currentTime = 0;
        } else {
            if (bufferSource != null)
                bufferSource.stop();
            playWhenReady = false;
            audioContext.close();
            AUDIO_CONTEXTS[index] = null;
        }
    }

    @Override
    public void setVolume(double volume) {
        if (!isWebAudioApi())
            audio.volume = volume;
        // Not yet implemented for the web audio API
    }
}
