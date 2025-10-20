package dev.webfx.kit.mapper.peers.javafxmedia.spi.elemental2;

import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.UserInteraction;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.scheduler.Scheduler;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.stopwatch.StopWatch;
import elemental2.core.JsObject;
import elemental2.core.Uint8Array;
import elemental2.dom.*;
import elemental2.media.*;
import elemental2.promise.Promise;
import elemental2.webstorage.Storage;
import elemental2.webstorage.WebStorageWindow;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import jsinterop.base.Js;

import java.util.Objects;

import static elemental2.core.Global.JSON;

/**
 * @author Bruno Salmon
 */
public final class Elemental2MediaPlayerPeer implements MediaPlayerPeer {

    private static final boolean PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP = !"false".equals(WebStorageWindow.of(DomGlobal.window).localStorage.getItem("PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP"));
    private static final long MEDIA_PLAYER_CURRENT_TIME_SYNC_RATE_MILLIS = 250; // Same rate as mediaElement.ontimeupdate
    private static final boolean SYNC_START_TIME_WITH_AUDIO_BUFFER_FIRST_SOUND_DETECTION = true;
    private static AudioContext AUDIO_CONTEXT; // One single audio context for the whole application
    private final MediaPlayer mediaPlayer;
    private ObjectProperty<Duration> mediaPlayerCurrentTimeProperty;
    private BooleanProperty mediaPlayerMuteProperty;
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
    private HTMLMediaElement mediaElement;
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

