package naga.providers.toolkit.swing.util;

import naga.commons.util.collection.Collections;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Scale;
import naga.toolkit.transform.Transform;
import naga.toolkit.transform.Translate;

import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class SwingTransforms {

    public static AffineTransform toSwingTransform(List<Transform> transforms) {
        if (Collections.isEmpty(transforms))
            return null;
        return transforms.stream().map(SwingTransforms::toSwingTransform).reduce(SwingTransforms::concatenate).get();
    }

    private static AffineTransform concatenate(AffineTransform t1, AffineTransform t2) {
        t1.concatenate(t2);
        return t1;
    }

    private static AffineTransform toSwingTransform(Transform transform) {
        if (transform instanceof Translate)
            return toSwingTranslate((Translate) transform);
        if (transform instanceof Rotate)
            return toSwingRotate((Rotate) transform);
        if (transform instanceof Scale)
            return toSwingScale((Scale) transform);
        return null;
    }

    private static AffineTransform toSwingTranslate(Translate translate) {
        return AffineTransform.getTranslateInstance(translate.getX(), translate.getY());
    }

    private static AffineTransform toSwingRotate(Rotate rotate) {
        return AffineTransform.getRotateInstance(rotate.getAngle() * Math.PI / 180, rotate.getPivotX(), rotate.getPivotY());
    }

    private static AffineTransform toSwingScale(Scale scale) {
        return AffineTransform.getScaleInstance(scale.getX(), scale.getY());
    }

}
