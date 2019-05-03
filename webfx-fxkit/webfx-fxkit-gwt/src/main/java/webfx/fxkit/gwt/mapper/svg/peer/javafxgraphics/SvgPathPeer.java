package webfx.fxkit.gwt.mapper.svg.peer.javafxgraphics;

import elemental2.dom.Element;
import javafx.collections.ListChangeListener;
import javafx.scene.shape.*;
import webfx.fxkit.gwt.mapper.util.SvgUtil;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerBase;
import webfx.fxkit.mapper.spi.impl.peer.javafxgraphics.PathPeerMixin;
import webfx.platform.shared.util.collection.Collections;

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
}
