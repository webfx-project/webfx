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

import java.util.ArrayList;
import java.util.List;

import static eu.hansolo.spacefx.Config.HEIGHT;
import static eu.hansolo.spacefx.Config.WIDTH;


public enum WaveType {
    NONE(1, 120),
    TYPE_1_SLOW(1, 240), TYPE_1_MEDIUM(1, 180), TYPE_1_FAST(1, 120),
    TYPE_2_SLOW(1, 240), TYPE_2_MEDIUM(1, 180), TYPE_2_FAST(1, 120),
    TYPE_3_SLOW(1, 240), TYPE_3_MEDIUM(1, 180), TYPE_3_FAST(1, 120),
    TYPE_4_SLOW(1, 240), TYPE_4_MEDIUM(1, 180), TYPE_4_FAST(1, 120),
    TYPE_5_SLOW(3, 460), TYPE_5_MEDIUM(3, 420), TYPE_5_FAST(3, 360),
    TYPE_6_SLOW(1, 240), TYPE_6_MEDIUM(1, 180), TYPE_6_FAST(1, 120),
    TYPE_7_SLOW(1, 240), TYPE_7_MEDIUM(1, 180), TYPE_7_FAST(1, 120),
    TYPE_8_SLOW(1, 240), TYPE_8_MEDIUM(1, 180), TYPE_8_FAST(1, 120),
    TYPE_9_SLOW(1, 240), TYPE_9_MEDIUM(1, 180), TYPE_9_FAST(1, 120),
    TYPE_10_SLOW(4, 300), TYPE_10_MEDIUM(4, 240), TYPE_10_FAST(4, 240),
    TYPE_11_SLOW(4, 300), TYPE_11_MEDIUM(4, 240), TYPE_11_FAST(4, 240),
    TYPE_12_SLOW(5, 360), TYPE_12_MEDIUM(5, 300), TYPE_12_FAST(5, 300),
    TYPE_13_SLOW(5, 360), TYPE_13_MEDIUM(5, 300), TYPE_13_FAST(5, 300);

    private static final double        ENEMY_SIZE = 50;
    public  final int                  waveSegments;
    public  final double               totalFrames;
    //public record WaveCoordinate1(double x, double y, double r) {}
    public  final List<WaveCoordinate> coordinates;


