package javafx.scene.media;

import com.gluonhq.attach.storage.StorageService;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.audio.Audio;
import dev.webfx.platform.audio.AudioService;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.scheduler.Scheduler;
import dev.webfx.platform.uischeduler.UiScheduler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author Bruno Salmon
 */
public class MediaPlayer {

    /**
     * Enumeration describing the different status values of a {@link MediaPlayer}.
     *
     * <p>
     * The principal <code>MediaPlayer</code> status transitions are given in the
     * following table:
     * </p>
     * <table border="1">
     * <caption>MediaPlayer Status Transition Table</caption>
     * <tr>
     * <th scope="col">Current \ Next</th><th scope="col">READY</th><th scope="col">PAUSED</th>
     * <th scope="col">PLAYING</th><th scope="col">STALLED</th><th scope="col">STOPPED</th>
     * <th scope="col">DISPOSED</th>
     * </tr>
     * <tr>
     * <th scope="row"><b>UNKNOWN</b></th><td>pre-roll</td><td></td><td></td><td></td><td></td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>READY</b></th><td></td><td></td><td>autoplay; play()</td><td></td><td></td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>PAUSED</b></th><td></td><td></td><td>play()</td><td></td><td>stop()</td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>PLAYING</b></th><td></td><td>pause()</td><td></td><td>buffering data</td><td>stop()</td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>STALLED</b></th><td></td><td>pause()</td><td>data buffered</td><td></td><td>stop()</td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>STOPPED</b></th><td></td><td>pause()</td><td>play()</td><td></td><td></td><td>dispose()</td>
     * </tr>
     * <tr>
     * <th scope="row"><b>HALTED</b></th><td></td><td></td><td></td><td></td><td></td><td>dispose()</td>
     * </tr>
     * </table>
     * <p>The table rows represent the current state of the player and the columns
     * the next state of the player. The cell at the intersection of a given row
     * and column lists the events which can cause a transition from the row
     * state to the column state. An empty cell represents an impossible transition.
     * The transitions to <code>UNKNOWN</code> and <code>HALTED</code> and from
     * <code>DISPOSED</code> status are intentionally not tabulated. <code>UNKNOWN</code>
     * is the initial status of the player before the media source is pre-rolled
     * and cannot be entered once exited. <code>DISPOSED</code> is a terminal status
     * entered after dispose() method is invoked and cannot be exited. <code>HALTED</code>
     * status entered when a critical error occurs and may be transitioned into
     * from any other status except <code>DISPOSED</code>.
     * </p>
     * <p>
     * The principal <code>MediaPlayer</code> status values and transitions are
     * depicted in the following diagram:
     * <br><br>
     * <img src="doc-files/mediaplayerstatus.png" alt="MediaPlayer status diagram">
     * </p>
     * <p>
     * Reaching the end of the media (or the
     * {@link #stopTimeProperty stopTime} if this is defined) while playing does not cause the
     * status to change from <code>PLAYING</code>. Therefore, for example, if
     * the media is played to its end and then a manual seek to an earlier
     * time within the media is performed, playing will continue from the
     * new media time.
     * </p>
     *
     * @since JavaFX 2.0
     */
    public enum Status {

        /**
         * State of the player immediately after creation. While in this state,
         * property values are not reliable and should not be considered.
         * Additionally, commands sent to the player while in this state will be
         * buffered until the media is fully loaded and ready to play.
         */
        UNKNOWN,
        /**
         * State of the player once it is prepared to play.
         * This state is entered only once when the movie is loaded and pre-rolled.
         */
        READY,
        /**
         * State of the player when playback is paused. Requesting the player
         * to play again will cause it to continue where it left off.
         */
        PAUSED,
        /**
         * State of the player when it is currently playing.
         */
        PLAYING,
        /**
         * State of the player when playback has stopped.  Requesting the player
         * to play again will cause it to start playback from the beginning.
         */
        STOPPED,
        /**
         * State of the player when data coming into the buffer has slowed or
         * stopped and the playback buffer does not have enough data to continue
         * playing. Playback will continue automatically when enough data are
         * buffered to resume playback. If paused or stopped in this state, then
         * buffering will continue but playback will not resume automatically
         * when sufficient data are buffered.
         */
        STALLED,
        /**
         * State of the player when a critical error has occurred.  This state
         * indicates playback can never continue again with this player.  The
         * player is no longer functional and a new player should be created.
         */
        HALTED,
        /**
         * State of the player after dispose() method is invoked. This state indicates
         * player is disposed, all resources are free and player SHOULD NOT be used again.
         * <code>Media</code> and <code>MediaView</code> objects associated with disposed player can be reused.
         *
         * @since JavaFX 8.0
         */
        DISPOSED
    }

    public static final int INDEFINITE = -1; // Note: this is a count, not a Duration.
    private static File privateStorage;

