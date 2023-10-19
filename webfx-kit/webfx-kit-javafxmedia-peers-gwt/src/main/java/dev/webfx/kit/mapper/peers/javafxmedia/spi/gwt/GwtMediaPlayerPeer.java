package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import com.google.gwt.storage.client.Storage;
import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.scheduler.Scheduler;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.core.Uint8Array;
import elemental2.dom.*;
import elemental2.media.*;
import elemental2.promise.Promise;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    private static final boolean PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP = !"false".equals(Storage.getLocalStorageIfSupported().getItem("PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP"));
    ;
    private static final long MEDIA_PLAYER_CURRENT_TIME_SYNC_RATE_MILLIS = 250; // Same rate as mediaElement.ontimeupdate
    private static final boolean SYNC_START_TIME_WITH_AUDIO_BUFFER_FIRST_SOUND_DETECTION = true;

    // Creating one single audio context for the whole application
    // If the user has not yet interacted with the page, the audio context will be in "suspended" mode
    private static final AudioContext AUDIO_CONTEXT = new AudioContext();
    private final MediaPlayer mediaPlayer;
    private ObjectProperty<Duration> mediaPlayerCurrentTimeProperty;
    private final String mediaUrl;
    private final boolean audioClip;
    private AudioBuffer audioBuffer;
    private AudioBufferSourceNode bufferSource;
    private double bufferSourceStartOffset;
    private boolean audioBufferFirstSoundDetected; // used only if SYNC_START_TIME_WITH_AUDIO_BUFFER_FIRST_SOUND_DETECTION = true
    private boolean seekingBufferSource;
    private boolean bufferSourceWasPlayingOnSeeking;
    private boolean bufferSourcePlayed;
    private final StopWatch bufferSourceStopWatchMillis;
    private Scheduled mediaPlayerCurrentTimePeriodicSyncer;
    private MediaElementAudioSourceNode mediaElementSource;
    private GainNode gainNode;
    private double volume = 1;
    private boolean mute;
    private HTMLMediaElement mediaElement; // alternative option for local files (as window.fetch() raises a CORS exception)
    private boolean fetched, playWhenReady, loopWhenReady;
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

    public GwtMediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
        this.mediaPlayer = mediaPlayer;
        this.mediaUrl = mediaPlayer.getMedia().getSource();
        this.audioClip = audioClip;
        if (!audioClip && PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP || mediaUrl.startsWith("file") || !mediaUrl.startsWith("http") && DomGlobal.window.location.protocol.equals("file:")) {
            // Note: Assuming is for audio, but in case of videos, this element will be replaced later by GwtMediaViewPeer
            setMediaElement((HTMLAudioElement) DomGlobal.document.createElement("audio"));
            bufferSourceStopWatchMillis = null;
        } else {
            bufferSourceStopWatchMillis = audioClip ? null : new StopWatch(() -> secondsDoubleToMillisLong(AUDIO_CONTEXT.currentTime));
            fetchAudioBuffer(false);
        }
    }

    /*static {
        if (!isAudioContextReady(false)) {
            AUDIO_CONTEXT.onstatechange = p0 -> {
                Console.log("AudioContext state changed to " + AUDIO_CONTEXT.state);
                return null;
            };
        }
    }*/

    private static boolean isAudioContextReady(boolean resumeIfSuspended) {
        if (AUDIO_CONTEXT.state.equalsIgnoreCase("suspended")) {
            if (!resumeIfSuspended)
                return false;
            AUDIO_CONTEXT.resume();
        }
        return true;
    }


    public void setMediaElement(HTMLMediaElement mediaElement) { // GwtMediaViewPeer also calls this method to pass the video element
        this.mediaElement = mediaElement;
        if (!audioClip) {
            mediaElement.onloadedmetadata = p0 -> {
                setMediaDuration(mediaElement.duration);
                mediaPlayer.setStatus(MediaPlayer.Status.READY);
                return null;
            };
        }
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

    private void setMediaDuration(double seconds) {
        setMediaDuration(Duration.seconds(seconds));
    }

    private void setMediaDuration(Duration duration) {
        mediaPlayer.getMedia().setDuration(duration);
    }

    private void setMediaPlayerCurrentTime(double seconds) {
        setMediaPlayerCurrentTime(Duration.seconds(seconds));
    }

    private void setMediaPlayerCurrentTime(Duration duration) {
        if (mediaPlayerCurrentTimeProperty != null)
            mediaPlayerCurrentTimeProperty.set(duration);
    }

    private void syncMediaPlayerCurrentTime() {
        setMediaPlayerCurrentTime(getCurrentTimeDuration());
    }

    @Override
    public Duration getCurrentTime() { // Note: this method is never called for AudioClip
        if (mediaPlayerCurrentTimeProperty != null)
            return mediaPlayerCurrentTimeProperty.get();
        Duration currentTimeDuration;
        if (SYNC_START_TIME_WITH_AUDIO_BUFFER_FIRST_SOUND_DETECTION && !audioBufferFirstSoundDetected && !hasMediaElement()) {
            getOrCreateAnalyzer().getByteTimeDomainData(byteTimeArray);
            if (byteTimeArray.getAt(0) != 128) {
                audioBufferFirstSoundDetected = true;
                bufferSourceStopWatchMillis.startAt(secondsDoubleToMillisLong(bufferSourceStartOffset));
            }
            currentTimeDuration = Duration.ZERO;
        } else
            currentTimeDuration = getCurrentTimeDuration();
        //Console.log("getCurrentTime() = " + currentTimeDuration);
        return currentTimeDuration;
    }

    private Duration getCurrentTimeDuration() { // Note: this method is never called for AudioClip
        return Duration.millis(getCurrentTimeMillis());
    }

    private double getCurrentTimeMillis() { // Note: this method is never called for AudioClip
        if (hasMediaElement())
            return mediaElement.currentTime * 1000;
        return bufferSourceStopWatchMillis.getStopWatchElapsedTime();
    }

    private void fetchAudioBuffer(boolean resumeIfSuspended) {
        if (isAudioContextReady(resumeIfSuspended)) {
            RequestInit init = RequestInit.create();
            init.setMode("no-cors");
            Request request = new Request(mediaUrl, init);
            DomGlobal.window.fetch(request)
                    .then(response -> {
                        if (!response.ok)
                            Console.log("HTTP error when fetching '" + mediaUrl + "', status = " + response.status);
                        return response.arrayBuffer();
                    })
                    .then(AUDIO_CONTEXT::decodeAudioData)
                    .then(buffer -> {
                        audioBuffer = buffer;
                        if (!audioClip) {
                            mediaPlayer.setStatus(MediaPlayer.Status.READY);
                            setMediaDuration(audioBuffer.duration);
                        }
                        onAudioBufferReady();
                        return null;
                    })
                    .catch_((Promise.CatchOnRejectedCallbackFn<?>) error -> {
                        Console.log("Error while fetching '" + mediaUrl + "'");
                        Console.logNative(error);
                        return null;
                    });
            fetched = true;
        } else if (GwtMediaModuleBooter.mediaRequiresUserInteractionFirst())
            GwtMediaModuleBooter.runOnFirstUserInteraction(() -> fetchAudioBuffer(true));
    }

    private void onAudioBufferReady() {
        if (bufferSource != null && !bufferSourcePlayed) {
            bufferSource.buffer = audioBuffer;
            bufferSource.loop = loopWhenReady;
            if (playWhenReady)
                startBufferSource();
        }
    }

    private static long secondsDoubleToMillisLong(double secondsDouble) {
        return (long) (secondsDouble * 1000);
    }

    private void startBufferSource() {
        if (isBufferSourcePaused())
            resumeBufferSource();
        else {
            bufferSource.start(0, bufferSourceStartOffset);
            bufferSource.onended = p0 -> doOnEnded();
            bufferSourcePlayed = true; // a buffer source can be played only once, so this flag indicates a new bufferSource is needed to play this sound again
            if (!audioClip) {
                bufferSourceStopWatchMillis.startAt(secondsDoubleToMillisLong(bufferSourceStartOffset));
                startMediaPlayerCurrentTimePeriodicSyncer();
                mediaPlayer.setStatus(MediaPlayer.Status.PLAYING);
            }
        }
    }

    private boolean hasMediaElement() {
        return mediaElement != null;
    }

    @Override
    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
        boolean loop = loopWhenReady = cycleCount == MediaPlayer.INDEFINITE;
        if (hasMediaElement())
            mediaElement.loop = loop;
        else if (bufferSource != null)
            bufferSource.loop = loop;
    }

    @Override
    public void play() {
        playedCycleCount = 0;
        playOnceCycle();
    }

    private void playOnceCycle() {
        if (hasMediaElement()) {
            setVolume(volume);
            mediaElement.muted = mute;
            // mediaElementSource is required for possible spectrum analysis
            if (isAudioContextReady(true) && mediaElementSource == null) {
                mediaElementSource = AUDIO_CONTEXT.createMediaElementSource(mediaElement);
                mediaElementSource.connect(AUDIO_CONTEXT.destination);
            }
            // Here is the remaining piece of code to play the media element
            Runnable playMediaElement = () -> {
                mediaElement.play(); // raises an exception if the user hasn't interacted before
                if (!audioClip)
                    startMediaPlayerCurrentTimePeriodicSyncer();
            };
            // If the user hasn't yet interacted, we postpone the play on the first user interaction
            if (GwtMediaModuleBooter.mediaRequiresUserInteractionFirst()) {
                GwtMediaModuleBooter.runOnFirstUserInteraction(playMediaElement);
            } else { // otherwise, we play it now
                playMediaElement.run();
            }
        } else {
            // If the buffer source is already ready to play, we start it now
            if (bufferSource != null && (isBufferSourcePaused() || !bufferSourcePlayed /* can't be played twice */))
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
                listener.spectrumDataUpdate(getCurrentTimeMillis(), audioSpectrumInterval, magnitudes, phases);
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
        if (hasMediaElement()) {
            mediaElement.pause();
            if (!audioClip)
                mediaPlayer.setStatus(MediaPlayer.Status.PAUSED);
        } else if (bufferSource != null)
            pauseBufferSource();
    }

    private void pauseBufferSource() {
        bufferSource.playbackRate.value = 0; // Using this trick as there is no pause() method in AudioBufferSourceNode
        if (!audioClip) {
            bufferSourceStopWatchMillis.pause();
            stopMediaPlayerCurrentTimePropertyPeriodicSyncer();
            mediaPlayer.setStatus(MediaPlayer.Status.PAUSED);
        }
    }

    private boolean isBufferSourcePaused() {
        return bufferSource.playbackRate.value == 0;
    }

    private void resumeBufferSource() {
        bufferSource.playbackRate.value = 1; // We reestablished the normal speed to resume
        if (!audioClip) {
            bufferSourceStopWatchMillis.resume();
            startMediaPlayerCurrentTimePeriodicSyncer();
            mediaPlayer.setStatus(MediaPlayer.Status.PLAYING);
        }
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
            audioBufferFirstSoundDetected = false;
        }
        unscheduleListener();
    }

    @Override
    public void dispose() {
        stop();
        if (hasMediaElement()) {
            // It's necessary to reset the media element source to dispose it
            mediaElement.src = ""; // Note: setting src to null causes issues (Chrome tries to download "null")
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
    public void setMute(boolean mute) {
        this.mute = mute;
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

    @Override
    public ObjectProperty<Duration> mediaPlayerCurrentTimeProperty() {
        if (mediaPlayerCurrentTimeProperty == null) {
            mediaPlayerCurrentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);
            if (bufferSource != null && bufferSourcePlayed && !isBufferSourcePaused())
                startMediaPlayerCurrentTimePeriodicSyncer();
        }
        return mediaPlayerCurrentTimeProperty;
    }

    private void startMediaPlayerCurrentTimePeriodicSyncer() {
        if (hasMediaElement()) {
            mediaElement.ontimeupdate = p0 -> { // observed time update rate = 250ms
                syncMediaPlayerCurrentTime();
                return null;
            };
        } else if (mediaPlayerCurrentTimePeriodicSyncer == null && mediaPlayerCurrentTimeProperty != null)
            mediaPlayerCurrentTimePeriodicSyncer = Scheduler.schedulePeriodic(MEDIA_PLAYER_CURRENT_TIME_SYNC_RATE_MILLIS, this::syncMediaPlayerCurrentTime);
    }

    private void stopMediaPlayerCurrentTimePropertyPeriodicSyncer() {
        if (hasMediaElement()) {
            mediaElement.ontimeupdate = null;
        } else if (mediaPlayerCurrentTimePeriodicSyncer != null) {
            mediaPlayerCurrentTimePeriodicSyncer.cancel();
            mediaPlayerCurrentTimePeriodicSyncer = null;
        }
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
        if (!audioClip)
            mediaPlayer.setStatus(MediaPlayer.Status.PLAYING);
        if (onPlaying != null)
            onPlaying.run();
    }

    private void doOnEnded() {
        if (seekingBufferSource) {
            seekingBufferSource = false;
            if (bufferSourceWasPlayingOnSeeking)
                play();
            return;
        }
        playedCycleCount++;
        if (playedCycleCount < cycleCount)
            playOnceCycle();
        else {
            if (!audioClip) {
                stopMediaPlayerCurrentTimePropertyPeriodicSyncer();
                mediaPlayer.setStatus(MediaPlayer.Status.STOPPED);
            }
            bufferSourceStartOffset = 0;
            if (onEndOfMedia != null)
                onEndOfMedia.run();
        }
    }

    @Override
    public void seek(Duration duration) { // This method is never called for AudioClip
        double jsDuration = Math.max(0, duration.toSeconds()); // Can't be negative
        jsDuration = Math.min(jsDuration, mediaPlayer.getMedia().getDuration().toSeconds());
        setMediaPlayerCurrentTime(jsDuration);
        if (hasMediaElement())
            mediaElement.currentTime = jsDuration;
        else {
            bufferSourceStopWatchMillis.startAt(secondsDoubleToMillisLong(jsDuration));
            bufferSourceStopWatchMillis.pause();
            bufferSourceStartOffset = jsDuration;
            if (bufferSource != null && bufferSourcePlayed) {
                seekingBufferSource = true;
                bufferSourceWasPlayingOnSeeking = !isBufferSourcePaused();
                stop(); // play() will be called again after the bufferSource has stopped in doOnEnded()
            }
        }
    }
}
