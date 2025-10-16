package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import dev.webfx.platform.util.collection.Collections;
import javafx.scene.transform.*;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class SvgTransforms {

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
        if (transform instanceof Translate)
            return toSvgTranslate((Translate) transform, sb);
        if (transform instanceof Rotate)
            return toSvgRotate((Rotate) transform, sb);
        if (transform instanceof Scale)
            return toSvgScale((Scale) transform, sb);
        return sb;
    }

    private static StringBuilder toSvgTranslate(Translate translate, StringBuilder sb) {
        return sb.append("translate(").append(translate.getX()).append(' ').append(translate.getY()).append(')');
    }

    private static StringBuilder toSvgRotate(Rotate rotate, StringBuilder sb) {
        return sb.append("rotate(").append(rotate.getAngle()).append(' ').append(rotate.getPivotX()).append(' ').append(rotate.getPivotY()).append(')');
    }

    private static StringBuilder toSvgScale(Scale scale, StringBuilder sb) {
        return sb.append("scale(").append(scale.getX()).append(' ').append(scale.getY()).append(')');
    }

}
