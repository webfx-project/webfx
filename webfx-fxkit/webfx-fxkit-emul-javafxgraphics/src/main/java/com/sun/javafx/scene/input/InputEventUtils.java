package com.sun.javafx.scene.input;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Node;
import javafx.scene.input.PickResult;
import javafx.scene.input.TransferMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for helper methods needed by input events.
 */
public class InputEventUtils {

    public static Point2D toSunPoint2D(javafx.geometry.Point2D p) {
        return new Point2D((float) p.getX(), (float) p.getY());
    }

    /**
     * Recomputes event coordinates for a different node.
     * @param result Coordinates to recompute
     * @param newSource Node to whose coordinate system to recompute
     * @return the recomputed coordinates
     */
    public static Point2D recomputeCoordinates(PickResult result, Object newSource) {

        Point2D coordinates = result.getIntersectedPoint();
        if (coordinates == null)
            return new Point2D(Float.NaN, Float.NaN);

        Node oldSourceNode = result.getIntersectedNode();
        Node newSourceNode = (newSource instanceof Node) ? (Node) newSource : null;

/*
        final SubScene oldSubScene =
                (oldSourceNode == null ? null : NodeHelper.getSubScene(oldSourceNode));
        final SubScene newSubScene =
                (newSourceNode == null ? null : NodeHelper.getSubScene(newSourceNode));
        final boolean subScenesDiffer = (oldSubScene != newSubScene);
*/

        if (oldSourceNode != null) {
            // transform to scene/nearest-subScene coordinates
            coordinates = toSunPoint2D(oldSourceNode.localToScene(coordinates.x, coordinates.y));
/*
            if (subScenesDiffer && oldSubScene != null) {
                // transform to scene coordiantes
                coordinates = SceneUtils.subSceneToScene(oldSubScene, coordinates);
            }
*/
        }

        if (newSourceNode != null) {

/*
            if (subScenesDiffer && newSubScene != null) {
                // flatten the coords to flat mouse coordinates - project
                // by scene's camera
                Point2D planeCoords = CameraHelper.project(
                        SceneHelper.getEffectiveCamera(newSourceNode.getScene()),
                        coordinates);
                // convert the point to subScene coordinates
                planeCoords = SceneUtils.sceneToSubScenePlane(newSubScene, planeCoords);
                // compute inner intersection with the subScene's camera
                // projection plane
                if (planeCoords == null) {
                    coordinates = null;
                } else {
                    coordinates = CameraHelper.pickProjectPlane(
                            SubSceneHelper.getEffectiveCamera(newSubScene),
                            planeCoords.getX(), planeCoords.getY());
                }
            }
*/
            // transform the point to source's local coordinates
            if (coordinates != null) {
                coordinates = toSunPoint2D(newSourceNode.sceneToLocal(coordinates.x, coordinates.y));
            }
            if (coordinates == null) {
                coordinates = new Point2D(Float.NaN, Float.NaN);
            }
        }

        return coordinates;
    }

    private static final List<TransferMode> TM_ANY =
            Collections.unmodifiableList(Arrays.asList(
                    TransferMode.COPY,
                    TransferMode.MOVE,
                    TransferMode.LINK
            ));

    private static final List<TransferMode> TM_COPY_OR_MOVE =
            Collections.unmodifiableList(Arrays.asList(
                    TransferMode.COPY,
                    TransferMode.MOVE
            ));

    /**
     * Makes sure changes to the static arrays specified in TransferMode
     * don't have any effect on the transfer modes used.
     * @param modes Modes passed in by user
     * @return list containing the passed modes. If one of the static arrays
     *         is passed, the expected modes are returned regardless of the
     *         values in those arrays.
     */
    public static List<TransferMode> safeTransferModes(TransferMode[] modes) {
        if (modes == TransferMode.ANY) {
            return TM_ANY;
        } else if (modes == TransferMode.COPY_OR_MOVE) {
            return TM_COPY_OR_MOVE;
        } else {
            return Arrays.asList(modes);
        }
    }
}