    private final Media media;
    private Audio audio;
    private boolean playWhenReady;
    private final StopWatch stopWatch = StopWatch.createSystemMillisStopWatch();
    private Scheduled mediaPlayerCurrentTimePeriodicSyncer;

    private final ObjectProperty<Duration> currentTimeProperty = new SimpleObjectProperty<>(Duration.ZERO);
    private final ObjectProperty<Status> statusProperty = FXProperties.newObjectProperty(Status.UNKNOWN, status -> {
        if (playWhenReady && status == Status.READY) {
            playWhenReady = false;
            play();
        }
    });

    public MediaPlayer(Media media) {
        this.media = media;
        try {
            URL url = new URL(media.getSource());
            String extForm = url.toExternalForm();
            boolean audioFileReady = true;
            if (extForm.startsWith("http")) {
                if (privateStorage == null) {
                    privateStorage = StorageService.create()
                        .flatMap(StorageService::getPrivateStorage)
                        .orElseThrow(() -> new RuntimeException("Error accessing Private Storage folder"));
                }
                String fileName = extForm.substring(extForm.lastIndexOf("/") + 1);
                Path file = privateStorage.toPath()
                    .resolve("assets")
                    .resolve("audio")
                    .resolve(fileName);
                audioFileReady = Files.exists(file);
                if (!audioFileReady) {
                    if (!Files.exists(file.getParent()))
                        Files.createDirectories(file.getParent());
                    Scheduler.runInBackground(() -> {
                        try {
                            Console.log("[WebFX Platform]: Downloading " + extForm);
                            InputStream in = url.openStream();
                            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
                            Console.log("[WebFX Platform]: File size: " + Files.size(file));
                            UiScheduler.runInUiThread(() -> loadGluonMusicNow(extForm));
                        } catch (IOException e) {
                            Console.log("[WebFX Platform]: Error while downloading " + extForm, e);
                            statusProperty.set(Status.HALTED);
                        }
                    });
                }
            }
            if (audioFileReady)
                loadGluonMusicNow(extForm);
        } catch (IOException e) {
            statusProperty.set(Status.HALTED);
        }
    }

    private void loadGluonMusicNow(String url) {
        audio = AudioService.loadMusic(url);
        if (audio != null)
            statusProperty.set(Status.READY);
    }

    public Media getMedia() {
        return media;
    }

    public void setCycleCount(int cycleCount) {
        if (audio != null)
            audio.setCycleCount(cycleCount);
    }

    public void play() {
        if (audio != null) {
            audio.play();
            if (Status.PAUSED.equals(getStatus()))
                stopWatch.resume();
            else
                stopWatch.start();
            startMediaPlayerCurrentTimePeriodicSyncer();
            statusProperty.set(Status.PLAYING);
        } else
            playWhenReady = true;
    }

    private void startMediaPlayerCurrentTimePeriodicSyncer() {
        if (mediaPlayerCurrentTimePeriodicSyncer == null) {
            syncMediaPlayerCurrentTime();
            mediaPlayerCurrentTimePeriodicSyncer = Scheduler.schedulePeriodic(250, this::syncMediaPlayerCurrentTime);
        }
    }

    private void syncMediaPlayerCurrentTime() {
        currentTimeProperty.set(Duration.millis(stopWatch.getStopWatchElapsedTime()));
    }

    private void stopMediaPlayerCurrentTimePeriodicSyncer() {
        if (mediaPlayerCurrentTimePeriodicSyncer != null) {
            mediaPlayerCurrentTimePeriodicSyncer.cancel();
            mediaPlayerCurrentTimePeriodicSyncer = null;
        }
    }

    public void pause() {
        if (audio != null) {
            audio.pause();
            stopWatch.pause();
            stopMediaPlayerCurrentTimePeriodicSyncer();
            statusProperty.set(Status.PAUSED);
        }
        playWhenReady = false;
    }

    public void stop() {
        if (audio != null) {
            audio.stop();
            stopWatch.pause();
            stopMediaPlayerCurrentTimePeriodicSyncer();
            statusProperty.set(Status.STOPPED);
        }
        playWhenReady = false;
    }

    public void dispose() {
        if (audio != null)
            audio.dispose();
        stopMediaPlayerCurrentTimePeriodicSyncer();
        statusProperty.set(Status.DISPOSED);
        playWhenReady = false;
    }

    public void setVolume(double volume) {
        if (audio != null)
            audio.setVolume(volume);
    }

    public void setOnPlaying(Runnable onPlaying) {
    }

    public void setOnEndOfMedia(Runnable onEndOfMedia) {
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return currentTimeProperty;
    }

    public Duration getCurrentTime() {
        return currentTimeProperty.get();
    }

    public void seek(Duration duration) {
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return statusProperty;
    }

    public Status getStatus() {
        return statusProperty.get();
    }
}
