/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.spacefx;


import javafx.scene.Node;


public class Helper {

    public static final void enableNode(final Node node, final boolean enable) {
        node.setVisible(enable);
        node.setManaged(enable);
    }

    public static final boolean isInsideCircle(final double cX, final double cY, final double r, final double x, final double y) {
        return ((x - cX) * (x - cX) + (y - cY) * (y - cY) <= r * r);
    }

    public static final double[] rotatePointAroundRotationCenter(final double x, final double y, final double rX, final double rY, final double angleDeg) {
        final double rad = Math.toRadians(angleDeg);
        final double sin = Math.sin(rad);
        final double cos = Math.cos(rad);
        final double nX  = rX + (x - rX) * cos - (y - rY) * sin;
        final double nY  = rY + (x - rX) * sin + (y - rY) * cos;
        return new double[] { nX, nY };
    }

    /*
     * t   -> Point in time  (Values between 0 - 1)
     * ap0 -> AnchorPoint  (Start point of the curve)
     * cp1 -> ControlPoint (Control point of the curve)
     * ap2 -> AnchorPoint  (End point of the curve)
     */
    public static final double[] bezierCurve3Points(final double t, final double ap0x, final double ap0y, final double cp1x, final double cp1y, final double ap2x, final double ap2y) {
        double oneMinusT         = (1 - t);
        double oneMinusTSquared2 = (oneMinusT * oneMinusT);
        double tSquared2         = t * t;

        double x = oneMinusTSquared2 * ap0x + 2 * oneMinusT * t * cp1x + tSquared2 * ap2x;
        double y = oneMinusTSquared2 * ap0y + 2 * oneMinusT * t * cp1y + tSquared2 * ap2y;
        //double x = (1 - t) * (1 - t) * ap0x + 2 * (1 - t) * t * cp1x + t * t * ap2x;
        //double y = (1 - t) * (1 - t) * ap0y + 2 * (1 - t) * t * cp1y + t * t * ap2y;
        return new double[] { x, y };
    }

    /*
     * t   -> Point in time  (Values between 0 - 1)
     * ap0 -> AnchorPoint    (Start point of the curve)
     * cp1 -> ControlPoint 1 (1st control point of the curve)
     * cp2 -> ControlPoint 2 (2nd control point of the curve)
     * ap3 -> AnchorPoint    (End point of the curve)
     */
    public static final double[] bezierCurve4Points(final double t, final double ap0x, final double ap0y, final double cp1x, final double cp1y, final double cp2x, final double cp2y, final double ap3x, final double ap3y) {
        double oneMinusT         = (1 - t);
        double oneMinusTSquared2 = (1 - t) * (1 - t);
        double oneMinusTSquared3 = (1 - t) * (1 - t) * (1 - t);
        double tSquared2         = t * t;
        double txSquared3        = t * t * t;

        double x = oneMinusTSquared3 * ap0x + 3 * oneMinusTSquared2 * t * cp1x + 3 * oneMinusT * tSquared2 * cp2x + txSquared3 * ap3x;
        double y = oneMinusTSquared3 * ap0y + 3 * oneMinusTSquared2 * t * cp1y + 3 * oneMinusT * tSquared2 * cp2y + txSquared3 * ap3y;

        return new double[] { x, y };
    }
}
