/*
 * Copyright (c) 2010, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package javafx.scene.robot;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public interface RobotPeer {

    /**
     * Presses the specified {@link KeyCode} key.
     *
     * @param keyCode the key to press
     */
    void keyPress(KeyCode keyCode);

    /**
     * Releases the specified {@link KeyCode} key.
     *
     * @param keyCode the key to release
     */
    void keyRelease(KeyCode keyCode);

    /**
     * Returns the current mouse x-position.
     *
     * @return the current mouse x-position
     */
    double getMouseX();

    /**
     * Returns the current mouse y-position.
     *
     * @return the current mouse y-position
     */
    double getMouseY();

    /**
     * Moves the mouse to the specified (x,y) screen coordinates relative to
     * the primary screen.
     *
     * @param x screen coordinate x to move the mouse to
     * @param y screen coordinate y to move the mouse to
     */
    void mouseMove(double x, double y);

    /**
     * Presses the specified {@link MouseButton}s.
     *
     * @param buttons the mouse buttons to press
     */
    void mousePress(MouseButton... buttons);

    /**
     * Releases the specified {@link MouseButton}s.
     *
     * @param buttons the mouse buttons to release
     */
    void mouseRelease(MouseButton... buttons);

    /**
     * Scrolls the mouse wheel by the specified amount of wheel clicks. A positive
     * {@code wheelAmt} scrolls the wheel towards the user (down) whereas negative
     * amounts scrolls the wheel away from the user (up).
     *
     * @param wheelAmt the (signed) amount of clicks to scroll the wheel
     */
    void mouseWheel(int wheelAmt);

    /**
     * Returns the {@link Color} of the pixel at the screen coordinates relative to the
     * primary screen specified by {@code location}. Regardless of the scale of the screen
     * ({@link Screen#getOutputScaleX()}, {@link Screen#getOutputScaleY()}), this method only
     * samples a single pixel. For example, on a HiDPI screen with output scale 2, the screen
     * unit at the point (x,y) may have 4 pixels. In this case the color returned is the color
     * of the top, left pixel. Color values are <em>not</em> averaged when a screen unit is
     * made up of more than one pixel.
     *
     * @param x the x coordinate to get the pixel color from
     * @param y the y coordinate to get the pixel color from
     * @return the pixel color at the specified screen coordinates
     */
    Color getPixelColor(double x, double y);

    /**
     * Captures the specified rectangular area of the screen and uses it to fill the given
     * {@code data} array with the raw pixel data. The data is in RGBA format where each
     * pixel in the image is encoded as 4 bytes - one for each color component of each
     * pixel. If this method is not overridden by subclasses then
     * {@link #getScreenCapture(WritableImage, double, double, double, double, boolean)}
     * must be overridden to not call this method.
     *
     * @param x the starting x-position of the rectangular area to capture
     * @param y the starting y-position of the rectangular area to capture
     * @param width the width of the rectangular area to capture
     * @param height the height of the rectangular area to capture
     * @param data the array to fill with the raw pixel data corresponding to
     * the captured region
     * @param scaleToFit If {@literal true} the returned {@code Image} will be
     * scaled to fit the request dimensions, if necessary. Otherwise the size
     * of the returned image will depend on the output scale (DPI) of the primary
     * screen.
     */
    default void getScreenCapture(int x, int y, int width, int height, int[] data, boolean scaleToFit) {
        throw new InternalError("not implemented");
    }

    /**
     * Returns an {@code Image} containing the specified rectangular area of the screen.
     * <p>
     * If the {@code scaleToFit} argument is {@literal false}, the returned
     * {@code Image} object dimensions may differ from the requested {@code width}
     * and {@code height} depending on how many physical pixels the area occupies
     * on the screen. E.g. in HiDPI mode on the Mac (aka Retina display) the pixels
     * are doubled, and thus a screen capture of an area of size (10x10) pixels
     * will result in an {@code Image} with dimensions (20x20). Calling code should
     * use the returned images's {@link Image#getWidth() and {@link Image#getHeight()
     * methods to determine the actual image size.
     * <p>
     * If {@code scaleToFit} is {@literal true}, the returned {@code Image} is of
     * the requested size. Note that in this case the image will be scaled in
     * order to fit to the requested dimensions if necessary such as when running
     * on a HiDPI display.
     *
     * @param x the starting x-position of the rectangular area to capture
     * @param y the starting y-position of the rectangular area to capture
     * @param width the width of the rectangular area to capture
     * @param height the height of the rectangular area to capture
     * @param scaleToFit If {@literal true} the returned {@code Image} will be
     * scaled to fit the request dimensions, if necessary. Otherwise the size
     * of the returned image will depend on the output scale (DPI) of the primary
     * screen.
     */
    default WritableImage getScreenCapture(WritableImage image, double x, double y, double width,
                                          double height, boolean scaleToFit) {
        throw new InternalError("not implemented");
    }

}