    public Elemental2MediaPlayerPeer(MediaPlayer mediaPlayer, boolean audioClip) {
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

    static AudioContext getAudioContext() {
        if (AUDIO_CONTEXT == null)
            AUDIO_CONTEXT = new AudioContext();
        return AUDIO_CONTEXT;
    }

    /*static {
        if (!isAudioContextReady(false)) {
            AUDIO_CONTEXT.onstatechange = e -> {
                Console.log("AudioContext state changed to " + AUDIO_CONTEXT.state);
                return null;
            };
        }
    }*/

    private static boolean isAudioContextReady(boolean resumeIfSuspended) {
        if (AUDIO_CONTEXT == null && UserInteraction.hasUserNotInteractedYet())
            return false;
        if (getAudioContext().state.equalsIgnoreCase("suspended")) {
            if (!resumeIfSuspended)
                return false;
            getAudioContext().resume();
        }
        return true;
    }


    public void setMediaElement(HTMLMediaElement mediaElement) { // GwtMediaViewPeer also calls this method to pass the video element
        this.mediaElement = mediaElement;
        if (!audioClip) {
            mediaElement.onloadedmetadata = e -> {
                readAndSetMediaDuration(true);
                return null;
            };
        }
        mediaElement.onplaying = e -> {
            doOnPlaying();
            return null;
        };
        mediaElement.onended = e -> {
            doOnEnded();
            return null;
        };
        if (loopWhenReady)
            mediaElement.loop = true;
        if (mute)
            mediaElement.muted = true;
        mediaElement.src = mediaUrl;
    }

    public HTMLMediaElement getMediaElement() {
        return mediaElement;
    }

    private double readAndSetMediaDuration(boolean setMediaPlayerStatusToReady) {
        double durationSeconds = hasMediaElement() ? mediaElement.duration : audioBuffer.duration;
        if (Double.isFinite(durationSeconds)) {
            Duration duration = Duration.seconds(durationSeconds);
            Media media = mediaPlayer.getMedia();
            if (!Objects.equals(media.getDuration(), duration))
                media.setDuration(duration);
        }
        // if requested, we set the player status to READY, checking however its status was UNKNOWN (initial state)
        if (setMediaPlayerStatusToReady && mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) {
            mediaPlayer.setStatus(MediaPlayer.Status.READY);
        }
        return durationSeconds;
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
                bufferSourceStopWatchMillis.reset();
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
                .then(getAudioContext()::decodeAudioData)
                .then(buffer -> {
                    audioBuffer = buffer;
                    if (!audioClip)
                        readAndSetMediaDuration(true);
                    onAudioBufferReady();
                    return null;
                })
                .catch_((Promise.CatchOnRejectedCallbackFn<?>) error -> {
                    Console.log("Error while fetching '" + mediaUrl + "'");
                    Console.logNative(error);
                    return null;
                });
            fetched = true;
        } else if (UserInteraction.hasUserNotInteractedYet())
            UserInteraction.runOnNextUserInteraction(() -> fetchAudioBuffer(true));
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
            bufferSource.onended = e -> doOnEnded();
            bufferSourcePlayed = true; // a buffer source can be played only once, so this flag indicates a new bufferSource is needed to play this sound again
            if (!audioClip) {
                bufferSourceStopWatchMillis.reset();
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
        // If the media is not muted and the user hasn't yet interacted, trying to play it will raise an exception. So
        // it's better not to try (otherwise a warning will be logged in the console). If the media is an AudioClip
        // (ex: short game sound) we will just ignore it, but if it's a music (ex: background music), we postpone the
        // play to the first user interaction.
        if (!mute && UserInteraction.hasUserNotInteractedYet()) {
            if (!audioClip && !playWhenReady) {
                playWhenReady = true;
                // checking that the music hasn't been stopped or paused in the meantime
                // Ok, we can now start playing the music
                UserInteraction.runOnNextUserInteraction(() -> {
                    if (playWhenReady) // checking that the music hasn't been stopped or paused in the meantime
                        playOnceCycle(); // Ok, we can now start playing the music
                });
            }
            return;
        }
        playWhenReady = false;
        if (hasMediaElement()) {
            setVolume(volume);
            mediaElement.muted = mute;
            callMediaElementPlay();
        } else {
            // If the buffer source is already ready to play, we start it now
            if (bufferSource != null && (isBufferSourcePaused() || !bufferSourcePlayed /* can't be played twice */))
                startBufferSource();
            else { // the buffer is not yet ready, or has already been played
                if (!fetched)
                    fetchAudioBuffer(true);
                bufferSource = getAudioContext().createBufferSource();
                bufferSourcePlayed = false;
                gainNode = getAudioContext().createGain();
                setVolume(volume);
                bufferSource.connect(gainNode);
                gainNode.connect(getAudioContext().destination);
                playWhenReady = true;
                if (audioBuffer != null)
                    onAudioBufferReady();
            }
        }
        scheduleListener();
    }

    private void callMediaElementPlay() {
        // Before playing, we need to set the CORS strategy (if not already set)
        setUpCors();
        // Now we try to play, and call onMediaElementPlaySuccess() on success (implying we were not blocked by CORS)
        mediaElement.play()
            .then(e -> {
                onMediaElementPlaySuccess(mediaElement);
                return null;
            });
        // If the CORS strategy was unknown, the previous play was in cors mode, and we try a second play in no-cors mode
        // and if it succeeds, we call onMediaElementPlaySuccess() which will understand that the working strategy is no-cors
        if (noCorsMediaElement != null)
            noCorsMediaElement.play()
                .then(e -> {
                    onMediaElementPlaySuccess(noCorsMediaElement);
                    return null;
                });
    }

    private void onMediaElementPlaySuccess(HTMLMediaElement mediaElement) {
        // We memorise the working CORS strategy
        memoriseWorkingCrossOrigin(mediaElement);
        // We also start a periodic timer to update the mediaPlayer currentTime
        if (!audioClip) // Not necessary for audio clips (short sounds)
            startMediaPlayerCurrentTimePeriodicSyncer();
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
        playWhenReady = false;
    }

    private void pauseBufferSource() {
        bufferSource.playbackRate.value = 0; // Using this trick as there is no pause() method in AudioBufferSourceNode
        if (!audioClip) {
            bufferSourceStopWatchMillis.off();
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
            bufferSourceStopWatchMillis.on();
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
            audioBufferFirstSoundDetected = false;
        }
        playWhenReady = false;
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
        if (mediaPlayerMuteProperty != null)
            mediaPlayerMuteProperty.set(mute);
    }

    @Override
    public BooleanProperty muteProperty() {
        if (mediaPlayerMuteProperty == null)
            mediaPlayerMuteProperty = new SimpleBooleanProperty(mute);
        return mediaPlayerMuteProperty;
    }

    private AnalyserNode getOrCreateAnalyzer() {
        if (analyser == null) {
            analyser = getAudioContext().createAnalyser();
            //analyser.frequencyBinCount = arraySize; // Readonly field
            analyser.fftSize = 2 * arraySize;
            analyser.minDecibels = -60;
            analyser.maxDecibels = 0;
            byteTimeArray = new Uint8Array(arraySize);
            byteFrequencyArray = new Uint8Array(arraySize);
            //floatTimeArray = new Float32Array(arraySize);
            //floatFrequencyArray = new Float32Array(arraySize);
            if (bufferSource != null) {
                bufferSource.connect(analyser);
                analyser.connect(gainNode);
            } else {
                // mediaElementSource is required for possible spectrum analysis
                if (isAudioContextReady(true) && mediaElementSource == null) {
                    mediaElementSource = getAudioContext().createMediaElementSource(mediaElement);
                    mediaElementSource.connect(getAudioContext().destination);
                }
                mediaElementSource.connect(analyser);
                analyser.connect(getAudioContext().destination);
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
            mediaElement.ontimeupdate = e -> { // observed time update rate = 250ms
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

    private void doOnPlaying() {
        if (!audioClip)
            mediaPlayer.setStatus(MediaPlayer.Status.PLAYING);
    }

    private void doOnEnded() {
        if (seekingBufferSource) {
            seekingBufferSource = false;
            if (bufferSourceWasPlayingOnSeeking)
                play();
            return;
        }
        playedCycleCount++;
        if (playedCycleCount < cycleCount) {
            playOnceCycle();
            if (!audioClip) {
                Runnable onRepeat = mediaPlayer.getOnRepeat();
                if (onRepeat != null)
                    onRepeat.run();
            }
        } else {
            if (!audioClip) {
                stopMediaPlayerCurrentTimePropertyPeriodicSyncer();
                mediaPlayer.setStatus(MediaPlayer.Status.STOPPED);
                Runnable onEndOfMedia = mediaPlayer.getOnEndOfMedia();
                if (onEndOfMedia != null)
                    onEndOfMedia.run();
            }
            bufferSourceStartOffset = 0;
        }
    }

    @Override
    public void seek(Duration duration) { // This method is never called for AudioClip
        double durationSeconds = Math.max(0, duration.toSeconds()); // Can't be negative
        double mediaDurationSeconds = readAndSetMediaDuration(false);
        if (Double.isFinite(mediaDurationSeconds)) // Sometimes mediaElement.duration returns infinite for some unknown reason
            durationSeconds = Math.min(durationSeconds, mediaDurationSeconds);
        setMediaPlayerCurrentTime(durationSeconds);
        if (hasMediaElement())
            mediaElement.currentTime = durationSeconds;
        else {
            bufferSourceStopWatchMillis.reset();
            bufferSourceStopWatchMillis.startAt(secondsDoubleToMillisLong(durationSeconds));
            bufferSourceStopWatchMillis.pause();
            bufferSourceStartOffset = durationSeconds;
            if (bufferSource != null && bufferSourcePlayed) {
                seekingBufferSource = true;
                bufferSourceWasPlayingOnSeeking = !isBufferSourcePaused();
                stop(); // play() will be called again after the bufferSource has stopped in doOnEnded()
            }
        }
    }

    // CORS management

    // JS object memorizing the working crossOrigin for each remote origin (remoteOrigin => workingCrossOrigin)
    private static JsObject WORKING_CROSS_ORIGINS;
    // The key used to store WORKING_CROSS_ORIGINS in the local storage
    private static final String LOCAL_STORAGE_WORKING_CROSS_ORIGINS_KEY = "webfx-workingCrossOrigins";
    private String mediaOrigin;
    // This field will contain the working CORS strategy for this particular media
    private String workingCrossOrigin; // 3 possible values:
    // - null -> means that the CORS strategy has not being set yet (which doesn't matter for medias from same origin)
    // - "null" -> means that mediaElement.crossOrigin = null; is the working CORS strategy
    // - "anonymous" -> means that mediaElement.crossOrigin = "anonymous"; is the working CORS strategy
    private HTMLMediaElement noCorsMediaElement;
    private boolean corsSetup = false;

    private void setUpCors() {
        // No need to set up the CORS strategy again if it was already done
        if (corsSetup)
            return;
        corsSetup = true;

        // We don't need to pay attention to CORS when the media is downloaded from the same origin as the application.
        if (isMediaFromSameOrigin())
            return;

        // The only little CORS control browsers give us is the crossOrigin attribute of the mediaElement, there is no
        // control at all when using the Web Audio API (AudioContext) because in that case the browser will always fetch
        // the resource in cors mode (the request headers will contain "Sec-Fetch-Mode: cors").
        if (!hasMediaElement()) { // cors mode enforced by the browser
            // All we can do is to pray 🙏 that the remote server delivering the media has a relaxed CORS policy, i.e.
            // it will serve the media with "Access-Control-Allow-Origin: *" http response header. If not (which
            // includes the case of servers with no CORS policy i.e "Access-Control-Allow-Origin" is absent), the media
            // will be blocked by CORS 🤷
            return;
        }

        // Now we are in the case of a media element, and we have 2 choices for the crossOrigin attribute:

        // 1) crossOrigin = null => the browser will fetch in no-cors mode ("Sec-Fetch-Mode: no-cors") and this will
        // succeed only if the server has NO CORS policy (i.e "Access-Control-Allow-Origin" is absent). Otherwise, (if
        // "Access-Control-Allow-Origin" is present), it will fail. If it fails, the browser will just log this message
        // in the console: "MediaElementAudioSource outputs zeros due to CORS access restrictions", mediaElement.onerror
        // will not be called. If it succeeds, play().then() will be called <- used by callMediaElementPlay()

        // 2) crossOrigin = "anonymous" => the browser will fetch in cors mode ("Sec-Fetch-Mode: no-cors") and this will
        // succeed only if the server has a RELAXED CORS policy (i.e. "Access-Control-Allow-Origin: *" present).
        // Otherwise, (including if "Access-Control-Allow-Origin" is absent), it will fail and the browser will log:
        // ⛔️ Access to audio at <mediaUrl> from origin <appOrigin> has been blocked by CORS policy: No
        // 'Access-Control-Allow-Origin' header is present on the requested resource.
        // ⛔️ GET <mediaUrl> net::ERR_FAILED 206 (Partial Content)
        // ⛔️ Uncaught (in promise) DOMException: Failed to load because no supported source was found.
        // mediaElement.onerror will be called in that case by most browsers, but not on iOS...
        // If it succeeds, play().then() will be called <- used by callMediaElementPlay()

        // As it's not possible to know in advance which CORS policy the remote origin has, we will try both strategies
        // at the same time, and only one should work. Another possibility would be to try 2) first, and then try 1) if
        // 2) failed, but this would imply the use mediaElement.onerror which is unreliable at this time on iOS.

        // Also, we will keep a list of the working CORS strategy for each remote origin, so we can apply the correct
        // strategy straightaway next times for all media coming from this remote origin. This list will be memorised
        // in the local storage.

        // If workingCrossOrigin is not yet set, we try to get it from the memorised working CORS strategies.
        if (workingCrossOrigin == null)
            workingCrossOrigin = getMemorisedWorkingCrossOrigin();
        // If we can get it, then we use it to set mediaElement.crossOrigin, and that's it (this should work).
        if (workingCrossOrigin != null) {
            mediaElement.crossOrigin = "null".equals(workingCrossOrigin) ? null : workingCrossOrigin;
            return;
        }

        // If the CORS policy is unknown, we will try both strategies, and one will fail, which will cause some error
        // messages in the browser console, so we log this message before to reassure people who are watching the
        // console that it's kind of normal. There will be another log later from memoriseWorkingCrossOrigin().
        Console.log("🤷 Unknown CORS policy for remote origin " + mediaOrigin + ", so trying both cors and no-cors modes (one should succeed, one should fail)");
        // We try 2) with this.mediaElement
        mediaElement.crossOrigin = "anonymous"; // cors mode

        // We create another mediaElement with identical settings, except that crossOrigin is not set => no-cors mode
        noCorsMediaElement = (HTMLMediaElement) DomGlobal.document.createElement("audio");
        noCorsMediaElement.src = mediaElement.src;
        noCorsMediaElement.onloadedmetadata = mediaElement.onloadedmetadata;
        noCorsMediaElement.onplaying = mediaElement.onplaying;
        noCorsMediaElement.onended = mediaElement.onended;
        noCorsMediaElement.loop = mediaElement.loop;
        noCorsMediaElement.muted = mediaElement.muted;
        noCorsMediaElement.volume = mediaElement.volume;
        noCorsMediaElement.ontimeupdate = mediaElement.ontimeupdate;

        // The next step will be a call to callMediaElementPlay() which will call play() on both elements, and it will
        // use play().then() to identify which one is working. Then onMediaElementPlaySuccess() will call
        // memoriseWorkingCrossOrigin() with the working element, so we can memorise the working CORS strategy.
    }

    private boolean isMediaFromSameOrigin() {
        String appOrigin = DomGlobal.window.location.origin;
        if (mediaOrigin == null) { // not yet set on first call, so set it now
            try {
                mediaOrigin = new URL(mediaUrl).origin;
            } catch (Exception e) { // happens with incomplete urls (ex: relative paths)
                mediaOrigin = appOrigin; // relative paths refer to the appOrigin
            }
        }
        return appOrigin.equals(mediaOrigin);
    }

    private String getMemorisedWorkingCrossOrigin() {
        if (WORKING_CROSS_ORIGINS == null) {
            Storage localStorage = WebStorageWindow.of(DomGlobal.window).localStorage;
            if (localStorage != null) {
                String item = localStorage.getItem(LOCAL_STORAGE_WORKING_CROSS_ORIGINS_KEY);
                WORKING_CROSS_ORIGINS = Js.cast(JSON.parse(item)); // ok to pass null (will return null)
            }
            if (WORKING_CROSS_ORIGINS == null)
                WORKING_CROSS_ORIGINS = JsObject.create(null);
        }
        return HtmlUtil.getJsJavaObjectAttribute(WORKING_CROSS_ORIGINS, mediaOrigin);
    }

    private void memoriseWorkingCrossOrigin(HTMLMediaElement mediaElement) {
        // We don't need to pay attention to CORS when the media is downloaded from the same origin as the application.
        if (isMediaFromSameOrigin())
            return;
        HTMLMediaElement otherMediaElement = mediaElement == this.mediaElement ? noCorsMediaElement : this.mediaElement;
        if (otherMediaElement != null) {
            otherMediaElement.muted = true;
            otherMediaElement.onended = null;
            otherMediaElement.pause();
        }
        this.mediaElement = mediaElement;
        noCorsMediaElement = null;
        String workingCrossOrigin = mediaElement.crossOrigin;
        if (workingCrossOrigin == null)
            workingCrossOrigin = "null";
        if (!Objects.equals(this.workingCrossOrigin, workingCrossOrigin)) {
            this.workingCrossOrigin = workingCrossOrigin;
            if (mediaOrigin != null) {
                Console.log("✅ This is " + ("null".equals(workingCrossOrigin) ? "no-cors" : "cors") + " mode that is working for remote origin " + mediaOrigin + " (now memorized)");
                HtmlUtil.setJsJavaObjectAttribute(WORKING_CROSS_ORIGINS, mediaOrigin, workingCrossOrigin);
                Storage localStorage = WebStorageWindow.of(DomGlobal.window).localStorage;
                if (localStorage != null) {
                    localStorage.setItem(LOCAL_STORAGE_WORKING_CROSS_ORIGINS_KEY, JSON.stringify(WORKING_CROSS_ORIGINS));
                }
            }
        }
    }

    /**
     *
     * The purpose of this static initialiser is to ensure that the sound will play ok on iOS and iPadOS after the first
     * user interaction.
     * ==========================
     * Description of the problem
     * ==========================
     * Other OS automatically unlock the sound on first user interaction, even if the application code doesn't play any
     * sound at this time, it can play sound any time later after the first user interaction, even not necessarily during
     *  a user interaction. On iOS and iPadOS however, this sound unlocking is not automatic. The unlocking happens only
     * when the application plays a sound DURING the user interaction.
     * Because of this difference, if the JavaFX application code tries to start playing sound using setOnMouseClicked(),
     * this won't work (it will work however with setOnMousePressed() or setOnMouseReleased()). This is due to the way
     * WebFX emulates the JavaFX click event, which is not based on the JavaScript "click" event as opposed to the other
     * events, because JavaFX has its own way to fire it when detecting the mouse released, and WebFX postpones this process
     * (see HtmlScenePeer.java, installMouseListeners() and passHtmlMouseEventOnToFx() methods).
     * ===========================
     * Description of the solution
     * ===========================
     * This static initializer will automatically detect the first (or next) user interaction and play a silent sound
     * for a very short time during that interaction, causing the sound unlocking even on iOS and iPadOS. Then, if the
     * JavaFX application requests playing sound using setOnMouseClicked(), it will work because the sound unlocking has
     * previously been done.
     */

    static {
        UserInteraction.runOnNextUserInteraction(() -> {
            String tinySilentMp3Data = "data:audio/mpeg;base64,SUQzBAAAAAABEVRYWFgAAAAtAAADY29tbWVudABCaWdTb3VuZEJhbmsuY29tIC8gTGFTb25vdGhlcXVlLm9yZwBURU5DAAAAHQAAA1N3aXRjaCBQbHVzIMKpIE5DSCBTb2Z0d2FyZQBUSVQyAAAABgAAAzIyMzUAVFNTRQAAAA8AAANMYXZmNTcuODMuMTAwAAAAAAAAAAAAAAD/80DEAAAAA0gAAAAATEFNRTMuMTAwVVVVVVVVVVVVVUxBTUUzLjEwMFVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQsRbAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVf/zQMSkAAADSAAAAABVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV";
            new Elemental2MediaPlayerPeer(new MediaPlayer(new Media(tinySilentMp3Data)), true).play(); // This will unlock the sound
        });
    }
}
