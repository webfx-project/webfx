package dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.storage.client.Storage;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
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

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
final class GwtMediaPlayerPeer implements MediaPlayerPeer {

    private static final boolean PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP = !"false".equals(Storage.getLocalStorageIfSupported().getItem("PREFER_MEDIA_ELEMENT_TO_AUDIO_BUFFER_FOR_NON_AUDIO_CLIP"));
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
            AUDIO_CONTEXT.onstatechange = e -> {
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
            mediaElement.onloadedmetadata = e -> {
                setMediaDuration(mediaElement.duration);
                mediaPlayer.setStatus(MediaPlayer.Status.READY);
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
            bufferSource.onended = e -> doOnEnded();
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
            // If the user hasn't yet interacted, we postpone the play on the first user interaction (otherwise
            // trying to play mediaElement will raise an exception)
            if (!mute && GwtMediaModuleBooter.mediaRequiresUserInteractionFirst()) {
                GwtMediaModuleBooter.runOnFirstUserInteraction(this::callMediaElementPlay);
            } else { // otherwise, we play it now
                callMediaElementPlay();
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

    private void callMediaElementPlay() {
        // Before playing, we need to set the CORS strategy (if not already set)
        setUpCors();
        // Now we try to play, and call onMediaElementPlaySuccess() on success (implying we were not blocked by CORS)
        mediaElement.play()
                .then(e -> { onMediaElementPlaySuccess(mediaElement); return null; });
        // If the CORS strategy was unknown, the previous play was in cors mode, and we try a second play in no-cors mode
        // and if it succeeds, we call onMediaElementPlaySuccess() which will understand that the working strategy is no-cors
        if (noCorsMediaElement != null)
            noCorsMediaElement.play()
                    .then(e -> { onMediaElementPlaySuccess(noCorsMediaElement); return null; });
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
            } else {
                // mediaElementSource is required for possible spectrum analysis
                if (isAudioContextReady(true) && mediaElementSource == null) {
                    mediaElementSource = AUDIO_CONTEXT.createMediaElementSource(mediaElement);
                    mediaElementSource.connect(AUDIO_CONTEXT.destination);
                }
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

    // CORS management

    // JS object memorizing the working crossOrigin for each remote origin (remoteOrigin => workingCrossOrigin)
    private static JavaScriptObject WORKING_CROSS_ORIGINS;
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
            Storage localStorage = Storage.getLocalStorageIfSupported();
            if (localStorage != null) {
                String item = localStorage.getItem(LOCAL_STORAGE_WORKING_CROSS_ORIGINS_KEY);
                WORKING_CROSS_ORIGINS = JsonUtils.safeEval(item);
            }
            if (WORKING_CROSS_ORIGINS == null)
                WORKING_CROSS_ORIGINS = JavaScriptObject.createObject();
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
                Storage localStorage = Storage.getLocalStorageIfSupported();
                if (localStorage != null) {
                    localStorage.setItem(LOCAL_STORAGE_WORKING_CROSS_ORIGINS_KEY, JsonUtils.stringify(WORKING_CROSS_ORIGINS));
                }
            }
        }
    }

}
