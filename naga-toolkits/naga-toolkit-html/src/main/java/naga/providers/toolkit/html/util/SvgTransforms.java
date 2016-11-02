package naga.providers.toolkit.html.util;

import naga.commons.util.collection.Collections;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Transform;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class SvgTransforms {

    public static String toSvgTransforms(Collection<Transform> transforms) {
        if (Collections.isEmpty(transforms))
            return null;
        return toSvgTransforms(transforms, new StringBuilder()).toString();
    }

    private static StringBuilder toSvgTransforms(Collection<Transform> transforms, StringBuilder sb) {
        Collections.forEach(transforms, transform -> toSvgTransform(transform, sb));
        return sb;
    }

    private static StringBuilder toSvgTransform(Transform transform, StringBuilder sb) {
        if (sb.length() > 0)
            sb.append(' ');
        if (transform instanceof Rotate)
            return toSvgRotate((Rotate) transform, sb);
        return sb;
    }

    private static StringBuilder toSvgRotate(Rotate rotate, StringBuilder sb) {
        return sb.append("rotate(").append(rotate.getAngle()).append(' ').append(rotate.getPivotX()).append(' ').append(rotate.getPivotY()).append(')');
    }

}
