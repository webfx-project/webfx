package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import elemental2.dom.Element;
import elemental2.svg.SVGPathElement;
import elemental2.svg.SVGRect;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.*;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.PathPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import dev.webfx.platform.shared.util.collection.Collections;

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

    public void updatePath(String path) {
        setElementAttribute("d", path);
    }

    @Override
    public void updateElements(List<PathElement> elements, ListChangeListener.Change<PathElement> change) {
        updatePath(toSvgPathData(elements));
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
    public SVGRect getBBox() {
        return ((SVGPathElement) getElement()).getBBox();
    }

}
