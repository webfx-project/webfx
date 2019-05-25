package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html;

import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLElement;
import javafx.scene.canvas.Canvas;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.CanvasPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base.CanvasPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlCanvasPeer
        <N extends Canvas, NB extends CanvasPeerBase<N, NB, NM>, NM extends CanvasPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements CanvasPeerMixin<N, NB, NM> {

    public HtmlCanvasPeer() {
        this((NB) new CanvasPeerBase(), HtmlUtil.createElement("canvas"));
    }

    public HtmlCanvasPeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void updateWidth(Number width) {
        ((HTMLCanvasElement) getElement()).width = width.intValue();
    }

    @Override
    public void updateHeight(Number height) {
        ((HTMLCanvasElement) getElement()).height = height.intValue();
    }

}
