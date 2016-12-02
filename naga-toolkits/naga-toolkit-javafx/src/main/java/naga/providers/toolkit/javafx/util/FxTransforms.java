package naga.providers.toolkit.javafx.util;

import naga.toolkit.fx.scene.transform.Rotate;
import naga.toolkit.fx.scene.transform.Scale;
import naga.toolkit.fx.scene.transform.Transform;
import naga.toolkit.fx.scene.transform.Translate;

/**
 * @author Bruno Salmon
 */
public class FxTransforms {

    public static javafx.scene.transform.Transform toFxTransform(Transform transform) {
        if (transform instanceof Translate)
            return toFxTranslate((Translate) transform);
        if (transform instanceof Rotate)
            return toFxRotate((Rotate) transform);
        if (transform instanceof Scale)
            return toFxScale((Scale) transform);
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

    private static javafx.scene.transform.Scale toFxScale(Scale scale) {
        javafx.scene.transform.Scale fxScale = new javafx.scene.transform.Scale();
        fxScale.xProperty().bind(scale.xProperty());
        fxScale.yProperty().bind(scale.yProperty());
        return fxScale;
    }

}
