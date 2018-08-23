package emul.com.sun.javafx.tk;

/**
 * TKStageListener - Listener for the Stage Peer TKStage to pass updates and events back to the stage
 *
 */
public interface TKStageListener {

    /**
     * The stages peer's location have changed so we need to update the scene
     *
     * @param x the new X
     * @param y The new Y
     */
    void changedLocation(float x, float y);

    /**
     * The stages peer's size have changed so we need to update the scene
     *
     * @param width The new Width
     * @param height The new Height
     */
    void changedSize(float width, float height);

    /**
     * The stages peer focused state has changed.
     *
     * @param focused True if the stage's peer now contains the focus
     * @param cause The cause of (de)activation
     */
    void changedFocused(boolean focused, FocusCause cause);

    /**
     * The stages peer has become iconified or uniconified
     *
     * @param iconified True if the stage's peer is now iconified
     */
    void changedIconified(boolean iconified);

    /**
     * The stages peer has become maximized or unmaximized
     *
     * @param maximized True if the stage's peer is now maximized
     */
    void changedMaximized(boolean maximized);

    /**
     * The stages peer has changed it's "always on top" flag.
     * @param alwaysOnTop
     */
    void changedAlwaysOnTop(boolean alwaysOnTop);

    /**
     * The stages peer has become resizable or nonresizable
     *
     * @param resizable True if the stage's peer is now resizable
     */
    void changedResizable(boolean resizable);

    /**
     * The stages peer has changed its full screen status
     *
     * @param fs True if the stage's peer is now full screen, false otherwise
     */
    void changedFullscreen(boolean fs);

    /**
     * The stage's peer has moved to another screen.
     *
     * @param from An object that identifies the old screen (may be null)
     * @param to An object that identifies the new screen
     */
    void changedScreen(Object from, Object to);

    /**
     * Called if the window is closing do to something that has happened on the peer. For
     * example the user clicking the close button or choosing quit from the application menu
     * on a mac or right click close on the task bar on windows.
     */
    void closing();

    /**
     * Called if the stages peer has closed. For example the platform closes the
     * window after user has clicked the close button on its parent window.
     */
    void closed();

    /**
     * Focus grab has been reset for the stage peer.
     *
     * Called after a previous call to {@link TKStage#grabFocus} when the grab
     * is reset either by user action (e.g. clicking the titlebar of the
     * stage), or via a call to {@link TKStage#ungrabFocus}.
     */
    void focusUngrab();
}
