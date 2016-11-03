package naga.providers.toolkit.javafx.util;

import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Transform;
import naga.toolkit.transform.Translate;

/**
 * @author Bruno Salmon
 */
public class FxTransforms {

    public static javafx.scene.transform.Transform toFxTransform(Transform transform) {
        if (transform instanceof Translate)
            return toFxTranslate((Translate) transform);
        if (transform instanceof Rotate)
            return toFxRotate((Rotate) transform);
        return null;
    }

    private static javafx.scene.transform.Translate toFxTranslate(Translate translate) {
        javafx.scene.transform.Translate fxTranslate = new javafx.scene.transform.Translate();
        fxTranslate.xProperty().bind(translate.xProperty());
        fxTranslate.yProperty().bind(translate.yProperty());
        return fxTranslate;
    }

    private static javafx.scene.transform.Rotate toFxRotate(Rotate rotate) {
        javafx.scene.transform.Rotate fxRotate = new javafx.scene.transform.Rotate();
        fxRotate.angleProperty().bind(rotate.angleProperty());
        fxRotate.pivotXProperty().bind(rotate.pivotXProperty());
        fxRotate.pivotYProperty().bind(rotate.pivotYProperty());
        return fxRotate;
    }
}
