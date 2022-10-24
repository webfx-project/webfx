package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.core.Uint8Array;
import elemental2.dom.*;
import elemental2.media.*;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;

import java.time.Duration;

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
    private HTMLMediaElement mediaElement; // alternative option for local files (as window.fetch() raises a CORS exception)
    private boolean fetched, playWhenReady, loopWhenReady;
    private double mediaStartTimeMillis = -1;
    private AnalyserNode analyser;
    private Uint8Array byteTimeArray;
    private Uint8Array byteFrequencyArray;
    //private Float32Array floatTimeArray;
    //private Float32Array floatFrequencyArray;
    private int arraySize = 16;
    private AudioSpectrumListener listener;
    private Scheduled listenerScheduled;
    private double audioSpectrumInterval;
    private float[] magnitudes, phases;
    private Runnable onEndOfMedia, onPlaying;

    public GwtMediaPlayerPeer(Media media) {
        this.media = media;
        String url = media.getSource();
        if (url.startsWith("file") || !url.startsWith("http") && DomGlobal.window.location.protocol.equals("file:"))
            setMediaElement((HTMLAudioElement) DomGlobal.document.createElement("audio"));
        else {
            mediaElement = null;
            fetch(false);
        }
    }

    public void setMediaElement(HTMLMediaElement mediaElement) {
        this.mediaElement = mediaElement;
        mediaElement.onplaying = p0 -> {
            doOnPlaying();
            return null;
        };
        mediaElement.onended = p0 -> {
            doOnEnded();
            return null;
        };
        mediaElement.src = media.getSource();
        if (loopWhenReady)
            mediaElement.loop = true;
    }

    @Override
    public Media getMedia() {
        return media;
    }

    private void fetch(boolean resumeIfSuspended) {
        if (AUDIO_CONTEXT.state.equals("suspended")) {
            if (!resumeIfSuspended)
                return;
            AUDIO_CONTEXT.resume();
        }
        DomGlobal.window.fetch(media.getSource())
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
            bufferSource.loop = loopWhenReady;
            if (playWhenReady)
                startBufferSource();
        }
    }

    private void startBufferSource() {
        bufferSource.start();
        bufferSource.onended = p0 -> doOnEnded();
    }

    private void captureMediaStartTimeNow() {
        mediaStartTimeMillis = mediaCurrentTimeMillis();
    }

    private boolean hasMediaElement() {
        return mediaElement != null;
    }

    @Override
    public void setCycleCount(int cycleCount) {
        boolean loop = cycleCount == -1;
        if (hasMediaElement())
            mediaElement.loop = loop;
        else if (bufferSource != null)
            bufferSource.loop = loop;
        else
            loopWhenReady = loop;
    }

    @Override
    public void play() {
        if (hasMediaElement()) {
            setVolume(volume);
            mediaElement.play();
            captureMediaStartTimeNow();
        } else {
            if (bufferSource != null)
                startBufferSource();
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
            scheduleListener();
        }
    }

    private void scheduleListener() {
        if (listener != null && listenerScheduled == null && audioSpectrumInterval > 0) {
            //DomGlobal.window.console.log("Scheduling listener, audioSpectrumInterval = " + audioSpectrumInterval);
            listenerScheduled = UiScheduler.schedulePeriodic((long) (audioSpectrumInterval * 1000), () -> {
                //DomGlobal.window.console.log("Calling spectrum listener");
                AnalyserNode analyzer = getOrCreateAnalyzer();
                analyzer.getByteFrequencyData(byteFrequencyArray);
                analyzer.getByteTimeDomainData(byteTimeArray);
                for (int i = 0; i < arraySize; i++) {
                    magnitudes[i] = -60 + 60 * byteFrequencyArray.getAt(i).floatValue() / 256;
                    phases[i] = byteTimeArray.getAt(i).floatValue();
                }
                listener.spectrumDataUpdate(elapsedTimeMillis(), audioSpectrumInterval, magnitudes, phases);
            });
        }
    }

    private void unscheduleListener() {
        if (listenerScheduled != null) {
            listenerScheduled.cancel();
            listenerScheduled = null;
        }
    }

    @Override
    public void pause() {
        if (hasMediaElement())
            mediaElement.pause();
        else
            AUDIO_CONTEXT.suspend();
    }

    @Override
    public void stop() {
        if (hasMediaElement()) {
            mediaElement.pause();
            mediaElement.currentTime = 0;
        } else {
            if (bufferSource != null)
                try {
                    bufferSource.stop(); // May raise an exception
                } catch (Throwable th) {
                    DomGlobal.console.log(th);
                } finally {
                    bufferSource = null;
                }
            playWhenReady = false;
            unscheduleListener();
        }
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        if (hasMediaElement())
            mediaElement.volume = volume;
        else if (gainNode != null)
            gainNode.gain.value = volume;
    }

    @Override
    public Duration getCurrentTime() {
        if (mediaStartTimeMillis < 0) {
            if (!hasMediaElement()) {
                getOrCreateAnalyzer().getByteTimeDomainData(byteTimeArray);
                if (byteTimeArray.getAt(0) != 128)
                    captureMediaStartTimeNow();
            }
            return Duration.ZERO;
        }
        return Duration.ofMillis((long) elapsedTimeMillis());
    }

    private double elapsedTimeMillis() {
        return mediaCurrentTimeMillis() - mediaStartTimeMillis;
    }

    private AnalyserNode getOrCreateAnalyzer() {
        if (analyser == null) {
            analyser = AUDIO_CONTEXT.createAnalyser();
            analyser.frequencyBinCount = arraySize;
            analyser.minDecibels = -60;
            analyser.maxDecibels = 0;
            byteTimeArray = new Uint8Array(arraySize);
            byteFrequencyArray = new Uint8Array(arraySize);
            //floatTimeArray = new Float32Array(arraySize);
            //floatFrequencyArray = new Float32Array(arraySize);
            bufferSource.connect(analyser);
            analyser.connect(gainNode);
        }
        return analyser;
    }

    private double mediaCurrentTimeMillis() {
        return hasMediaElement() ? System.currentTimeMillis() : AUDIO_CONTEXT.currentTime * 1000;
    }

    @Override
    public void setAudioSpectrumInterval(double value) {
        audioSpectrumInterval = value;
    }

    @Override
    public void setAudioSpectrumNumBands(int value) {
        arraySize = value;
        magnitudes = new float[value];
        phases = new float[value];
        if (analyser != null)
            analyser.frequencyBinCount = arraySize;
    }

    @Override
    public void setAudioSpectrumListener(AudioSpectrumListener listener) {
        this.listener = listener;
    }

    @Override
    public void setOnEndOfMedia(Runnable onEndOfMedia) {
        this.onEndOfMedia = onEndOfMedia;
    }

    @Override
    public void setOnPlaying(Runnable onPlaying) {
        this.onPlaying = onPlaying;
    }

    private void doOnPlaying() {
        if (onPlaying != null)
            onPlaying.run();
    }

    private void doOnEnded() {
        if (onEndOfMedia != null)
            onEndOfMedia.run();
    }
}
