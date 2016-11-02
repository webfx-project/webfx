package naga.providers.toolkit.javafx.util;

import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public class FxTransforms {

    public static javafx.scene.transform.Transform toFxTransform(Transform transform) {
        if (transform instanceof Rotate)
            return toFxRotate((Rotate) transform);
        return null;
    }

    public static javafx.scene.transform.Rotate toFxRotate(Rotate rotate) {
        javafx.scene.transform.Rotate fxRotate = new javafx.scene.transform.Rotate();
        fxRotate.angleProperty().bind(rotate.angleProperty());
        fxRotate.pivotXProperty().bind(rotate.pivotXProperty());
        fxRotate.pivotYProperty().bind(rotate.pivotYProperty());
        return fxRotate;
    }
}
