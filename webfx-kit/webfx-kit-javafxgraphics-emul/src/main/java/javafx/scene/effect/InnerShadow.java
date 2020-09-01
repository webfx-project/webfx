/*
 * Copyright (c) 2010, 2017, Oracle and/or its affiliates. All rights reserved.
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

package javafx.scene.effect;

import javafx.scene.paint.Color;


public class InnerShadow implements Effect {
    private BlurType blurType;
    private Color color;
    private double radius;
    private double choke;
    private double offsetX;
    private double offsetY;
    private Effect input;

    /**
     * Creates a new instance of InnerShadow with default parameters.
     */
    public InnerShadow() {}

    /**
     * Creates a new instance of InnerShadow with specified radius and color.
     * @param radius the radius of the shadow blur kernel
     * @param color the shadow {@code Color}
     */
    public InnerShadow(double radius, Color color) {
        setRadius(radius);
        setColor(color);
    }

    /**
     * Creates a new instance of InnerShadow with specified radius, offsetX,
     * offsetY and color.
     * @param radius the radius of the shadow blur kernel
     * @param offsetX the shadow offset in the x direction
     * @param offsetY the shadow offset in the y direction
     * @param color the shadow {@code Color}
     */
    public InnerShadow(double radius, double offsetX, double offsetY, Color color) {
        setRadius(radius);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
        setColor(color);
    }

    /**
     * Creates a new instance of InnerShadow with the specified blurType, color,
     * radius, spread, offsetX and offsetY.
     * @param blurType the algorithm used to blur the shadow
     * @param color the shadow {@code Color}
     * @param radius the radius of the shadow blur kernel
     * @param choke the portion of the radius where the contribution of
     * the source material will be 100%
     * @param offsetX the shadow offset in the x direction
     * @param offsetY the shadow offset in the y direction
     * @since JavaFX 2.1
     */
    public InnerShadow(BlurType blurType, Color color, double radius, double choke,
                       double offsetX, double offsetY) {
        setBlurType(blurType);
        setColor(color);
        setRadius(radius);
        setChoke(choke);
        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    public BlurType getBlurType() {
        return blurType;
    }

    public void setBlurType(BlurType blurType) {
        this.blurType = blurType;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getChoke() {
        return choke;
    }

    public void setChoke(double choke) {
        this.choke = choke;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public Effect getInput() {
        return input;
    }

    public void setInput(Effect input) {
        this.input = input;
    }
}
