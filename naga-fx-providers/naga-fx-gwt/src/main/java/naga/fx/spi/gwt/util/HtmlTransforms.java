package naga.fx.spi.gwt.util;

import naga.util.collection.Collections;
import emul.javafx.scene.transform.*;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class HtmlTransforms {

    public static String toHtmlTransforms(Collection<Transform> transforms) {
        if (Collections.isEmpty(transforms))
            return null;
        return toHtmlTransforms(transforms, new StringBuilder()).toString();
    }

    private static StringBuilder toHtmlTransforms(Collection<Transform> transforms, StringBuilder sb) {
        Collections.forEach(transforms, transform -> toHtmlTransform(transform, sb));
        return sb;
    }

    private static StringBuilder toHtmlTransform(Transform transform, StringBuilder sb) {
        if (sb.length() > 0)
            sb.append(' ');
        if (transform instanceof Translate)
            return toHtmlTranslate((Translate) transform, sb);
        if (transform instanceof Rotate)
            return toHtmlRotate((Rotate) transform, sb);
        if (transform instanceof Scale)
            return toHtmlScale((Scale) transform, sb);
        return sb;
    }

    private static StringBuilder toHtmlTranslate(Translate translate, StringBuilder sb) {
        return sb.append("translate(").append(translate.getX()).append("px, ").append(translate.getY()).append("px)");
    }

    private static StringBuilder toHtmlRotate(Rotate rotate, StringBuilder sb) {
        double px = rotate.getPivotX();
        double py = rotate.getPivotY();
        if (px == 0 && py == 0)
            return sb.append("rotate(").append(rotate.getAngle()).append("deg)");
        return toHtmlAffine(rotate.toAffine(), sb);
    }

    private static StringBuilder toHtmlScale(Scale scale, StringBuilder sb) {
        return sb.append("scale(").append(scale.getX()).append("px, ").append(scale.getY()).append("px)");
    }

    private static StringBuilder toHtmlAffine(Affine a, StringBuilder sb) {
        return sb.append("matrix(").append(a.getMxx()).append(',').append(a.getMyx()).append(',').append(a.getMxy()).append(',').append(a.getMyy()).append(',').append(a.getTx()).append(',').append(a.getTy()).append(')');
    }
}
