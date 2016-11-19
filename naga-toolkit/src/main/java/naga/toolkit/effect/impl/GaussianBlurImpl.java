package naga.toolkit.effect.impl;

import naga.toolkit.effect.GaussianBlur;

/**
 * @author Bruno Salmon
 */
public class GaussianBlurImpl implements GaussianBlur {

    private final double radius;

    public GaussianBlurImpl() {
        this(10);
    }

    public GaussianBlurImpl(double radius) {
        this.radius = radius;
    }

    @Override
    public double getRadius() {
        return radius;
    }

}
