package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.core.Uint8Array;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLAudioElement;
import elemental2.dom.HTMLMediaElement;
import elemental2.dom.Response;
import elemental2.media.*;
import javafx.scene.media.AudioSpectrumListener;

import java.time.Duration;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    // Creating one single audio context for the whole application
    // If the user has not yet interacted with the page, the audio context will be in "suspended" mode
    private static final AudioContext AUDIO_CONTEXT = new AudioContext();
    private final String mediaUrl;
    private AudioBuffer audioBuffer;
    private AudioBufferSourceNode bufferSource;
    private boolean bufferSourcePlayed;
    private MediaElementAudioSourceNode mediaElementSource;
    private GainNode gainNode;
    private double volume = 1;
    private boolean mute;
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
    private int cycleCount = 1;
    private int playedCycleCount = 0;
    private Runnable onEndOfMedia, onPlaying;

    public GwtMediaPlayerPeer(String mediaUrl, boolean audioClip) {
        this.mediaUrl = mediaUrl;
        if (!audioClip || mediaUrl.startsWith("file") || !mediaUrl.startsWith("http") && DomGlobal.window.location.protocol.equals("file:"))
            setMediaElement((HTMLAudioElement) DomGlobal.document.createElement("audio"));
        else
            fetchAudioBuffer(false);
    }

    public void setMediaElement(HTMLMediaElement mediaElement) { // GwtMediaViewPeer also calls this method to pass the video element
        this.mediaElement = mediaElement;
        mediaElement.onplaying = p0 -> {
            doOnPlaying();
            return null;
        };
        mediaElement.onended = p0 -> {
            doOnEnded();
            return null;
        };
        mediaElement.src = mediaUrl;
        if (loopWhenReady)
            mediaElement.loop = true;
        if (mute)
            mediaElement.muted = true;
        mediaElement.crossOrigin = "anonymous"; // to avoid this possible error: MediaElementAudioSource outputs zeros due to CORS access restrictions
    }

    private void fetchAudioBuffer(boolean resumeIfSuspended) {
        if (isAudioContextReady(resumeIfSuspended)) {
            DomGlobal.window.fetch(mediaUrl)
                    .then(Response::arrayBuffer)
                    .then(AUDIO_CONTEXT::decodeAudioData)
                    .then(buffer -> {
                        audioBuffer = buffer;
                        onAudioBufferReady();
                        return null;
                    });
            fetched = true;
        }
    }

    private boolean isAudioContextReady(boolean resumeIfSuspended) {
        if (AUDIO_CONTEXT.state.equals("suspended")) {
            if (!resumeIfSuspended)
                return false;
            AUDIO_CONTEXT.resume();
        }
        return true;
    }

    private void onAudioBufferReady() {
        if (bufferSource != null && !bufferSourcePlayed) {
            bufferSource.buffer = audioBuffer;
            bufferSource.loop = loopWhenReady;
            if (playWhenReady)
                startBufferSource();
        }
    }

    private void startBufferSource() {
        if (bufferSource.playbackRate.value == 0) // This means that the AudioBufferSourceNode was paused
            bufferSource.playbackRate.value = 1;  // We reestablished the normal speed to resume
        else {
            bufferSource.start();
            bufferSource.onended = p0 -> doOnEnded();
            bufferSourcePlayed = true; // a buffer source can be played only once, so this flag indicates a new bufferSource is needed to play this sound again
        }
    }

    private void captureMediaStartTimeNow() {
        mediaStartTimeMillis = mediaCurrentTimeMillis();
    }

    private boolean hasMediaElement() {
        return mediaElement != null;
    }

    @Override
    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
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
        playedCycleCount = 0;
        replay();
    }

    private void replay() {
        if (hasMediaElement()) {
            setVolume(volume);
            // Muting the media if asked or if the user hasn't yet interacted (otherwise play() will raise an exception)
            if (!GwtMediaModuleBooter.mediaRequiresUserInteractionFirst())
                mediaElement.muted = mute;
            else {
                mediaElement.muted = true;
                GwtMediaModuleBooter.runOnFirstUserInteraction(() -> mediaElement.muted = mute);
            }
            // mediaElementSource is required for possible spectrum analysis
            if (isAudioContextReady(true) && mediaElementSource == null) {
                mediaElementSource = AUDIO_CONTEXT.createMediaElementSource(mediaElement);
                mediaElementSource.connect(AUDIO_CONTEXT.destination);
            }
            mediaElement.play();
            captureMediaStartTimeNow();
        } else {
            // If the buffer source is already ready to play, we start it now
            if (bufferSource != null && !bufferSourcePlayed /* can't be played twice */ )
                startBufferSource();
            else { // the buffer is not yet ready, or has already been played
                if (!fetched)
                    fetchAudioBuffer(true);
                bufferSource = AUDIO_CONTEXT.createBufferSource();
                bufferSourcePlayed = false;
                gainNode = AUDIO_CONTEXT.createGain();
                setVolume(volume);
                bufferSource.connect(gainNode);
                gainNode.connect(AUDIO_CONTEXT.destination);
                playWhenReady = true;
                if (audioBuffer != null)
                    onAudioBufferReady();
            }
        }
        scheduleListener();
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
        else if (bufferSource != null)
            bufferSource.playbackRate.value = 0; // Using this trick as there is no pause() method in AudioBufferSourceNode
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
        }
        unscheduleListener();
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
    public void setMute(boolean mute) {
        this.mute = mute;
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
            if (bufferSource != null) {
                bufferSource.connect(analyser);
                analyser.connect(gainNode);
            } else if (mediaElementSource != null) {
                mediaElementSource.connect(analyser);
                analyser.connect(AUDIO_CONTEXT.destination);
            }
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
        playedCycleCount++;
        if (playedCycleCount < cycleCount)
            replay();
        else if (onEndOfMedia != null)
            onEndOfMedia.run();
    }
}
