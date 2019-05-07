package webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics;

import elemental2.dom.Element;
import elemental2.svg.SVGPathElement;
import elemental2.svg.SVGRect;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.*;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerMixin;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class SvgPathPeer
        <N extends Path, NB extends PathPeerBase<N, NB, NM>, NM extends PathPeerMixin<N, NB, NM>>

        extends SvgShapePeer<N, NB, NM>
        implements PathPeerMixin<N, NB, NM> {

    public SvgPathPeer() {
        this((NB) new PathPeerBase(), SvgUtil.createSvgPath());
    }

    public SvgPathPeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    public void updateFillRule(FillRule fillRule) {

    }

    @Override
    public void updateElements(List<PathElement> elements, ListChangeListener.Change<PathElement> change) {
        setElementAttribute("d", toSvgPathData(elements));
        updateLocalToParentTransforms(getNode().localToParentTransforms());
    }

    private String toSvgPathData(List<PathElement> elements) {
        StringBuilder sb = new StringBuilder();
        Collections.forEach(elements, e -> appendPathElementToSvgPathData(e, sb));
        return sb.toString();
    }

    private void appendPathElementToSvgPathData(PathElement pathElement, StringBuilder sb) {
        if (sb.length() > 0)
            sb.append(' ');
        if (pathElement instanceof MoveTo) {
            MoveTo moveTo = (MoveTo) pathElement;
            sb.append('M').append(moveTo.getX()).append(',').append(moveTo.getY());
        } else if (pathElement instanceof LineTo) {
            LineTo lineTo = (LineTo) pathElement;
            sb.append('L').append(lineTo.getX()).append(',').append(lineTo.getY());
        } else if (pathElement instanceof CubicCurveTo) {
            CubicCurveTo cubicCurveTo = (CubicCurveTo) pathElement;
            sb.append('C').append(cubicCurveTo.getControlX1()).append(',').append(cubicCurveTo.getControlY1()).append(' ').append(cubicCurveTo.getControlX2()).append(',').append(cubicCurveTo.getControlY2()).append(' ').append(cubicCurveTo.getX()).append(',').append(cubicCurveTo.getY());
        } else if (pathElement instanceof ClosePath)
            sb.append('Z');
    }

    @Override
    public void updateLocalToParentTransforms(List<Transform> localToParentTransforms) {
        // Before transformation, the top left corner of the container refers to the axis origin (0,0) in SVG whereas it
        // refers to the top left corner of the path in JavaFx. So to emulate the same behaviour as JavaFx, we need to
        // add a translation of the path so it appears on the top left corner.
        List<Transform> svgTransforms = new ArrayList<>(localToParentTransforms.size() + 1);
        SVGRect bBox = ((SVGPathElement) getElement()).getBBox();
        svgTransforms.add(new Translate(-bBox.x, -bBox.y));
        svgTransforms.addAll(localToParentTransforms);
        super.updateLocalToParentTransforms(svgTransforms);
    }

}
