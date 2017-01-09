package naga.fx.sun.tk;

/**
 * TKScene
 *
 */
public interface TKScene {

    /**
     * This method is called from Scene, when it is being destroyed.
     */
/*
    void dispose();
*/

/*
    void waitForRenderingToComplete();
*/

    /**
     * Waits until the render thread is available for synchronization
     * from the scene graph. Once this method returns, the caller has
     * the lock, and will continue to hold the lock until releaseSynchronization
     * is called.
     */
/*
    void waitForSynchronization();
*/

    /**
     * Releases the synchronization lock previously held. If the updateState
     * flag is set then the glass scene state is updated prior to releasing
     * the lock.
     */
/*
    void releaseSynchronization(boolean updateState);
*/

    void setTKSceneListener(TKSceneListener listener);
/*
    void setTKScenePaintListener(final TKScenePaintListener listener);
*/

/*
    void markDirty();
*/

/*
    void setRoot(NGNode root);
*/

/*
    void setCamera(NGCamera camera);
*/

/*
    NGLightBase[] getLights();
    void setLights(NGLightBase[] lights);
*/

    /**
     * Set the background fill for the scene
     *
     * @param fillPaint This must be a paint class as returned from Toolkit.createPaint(...)
     */
/*
    void setFillPaint(Object fillPaint);
*/

/*
    void setCursor(Object cursor);
*/

/*
    void enableInputMethodEvents(boolean enable);
*/

/*
    void finishInputMethodComposition();
*/

/*
    void entireSceneNeedsRepaint();
*/

/*
    TKClipboard createDragboard(boolean isDragSource);
*/

/*
    AccessControlContext getAccessControlContext();
*/
}
