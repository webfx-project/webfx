package naga.toolkit.effect;

import naga.toolkit.effect.impl.GaussianBlurImpl;

/**
 * @author Bruno Salmon
 */
public interface GaussianBlur extends Effect {

    double getRadius();

    default double getSigma() {
        return (getRadius() + 1) / Math.sqrt(2 * Math.log(255));
    }

    static GaussianBlur create() {
        return new GaussianBlurImpl();
    }

    static GaussianBlur create(double radius) {
        return new GaussianBlurImpl(radius);
    }
}
