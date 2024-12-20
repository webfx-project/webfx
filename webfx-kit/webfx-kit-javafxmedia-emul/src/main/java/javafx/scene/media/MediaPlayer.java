package javafx.scene.media;

import dev.webfx.kit.mapper.peers.javafxmedia.MediaPlayerPeer;
import dev.webfx.kit.mapper.peers.javafxmedia.WebFxKitMediaMapper;
import dev.webfx.kit.util.properties.FXProperties;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

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

    private final Media media;
    private final MediaPlayerPeer peer;
    private int cycleCount;

    private ObjectProperty<Status> statusProperty;

    public MediaPlayer(Media media) {
        this(media, false);
    }

    MediaPlayer(Media media, boolean audioClip) {
        this.media = media;
        peer = WebFxKitMediaMapper.createMediaPlayerPeer(this, audioClip);
    }

    public MediaPlayerPeer getPeer() {
        return peer;
    }

    public Media getMedia() {
        return media;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
        peer.setCycleCount(cycleCount);
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void play() {
        peer.play();
    }

    public void pause() {
        peer.pause();
    }

    public void stop() {
        peer.stop();
    }

    public void dispose() {
        peer.dispose();
    }

    public void setVolume(double volume) {
        peer.setVolume(volume);
    }

    public BooleanProperty muteProperty() {
        return peer.muteProperty();
    }

    public final void setMute(boolean mute) {
        peer.setMute(mute);
    }

    public ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        return peer.mediaPlayerCurrentTimeProperty();
    }

    public final Duration getCurrentTime() {
        return peer.getCurrentTime();
    }

    public void seek(Duration duration) {
        peer.seek(duration);
    }

    /**
     * Sets the value of the audio spectrum notification interval in seconds.
     *
     * @param value a positive value specifying the spectral update interval
     */
    public final void setAudioSpectrumInterval(double value) {
        peer.setAudioSpectrumInterval(value);
    }

    /**
     * Sets the number of bands in the audio spectrum.
     *
     * @param value the number of spectral bands; <code>value</code>must be &ge; 2
     */
    public final void setAudioSpectrumNumBands(int value) {
        peer.setAudioSpectrumNumBands(value);
    }


    public final void setAudioSpectrumListener(AudioSpectrumListener listener) {
        peer.setAudioSpectrumListener(listener);
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        if (statusProperty == null)
            statusProperty = FXProperties.newObjectProperty(Status.UNKNOWN, () -> {
                Runnable statusRunnable = getStatusRunnable();
                if (statusRunnable != null)
                    statusRunnable.run();
            });
        return statusProperty;
    }

    private Runnable getStatusRunnable() {
        switch (getStatus()) {
            case READY:
                return getOnReady();
            case PAUSED:
                return getOnPaused();
            case PLAYING:
                return getOnPlaying();
            case STOPPED:
                return getOnStopped();
            case STALLED:
                return getOnStalled();
            case HALTED:
                return getOnHalted();
        }
        // Note: onRepeat & endOfMedia are not called by this class, this responsibility is delegated to the peer
        return null;
    }

    public Status getStatus() {
        return statusProperty == null ? Status.UNKNOWN : statusProperty.get();
    }

    private ObjectProperty<Runnable> onEndOfMedia;

    /**
     * Sets the end of media event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnEndOfMedia(Runnable value) {
        onEndOfMediaProperty().set(value);
    }

    /**
     * Retrieves the end of media event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnEndOfMedia() {
        return onEndOfMedia == null ? null : onEndOfMedia.get();
    }

    public ObjectProperty<Runnable> onEndOfMediaProperty() {
        if (onEndOfMedia == null) {
            onEndOfMedia = new SimpleObjectProperty<Runnable>(this, "onEndOfMedia");
        }
        return onEndOfMedia;
    }

    /**
     * Event handler invoked when the status changes to
     * <code>READY</code>.
     */
    private ObjectProperty<Runnable> onReady; // Player is ready and media has prerolled

    /**
     * Sets the {@link Status#READY} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnReady(Runnable value) {
        onReadyProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#READY} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnReady() {
        return onReady == null ? null : onReady.get();
    }

    public ObjectProperty<Runnable> onReadyProperty() {
        if (onReady == null) {
            onReady = new SimpleObjectProperty<Runnable>(this, "onReady");
        }
        return onReady;
    }

    /**
     * Event handler invoked when the status changes to
     * <code>PLAYING</code>.
     */
    private ObjectProperty<Runnable> onPlaying; // Media has reached its end.

    /**
     * Sets the {@link Status#PLAYING} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnPlaying(Runnable value) {
        onPlayingProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#PLAYING} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnPlaying() {
        return onPlaying == null ? null : onPlaying.get();
    }

    public ObjectProperty<Runnable> onPlayingProperty() {
        if (onPlaying == null) {
            onPlaying = new SimpleObjectProperty<Runnable>(this, "onPlaying");
        }
        return onPlaying;
    }

    /**
     * Event handler invoked when the status changes to <code>PAUSED</code>.
     */
    private ObjectProperty<Runnable> onPaused; // Media has reached its end.

    /**
     * Sets the {@link Status#PAUSED} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnPaused(Runnable value) {
        onPausedProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#PAUSED} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnPaused() {
        return onPaused == null ? null : onPaused.get();
    }

    public ObjectProperty<Runnable> onPausedProperty() {
        if (onPaused == null) {
            onPaused = new SimpleObjectProperty<Runnable>(this, "onPaused");
        }
        return onPaused;
    }

    /**
     * Event handler invoked when the status changes to
     * <code>STOPPED</code>.
     */
    private ObjectProperty<Runnable> onStopped; // Media has reached its end.

    /**
     * Sets the {@link Status#STOPPED} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnStopped(Runnable value) {
        onStoppedProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#STOPPED} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnStopped() {
        return onStopped == null ? null : onStopped.get();
    }

    public ObjectProperty<Runnable> onStoppedProperty() {
        if (onStopped == null) {
            onStopped = new SimpleObjectProperty<Runnable>(this, "onStopped");
        }
        return onStopped;
    }

    /**
     * Event handler invoked when the status changes to <code>HALTED</code>.
     */
    private ObjectProperty<Runnable> onHalted; // Media caught an irrecoverable error.

    /**
     * Sets the {@link Status#HALTED} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnHalted(Runnable value) {
        onHaltedProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#HALTED} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnHalted() {
        return onHalted == null ? null : onHalted.get();
    }

    public ObjectProperty<Runnable> onHaltedProperty() {
        if (onHalted == null) {
            onHalted = new SimpleObjectProperty<Runnable>(this, "onHalted");
        }
        return onHalted;
    }

    /**
     * Event handler invoked when the player <code>currentTime</code> reaches
     * <code>stopTime</code> and <i>will be</i> repeating. This callback is made
     * prior to seeking back to <code>startTime</code>.
     *
     * @see cycleCount
     */
    private ObjectProperty<Runnable> onRepeat;

    /**
     * Sets the repeat event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnRepeat(Runnable value) {
        onRepeatProperty().set(value);
    }

    /**
     * Retrieves the repeat event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnRepeat() {
        return onRepeat == null ? null : onRepeat.get();
    }

    public ObjectProperty<Runnable> onRepeatProperty() {
        if (onRepeat == null) {
            onRepeat = new SimpleObjectProperty<Runnable>(this, "onRepeat");
        }
        return onRepeat;
    }

    /**
     * Event handler invoked when the status changes to
     * <code>STALLED</code>.
     */
    private ObjectProperty<Runnable> onStalled;

    /**
     * Sets the {@link Status#STALLED} event handler.
     *
     * @param value the event handler or <code>null</code>.
     */
    public final void setOnStalled(Runnable value) {
        onStalledProperty().set(value);
    }

    /**
     * Retrieves the {@link Status#STALLED} event handler.
     *
     * @return the event handler or <code>null</code>.
     */
    public final Runnable getOnStalled() {
        return onStalled == null ? null : onStalled.get();
    }

    public ObjectProperty<Runnable> onStalledProperty() {
        if (onStalled == null) {
            onStalled = new SimpleObjectProperty<Runnable>(this, "onStalled");
        }
        return onStalled;
    }

    // For internal WebFX usage only

    public void setStatus(Status status) {
        statusProperty(); // To ensure non-nullity
        statusProperty.set(status);
    }
}
