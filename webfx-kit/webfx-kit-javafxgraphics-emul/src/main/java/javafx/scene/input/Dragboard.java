package javafx.scene.input;

import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.util.Set;

/**
 * A drag and drop specific {@link Clipboard}.
 * @since JavaFX 2.0
 */
public class Dragboard extends Clipboard {

    /**
     * Whether access to the data requires a permission.
     */
    //private boolean dataAccessRestricted = true;
    private final Scene scene; // WebFX addition

    public Dragboard(Scene scene) {
        this.scene = scene;
    }

/*
    Dragboard(TKClipboard peer) {
        super(peer);
    }
*/

/*
    @Override
    Object getContentImpl(DataFormat dataFormat) {
        if (dataAccessRestricted) {
            final SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                final Permission clipboardPerm =
                        PermissionHelper.getAccessClipboardPermission();
                securityManager.checkPermission(clipboardPerm);
            }
        }
        return super.getContentImpl(dataFormat);
    }
*/

    /**
     * Gets set of transport modes supported by source of this drag opeation.
     * @return set of supported transfer modes
     */
    public final Set<TransferMode> getTransferModes() {
        return scene.getOrCreateDndGesture().sourceTransferModes; //peer.getTransferModes();
    }

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
/*
    @Deprecated
    public TKClipboard impl_getPeer() {
        return peer;
    }
*/

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
/*
    @Deprecated
    public static Dragboard impl_createDragboard(TKClipboard peer) {
        return new Dragboard(peer);
    }
*/

    // PENDING_DOC_REVIEW
    /**
     * Sets the visual representation of data being transfered
     * in a drag and drop gesture.
     * Uses the given image for the drag view with the offsetX and offsetY
     * specifying cursor position over the image.
     * This method should be called only when starting drag and drop operation
     * in the DRAG_DETECTED handler, calling it at other times
     * doesn't have any effect.
     * @param image image to use for the drag view
     * @param offsetX x position of the cursor over the image
     * @param offsetY y position of the cursor over the image
     * @since JavaFX 8.0
     */
/*
    public void setDragView(Image image, double offsetX, double offsetY) {
        peer.setDragView(image);
        peer.setDragViewOffsetX(offsetX);
        peer.setDragViewOffsetY(offsetY);
    }
*/

    /**
     * Sets the visual representation of data being transfered
     * in a drag and drop gesture.
     * This method should be called only when starting drag and drop operation
     * in the DRAG_DETECTED handler, calling it at other times
     * doesn't have any effect.
     * @param image image to use for the drag view
     * @since JavaFX 8.0
     */
    public void setDragView(Image image) {
        //peer.setDragView(image);
    }

    /**
     * Sets the x position of the cursor of the drag view image.
     * This method should be called only when starting drag and drop operation
     * in the DRAG_DETECTED handler, calling it at other times
     * doesn't have any effect.
     * @param offsetX x position of the cursor over the image
     * @since JavaFX 8.0
     */
    public void setDragViewOffsetX(double offsetX) {
        //peer.setDragViewOffsetX(offsetX);
    }

    /**
     * Sets the y position of the cursor of the drag view image.
     * This method should be called only when starting drag and drop operation
     * in the DRAG_DETECTED handler, calling it at other times
     * doesn't have any effect.
     * @param offsetY y position of the cursor over the image
     * @since JavaFX 8.0
     */
    public void setDragViewOffsetY(double offsetY) {
        //peer.setDragViewOffsetY(offsetY);
    }

    /**
     * Gets the image used as a drag view.
     * This method returns meaningful value only when starting drag and drop
     * operation in the DRAG_DETECTED handler, it returns null at other times.
     * @return the image used as a drag view
     * @since JavaFX 8.0
     */
    public Image getDragView() {
        return null; // peer.getDragView();
    }

    /**
     * Gets the x position of the cursor of the drag view image.
     * This method returns meaningful value only when starting drag and drop
     * operation in the DRAG_DETECTED handler, it returns 0 at other times.
     * @return x position of the cursor over the image
     * @since JavaFX 8.0
     */
    public double getDragViewOffsetX() {
        return 0; // peer.getDragViewOffsetX();
    }

    /**
     * Gets the y position of the cursor of the drag view image.
     * This method returns meaningful value only when starting drag and drop
     * operation in the DRAG_DETECTED handler, it returns 0 at other times.
     * @return y position of the cursor over the image
     * @since JavaFX 8.0
     */
    public double getDragViewOffsetY() {
        return 0; //peer.getDragViewOffsetY();
    }

/*
    static {
        // This is used by classes in different packages to get access to
        // private and package private methods.
        DragboardHelper.setDragboardAccessor((dragboard, restricted) -> {
            dragboard.dataAccessRestricted = restricted;
        });
    }
*/
}
