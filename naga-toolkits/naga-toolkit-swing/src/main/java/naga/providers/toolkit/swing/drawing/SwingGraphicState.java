package naga.providers.toolkit.swing.drawing;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * @author Bruno Salmon
 */
class SwingGraphicState {
    private final AffineTransform transform;
    private final Composite composite;

    SwingGraphicState(AffineTransform transform, Composite composite) {
        this.transform = transform;
        this.composite = composite;
    }

    AffineTransform getTransform() {
        return transform;
    }

    Composite getComposite() {
        return composite;
    }
}
