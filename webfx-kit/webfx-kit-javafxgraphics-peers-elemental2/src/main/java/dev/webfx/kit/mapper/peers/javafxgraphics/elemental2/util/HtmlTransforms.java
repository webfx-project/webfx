package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import dev.webfx.platform.util.collection.Collections;
import javafx.scene.transform.*;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class HtmlTransforms {

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
        return toHtmlAffine(transform.toAffine(), sb);
    }

    private static StringBuilder toHtmlTranslate(Translate translate, StringBuilder sb) {
        return sb.append("translate(").append(translate.getX()).append("px, ").append(translate.getY()).append("px)");
    }

    private static StringBuilder toHtmlRotate(Rotate rotate, StringBuilder sb) {
        if (Rotate.X_AXIS.equals(rotate.getAxis())) // Assuming half height pivot (used for FlipPanel in EnzoClocks demo) TODO: generalize pivot
            return sb.append("rotateX(").append(rotate.getAngle()).append("deg)");
        if (Rotate.Y_AXIS.equals(rotate.getAxis())) // Assuming half width pivot (used for FlipPanel in EnzoClocks demo) TODO: generalize pivot
            return sb.append("rotateY(").append(rotate.getAngle()).append("deg)");
        //if (rotate.getPivotX() == 0 && rotate.getPivotY() == 0) // Commented as the correct condition is when the pivot is at the node center (no way to test this here)
        //    return sb.append("rotate(").append(rotate.getAngle()).append("deg)");
        return toHtmlAffine(rotate.toAffine(), sb);
    }

    private static StringBuilder toHtmlScale(Scale scale, StringBuilder sb) {
        // The following code should be commented as the correct condition is when the pivot is at the node center and
        // there is no way to test this here. However, if we uncomment it, this breaks the WebFX Website LongTermCard
        // (the HTML5 red frame is incorrectly shifted). TODO: generalize pivot
        if (scale.getPivotX() == 0 && scale.getPivotY() == 0)
            return sb.append("scale(").append(scale.getX()).append(", ").append(scale.getY()).append(")");
        return toHtmlAffine(scale.toAffine(), sb);
    }

    private static StringBuilder toHtmlAffine(Affine a, StringBuilder sb) {
        return sb.append("matrix(").append(a.getMxx()).append(',').append(a.getMyx()).append(',').append(a.getMxy()).append(',').append(a.getMyy()).append(',').append(a.getTx()).append(',').append(a.getTy()).append(')');
    }
}
