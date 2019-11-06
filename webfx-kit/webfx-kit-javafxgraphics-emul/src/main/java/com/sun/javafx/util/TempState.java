package com.sun.javafx.util;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;

/**************************************************************************
 *                                                                        *
 * Temporary state, used to reduce the occurrence of temporary garbage    *
 * while computing things such as bounds and transforms, and while        *
 * picking. Since these operations happen extremely often and must be     *
 * very fast, we need to reduce the load on the garbage collector.        *
 *                                                                        *
 *************************************************************************/
public final class TempState {
    /**
     * A temporary rect used for computing bounds by the various bounds
     * variables. This bounds starts life as a RectBounds, but may be promoted
     * to a BoxBounds if there is a 3D transform mixed into its computation.
     */
    public BaseBounds bounds = new RectBounds(0, 0, -1, -1);

    /**
     * A temporary affine transform used when picking to avoid creating
     * temporary garbage.
     */
    //public final BaseTransform pickTx = new Affine3D();

    /**
     * A temporary affine transform used by the path transition to avoid
     * creating temporary garbage.
     */
    //public final Affine3D leafTx = new Affine3D();

    /**
     * A temporary point used for picking and other purposes.
     */
    public final Point2D point = new Point2D(0, 0);

    //public final Vec3d vec3d = new Vec3d(0, 0, 0);


    /**
     * A temporary general transform used by LOD helper method, in node,
     * to compute area in scene.
     */
    //public final GeneralTransform3D projViewTx = new GeneralTransform3D();

    /**
     * A temporary affine transform used by the LOD helper method to get an
     * affine transform.
     */
    //public final Affine3D tempTx = new Affine3D();

    private static final ThreadLocal<TempState> tempStateRef = new ThreadLocal<>();

    private TempState() {
    }

    public static TempState getInstance() {
        TempState tempState = tempStateRef.get();
        if (tempState == null)
            tempStateRef.set(tempState = new TempState());
        return tempState;
    }
}