    WaveType(final int waveSegments, final double totalFrames) {
        this.waveSegments = waveSegments;
        this.totalFrames  = totalFrames;
        this.coordinates  = new ArrayList<>();

        double[] ap0;
        double[] cp1;
        double[] cp2;
        double[] ap3;
        switch(this.name()) {
            case "TYPE_1_SLOW":
            case "TYPE_1_MEDIUM":
            case "TYPE_1_FAST":
                ap0 = new double[] { WIDTH + ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { 0, HEIGHT * 0.35 };
                cp2 = new double[] { WIDTH * 0.5, HEIGHT };
                ap3 = new double[] { WIDTH + ENEMY_SIZE, HEIGHT * 0.5 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_2_SLOW":
            case "TYPE_2_MEDIUM":
            case "TYPE_2_FAST":
                ap0 = new double[] { -ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH, HEIGHT * 0.35 };
                cp2 = new double[] { WIDTH * 0.5, HEIGHT };
                ap3 = new double[] { -ENEMY_SIZE, HEIGHT * 0.5 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_3_SLOW":
            case "TYPE_3_MEDIUM":
            case "TYPE_3_FAST":
                ap0 = new double[] { -ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.5, HEIGHT * 0.35 };
                cp2 = new double[] { 0, HEIGHT };
                ap3 = new double[] { WIDTH + ENEMY_SIZE, HEIGHT * 0.5 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_4_SLOW":
            case "TYPE_4_MEDIUM":
            case "TYPE_4_FAST":
                ap0 = new double[] { WIDTH + ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.5, HEIGHT * 0.35 };
                cp2 = new double[] { WIDTH, HEIGHT };
                ap3 = new double[] { -ENEMY_SIZE, HEIGHT * 0.5 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_5_SLOW":
            case "TYPE_5_MEDIUM":
            case "TYPE_5_FAST":
                ap0 = new double[] { -ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.11428571, HEIGHT * 0.08888889 };
                cp2 = new double[] { WIDTH * 0.09285714, HEIGHT * 0.53333333 };
                ap3 = new double[] { WIDTH * 0.3, HEIGHT * 0.63888889 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.3, HEIGHT * 0.63888889 };
                cp1 = new double[] { WIDTH, HEIGHT };
                cp2 = new double[] { WIDTH * 1.08571429, HEIGHT * 0.22777778 };
                ap3 = new double[] { WIDTH * 0.54285714, HEIGHT * 0.35555556 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.54285714, HEIGHT * 0.35555556 };
                cp1 = new double[] { WIDTH * 0.3, HEIGHT * 0.41111111 };
                cp2 = new double[] { WIDTH * -0.37539683, HEIGHT * 0.8 };
                ap3 = new double[] { WIDTH * 0.76, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_6_SLOW":
            case "TYPE_6_MEDIUM":
            case "TYPE_6_FAST":
                ap0 = new double[] { 0, HEIGHT + ENEMY_SIZE };
                cp1 = new double[] { 0, 0 };
                cp2 = new double[] { WIDTH, 0 };
                ap3 = new double[] { WIDTH, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_7_SLOW":
            case "TYPE_7_MEDIUM":
            case "TYPE_7_FAST":
                ap0 = new double[] { -ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { 0, HEIGHT * 0.5 };
                cp2 = new double[] { WIDTH, 0 };
                ap3 = new double[] { WIDTH * 0.5, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_8_SLOW":
            case "TYPE_8_MEDIUM":
            case "TYPE_8_FAST":
                ap0 = new double[] { WIDTH + ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH, HEIGHT * 0.5 };
                cp2 = new double[] { 0, 0 };
                ap3 = new double[] { WIDTH * 0.5, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_9_SLOW":
            case "TYPE_9_MEDIUM":
            case "TYPE_9_FAST":
                ap0 = new double[] { WIDTH * 0.5, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * -0.92857143, HEIGHT * 0.66666667 };
                cp2 = new double[] { WIDTH * 1.85714286, HEIGHT * 0.24444444 };
                ap3 = new double[] { WIDTH * 0.5, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_10_SLOW":
            case "TYPE_10_MEDIUM":
            case "TYPE_10_FAST":
                ap0 = new double[] { WIDTH * 0.5, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.5, HEIGHT * 0.33333333 };
                cp2 = new double[] { WIDTH * 0.5, HEIGHT * 0.38888889 };
                ap3 = new double[] { WIDTH * 0.317142857142857, HEIGHT * 0.48 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.317142857142857, HEIGHT * 0.48 };
                cp1 = new double[] { WIDTH * 0.0285714285714286, HEIGHT * 0.611111111111111 };
                cp2 = new double[] { WIDTH * -0.0428571428571429, HEIGHT * 0.255555555555556 };
                ap3 = new double[] { WIDTH * 0.26, HEIGHT * 0.316666666666667 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.26, HEIGHT * 0.316666666666667 };
                cp1 = new double[] { WIDTH * 0.557142857142857, HEIGHT * 0.388888888888889 };
                cp2 = new double[] { WIDTH * 0.857142857142857, HEIGHT * 0.533333333333333 };
                ap3 = new double[] { WIDTH * 0.635714285714286, HEIGHT * 0.75 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.635714285714286, HEIGHT * 0.75 };
                cp1 = new double[] { WIDTH * 0.442857142857143, HEIGHT * 0.911111111111111 };
                cp2 = new double[] { WIDTH * 0.142857142857143, HEIGHT * 0.833333333333333 };
                ap3 = new double[] { -ENEMY_SIZE, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_11_SLOW":
            case "TYPE_11_MEDIUM":
            case "TYPE_11_FAST":
                ap0 = new double[] { WIDTH * 0.5, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.5, HEIGHT * 0.33333333 };
                cp2 = new double[] { WIDTH * 0.5, HEIGHT * 0.38888889 };
                ap3 = new double[] { WIDTH * 0.682857142857143, HEIGHT * 0.48 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.682857142857143, HEIGHT * 0.48 };
                cp1 = new double[] { WIDTH * 0.971428571428571, HEIGHT * 0.611111111111111 };
                cp2 = new double[] { WIDTH * 1.04285714285714, HEIGHT * 0.255555555555556 };
                ap3 = new double[] { WIDTH * 0.74, HEIGHT * 0.316666666666667 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.74, HEIGHT * 0.316666666666667 };
                cp1 = new double[] { WIDTH * 0.442857142857143, HEIGHT * 0.388888888888889 };
                cp2 = new double[] { WIDTH * 0.142857142857143, HEIGHT * 0.533333333333333 };
                ap3 = new double[] { WIDTH * 0.364285714285714, HEIGHT * 0.75 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.364285714285714, HEIGHT * 0.75 };
                cp1 = new double[] { WIDTH * 0.557142857142857, HEIGHT * 0.911111111111111 };
                cp2 = new double[] { WIDTH * 0.857142857142857, HEIGHT * 0.833333333333333 };
                ap3 = new double[] { WIDTH + ENEMY_SIZE, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_12_SLOW":
            case "TYPE_12_MEDIUM":
            case "TYPE_12_FAST":
                ap0 = new double[] { -ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.117142857142857, HEIGHT * 0.0888888888888889 };
                cp2 = new double[] { WIDTH * 0, HEIGHT * 0.477777777777778 };
                ap3 = new double[] { WIDTH * 0.428571428571429, HEIGHT * 0.477777777777778 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.428571428571429, HEIGHT * 0.477777777777778 };
                cp1 = new double[] { WIDTH * 0.688571428571429, HEIGHT * 0.477777777777778 };
                cp2 = new double[] { WIDTH * 0.688571428571429, HEIGHT * 0.194444444444444 };
                ap3 = new double[] { WIDTH * 0.428571428571429, HEIGHT * 0.194444444444444 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.428571428571429, HEIGHT * 0.194444444444444 };
                cp1 = new double[] { WIDTH * 0.3, HEIGHT * 0.194444444444444 };
                cp2 = new double[] { WIDTH * 0.228571428571429, HEIGHT * 0.255555555555556 };
                ap3 = new double[] { WIDTH * 0.228571428571429, HEIGHT * 0.344444444444444 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.228571428571429, HEIGHT * 0.344444444444444 };
                cp1 = new double[] { WIDTH * 0.228571428571429, HEIGHT * 0.535555555555556 };
                cp2 = new double[] { WIDTH * 0.785714285714286, HEIGHT * 0.488888888888889 };
                ap3 = new double[] { WIDTH * 0.785714285714286, HEIGHT * 0.683333333333333 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.785714285714286, HEIGHT * 0.683333333333333 };
                cp1 = new double[] { WIDTH * 0.785714285714286, HEIGHT * 0.933333333333333 };
                cp2 = new double[] { WIDTH * 0.0285714285714286, HEIGHT * 0.866666666666667 };
                ap3 = new double[] { -ENEMY_SIZE, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
            case "TYPE_13_SLOW":
            case "TYPE_13_MEDIUM":
            case "TYPE_13_FAST":
                ap0 = new double[] { WIDTH + ENEMY_SIZE, -ENEMY_SIZE };
                cp1 = new double[] { WIDTH * 0.882857142857143, HEIGHT * 0.0888888888888889 };
                cp2 = new double[] { WIDTH * 1, HEIGHT * 0.477777777777778 };
                ap3 = new double[] { WIDTH * 0.571428571428571, HEIGHT * 0.477777777777778 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.571428571428571, HEIGHT * 0.477777777777778 };
                cp1 = new double[] { WIDTH * 0.311428571428571, HEIGHT * 0.477777777777778 };
                cp2 = new double[] { WIDTH * 0.311428571428571, HEIGHT * 0.194444444444444 };
                ap3 = new double[] { WIDTH * 0.571428571428571, HEIGHT * 0.194444444444444 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.571428571428571, HEIGHT * 0.194444444444444 };
                cp1 = new double[] { WIDTH * 0.7, HEIGHT * 0.194444444444444 };
                cp2 = new double[] { WIDTH * 0.771428571428571, HEIGHT * 0.255555555555556 };
                ap3 = new double[] { WIDTH * 0.771428571428571, HEIGHT * 0.344444444444444 };
                coordinates.addAll(addCoordinates(totalFrames / 4, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.771428571428571, HEIGHT * 0.344444444444444 };
                cp1 = new double[] { WIDTH * 0.771428571428571, HEIGHT * 0.535555555555556 };
                cp2 = new double[] { WIDTH * 0.214285714285714, HEIGHT * 0.488888888888889 };
                ap3 = new double[] { WIDTH * 0.214285714285714, HEIGHT * 0.683333333333333 };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));

                ap0 = new double[] { WIDTH * 0.214285714285714, HEIGHT * 0.683333333333333 };
                cp1 = new double[] { WIDTH * 0.214285714285714, HEIGHT * 0.933333333333333 };
                cp2 = new double[] { WIDTH * 0.971428571428571, HEIGHT * 0.866666666666667 };
                ap3 = new double[] { WIDTH + ENEMY_SIZE, HEIGHT + ENEMY_SIZE };
                coordinates.addAll(addCoordinates(totalFrames / waveSegments, ap0, cp1, cp2, ap3));
                break;
                case "NONE":
            default:
                ap0  = new double[] { 0, 0 };
                cp1  = new double[] { 0, 0 };
                cp2  = new double[] { 0, 0 };
                ap3  = new double[] { 0, 0 };
                break;
        }
    }

    private List<WaveCoordinate> addCoordinates(final double totalFrames, final double[] ap0, final double[] cp1, final double[] cp2, final double[] ap3) {
        double               oldX        = ap0[0];
        double               oldY        = ap0[1];
        List<WaveCoordinate> coordinates = new ArrayList<>();
        for (double frameCount = 0; frameCount < totalFrames; frameCount++) {
            double   t  = frameCount / totalFrames;
            double[] p  = Helper.bezierCurve4Points(t, ap0[0], ap0[1], cp1[0], cp1[1], cp2[0], cp2[1], ap3[0], ap3[1]);
            double   vX = p[0] - oldX;
            double   vY = p[1] - oldY;
            double   r  = Math.toDegrees(Math.atan2(vY, vX)) - 90.0;
            coordinates.add(new WaveCoordinate(p[0], p[1], r));
            oldX = p[0];
            oldY = p[1];
        }
        return coordinates;
    }
}
