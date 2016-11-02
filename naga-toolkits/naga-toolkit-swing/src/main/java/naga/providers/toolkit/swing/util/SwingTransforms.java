package naga.providers.toolkit.swing.util;

import naga.commons.util.collection.Collections;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Transform;

import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class SwingTransforms {

    public static AffineTransform toSwingTransform(List<Transform> transforms) {
        if (Collections.isEmpty(transforms))
            return null;
        return toSwingTransform(transforms.get(0));
    }

    private static AffineTransform toSwingTransform(Transform transform) {
        if (transform instanceof Rotate)
            return toSwingRotate((Rotate) transform);
        return null;
    }

    private static AffineTransform toSwingRotate(Rotate rotate) {
        return AffineTransform.getRotateInstance(rotate.getAngle() * Math.PI / 180, rotate.getPivotX(), rotate.getPivotY());
    }
}
