package naga.fx.spi.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Bruno Salmon
 */
class SwingGraphicState {
    private final AffineTransform transform;
    private final Composite composite;
    private final Shape clip;

    public SwingGraphicState(AffineTransform transform, Composite composite, Shape clip) {
        this.transform = transform;
        this.composite = composite;
        this.clip = clip;
    }

    AffineTransform getTransform() {
        return transform;
    }

    Composite getComposite() {
        return composite;
    }

    public Shape getClip() {
        return clip;
    }
}
