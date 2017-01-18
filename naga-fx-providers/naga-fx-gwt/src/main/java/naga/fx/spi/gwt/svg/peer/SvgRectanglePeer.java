package naga.fx.spi.gwt.svg.peer;

import elemental2.Element;
import naga.fx.spi.gwt.util.SvgUtil;
import emul.javafx.scene.shape.Rectangle;
import naga.fx.spi.peer.base.RectanglePeerMixin;
import naga.fx.spi.peer.base.RectanglePeerBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectanglePeer
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends SvgShapePeer<N, NB, NM>
        implements RectanglePeerMixin<N, NB, NM> {

    public SvgRectanglePeer() {
        this((NB) new RectanglePeerBase(), SvgUtil.createSvgRectangle());
    }

    public SvgRectanglePeer(NB base, Element element) {
        super(base, element);
    }

    @Override
    public void updateX(Double x) {
        setElementAttribute("x", x, 0d);
    }

    @Override
    public void updateY(Double y) {
        setElementAttribute("y", y, 0d);
    }

    @Override
    public void updateWidth(Double width) {
        setElementAttribute("width", width);
    }

    @Override
    public void updateHeight(Double height) {
        setElementAttribute("height", height);
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        setElementAttribute("rx", arcWidth == null ? null : arcWidth / 2, 0d);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        setElementAttribute("ry", arcHeight == null ? null : arcHeight / 2, 0d);
    }
}
