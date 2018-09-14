package emul.com.sun.javafx.util;

import emul.javafx.geometry.*;
import emul.javafx.scene.Node;
import emul.javafx.scene.Scene;
import webfx.fxkits.core.FxKit;
import emul.javafx.stage.Screen;
import emul.javafx.stage.Window;

/**
 * @author Bruno Salmon
 */
public class Utils {

    public static Point2D pointRelativeTo(Node parent, Node node, HPos hpos,
                                          VPos vpos, double dx, double dy, boolean reposition)
    {
        final double nodeWidth = node.getLayoutBounds().getWidth();
        final double nodeHeight = node.getLayoutBounds().getHeight();
        return pointRelativeTo(parent, nodeWidth, nodeHeight, hpos, vpos, dx, dy, reposition);
    }

    public static Point2D pointRelativeTo(Node parent, double anchorWidth,
                                          double anchorHeight, HPos hpos, VPos vpos, double dx, double dy,
                                          boolean reposition)
    {
        final Bounds parentBounds = getBounds(parent);
        Scene scene = parent.getScene();
/*
        NodeOrientation orientation = parent.getEffectiveNodeOrientation();

        if (orientation == NodeOrientation.RIGHT_TO_LEFT) {
            if (hpos == HPos.LEFT) {
                hpos = HPos.RIGHT;
            } else if (hpos == HPos.RIGHT) {
                hpos = HPos.LEFT;
            }
            dx *= -1;
        }
*/

        double layoutX = positionX(parentBounds, anchorWidth, hpos) + dx;
        final double layoutY = positionY(parentBounds, anchorHeight, vpos) + dy;

/*
        if (orientation == NodeOrientation.RIGHT_TO_LEFT && hpos == HPos.CENTER) {
            //TODO - testing for an instance of Stage seems wrong but works for menus
            if (scene.getWindow() instanceof Stage) {
                layoutX = layoutX + parentBounds.getWidth() - anchorWidth;
            } else {
                layoutX = layoutX - parentBounds.getWidth() - anchorWidth;
            }
        }
*/

        if (reposition) {
            return pointRelativeTo(parent, anchorWidth, anchorHeight, layoutX, layoutY, hpos, vpos);
        } else {
            return new Point2D(layoutX, layoutY);
        }
    }

    /**
     * This is the fallthrough function that most other functions fall into. It takes
     * care specifically of the repositioning of the item such that it remains onscreen
     * as best it can, given it's unique qualities.
     *
     * As will all other functions, this one returns a Point2D that represents an x,y
     * location that should safely position the item onscreen as best as possible.
     *
     * Note that <code>width</code> and <height> refer to the width and height of the
     * node/popup that is needing to be repositioned, not of the parent.
     *
     * Don't use the BASELINE vpos, it doesn't make sense and would produce wrong result.
     */
    public static Point2D pointRelativeTo(Object parent, double width,
                                          double height, double screenX, double screenY, HPos hpos, VPos vpos)
    {
        double finalScreenX = screenX;
        double finalScreenY = screenY;
        final Bounds parentBounds = getBounds(parent);

        // ...and then we get the bounds of this screen
        final Screen currentScreen = getScreen(parent);
        final Rectangle2D screenBounds =
/*                hasFullScreenStage(currentScreen)
                        ? currentScreen.getBounds()
                        :*/ currentScreen.getVisualBounds();

        // test if this layout will force the node to appear outside
        // of the screens bounds. If so, we must reposition the item to a better position.
        // We firstly try to do this intelligently, so as to not overlap the parent if
        // at all possible.
        if (hpos != null) {
            // Firstly we consider going off the right hand side
            if ((finalScreenX + width) > screenBounds.getMaxX()) {
                finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
            }

            // don't let the node go off to the left of the current screen
            if (finalScreenX < screenBounds.getMinX()) {
                finalScreenX = positionX(parentBounds, width, getHPosOpposite(hpos, vpos));
            }
        }

        if (vpos != null) {
            // don't let the node go off the bottom of the current screen
            if ((finalScreenY + height) > screenBounds.getMaxY()) {
                finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos,vpos));
            }

            // don't let the node out of the top of the current screen
            if (finalScreenY < screenBounds.getMinY()) {
                finalScreenY = positionY(parentBounds, height, getVPosOpposite(hpos,vpos));
            }
        }

        // --- after all the moving around, we do one last check / rearrange.
        // Unlike the check above, this time we are just fully committed to keeping
        // the item on screen at all costs, regardless of whether or not that results
        /// in overlapping the parent object.
        if ((finalScreenX + width) > screenBounds.getMaxX()) {
            finalScreenX -= (finalScreenX + width - screenBounds.getMaxX());
        }
        if (finalScreenX < screenBounds.getMinX()) {
            finalScreenX = screenBounds.getMinX();
        }
        if ((finalScreenY + height) > screenBounds.getMaxY()) {
            finalScreenY -= (finalScreenY + height - screenBounds.getMaxY());
        }
        if (finalScreenY < screenBounds.getMinY()) {
            finalScreenY = screenBounds.getMinY();
        }

        return new Point2D(finalScreenX, finalScreenY);
    }

    /**
     * This function attempts to determine the best screen given the parent object
     * from which we are wanting to position another item relative to. This is particularly
     * important when we want to keep items from going off screen, and for handling
     * multiple monitor support.
     */
    public static Screen getScreen(Object obj) {
        return FxKit.getPrimaryScreen();
    }

    /*
     * Simple utitilty function to return the 'opposite' value of a given HPos, taking
     * into account the current VPos value. This is used to try and avoid overlapping.
     */
    private static HPos getHPosOpposite(HPos hpos, VPos vpos) {
        if (vpos == VPos.CENTER) {
            if (hpos == HPos.LEFT){
                return HPos.RIGHT;
            } else if (hpos == HPos.RIGHT){
                return HPos.LEFT;
            } else if (hpos == HPos.CENTER){
                return HPos.CENTER;
            } else {
                // by default center for now
                return HPos.CENTER;
            }
        } else {
            return HPos.CENTER;
        }
    }

    /*
     * Simple utitilty function to return the 'opposite' value of a given VPos, taking
     * into account the current HPos value. This is used to try and avoid overlapping.
     */
    private static VPos getVPosOpposite(HPos hpos, VPos vpos) {
        if (hpos == HPos.CENTER) {
            if (vpos == VPos.BASELINE){
                return VPos.BASELINE;
            } else if (vpos == VPos.BOTTOM){
                return VPos.TOP;
            } else if (vpos == VPos.CENTER){
                return VPos.CENTER;
            } else if (vpos == VPos.TOP){
                return VPos.BOTTOM;
            } else {
                // by default center for now
                return VPos.CENTER;
            }
        } else {
            return VPos.CENTER;
        }
    }


    /**
     * To facilitate multiple types of parent object, we unfortunately must allow for
     * Objects to be passed in. This method handles determining the bounds of the
     * given Object. If the Object type is not supported, a default Bounds will be returned.
     */
    private static Bounds getBounds(Object obj) {
        if (obj instanceof Node) {
            final Node n = (Node)obj;
            Bounds b = null; // n.localToScreen(n.getLayoutBounds());
            return b != null ? b : new BoundingBox(0, 0, 0, 0);
        } else if (obj instanceof Window) {
            final Window window = (Window)obj;
            return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        } else {
            return new BoundingBox(0, 0, 0, 0);
        }
    }

    /**
     * Utility function that returns the x-axis position that an object should be positioned at,
     * given the parents screen bounds, the width of the object, and
     * the required HPos.
     */
    private static double positionX(Bounds parentBounds, double width, HPos hpos) {
        if (hpos == HPos.CENTER) {
            // this isn't right, but it is needed for root menus to show properly
            return parentBounds.getMinX();
        } else if (hpos == HPos.RIGHT) {
            return parentBounds.getMaxX();
        } else if (hpos == HPos.LEFT) {
            return parentBounds.getMinX() - width;
        } else {
            return 0;
        }
    }

    /**
     * Utility function that returns the y-axis position that an object should be positioned at,
     * given the parents screen bounds, the height of the object, and
     * the required VPos.
     *
     * The BASELINE vpos doesn't make sense here, 0 is returned for it.
     */
    private static double positionY(Bounds parentBounds, double height, VPos vpos) {
        if (vpos == VPos.BOTTOM) {
            return parentBounds.getMaxY();
        } else if (vpos == VPos.CENTER) {
            return parentBounds.getMinY();
        } else if (vpos == VPos.TOP) {
            return parentBounds.getMinY() - height;
        } else {
            return 0;
        }
    }

}
