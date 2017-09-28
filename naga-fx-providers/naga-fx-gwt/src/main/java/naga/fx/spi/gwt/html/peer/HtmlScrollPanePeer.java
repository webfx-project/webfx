package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import emul.javafx.geometry.BoundingBox;
import emul.javafx.geometry.Bounds;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.ScrollPane;
import naga.fx.scene.SceneRequester;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ScrollPanePeerBase;
import naga.fx.spi.peer.base.ScrollPanePeerMixin;

/**
 * @author Bruno Salmon
 */
public final class HtmlScrollPanePeer
        <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ScrollPanePeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlScrollPanePeer() {
        this((NB) new ScrollPanePeerBase(), HtmlUtil.createDivElement());
    }

    public HtmlScrollPanePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        node.setOnChildrenLayout(this::scheduleUpdate);
        HTMLElement element = getElement();
        callPerfectScrollbarInitialize(element);
        HtmlUtil.onNodeInsertedIntoDocument(element, this::scheduleUpdate);
    }

    private double scrollTop, scrollLeft;

    private void setScrollTop(double scrollTop) {
        this.scrollTop = scrollTop;
        syncModelFromUi(false);
    }

    private void setScrollLeft(double scrollLeft) {
        this.scrollLeft = scrollLeft;
        syncModelFromUi(true);
    }

    private void syncModelFromUi(boolean horizontal) {
        N node = getNode();
        double viewportWidth = node.getWidth();
        double viewportHeight = node.getHeight();
        Bounds viewportBounds = new BoundingBox(scrollLeft, scrollTop, viewportWidth, viewportHeight);
        node.setViewportBounds(viewportBounds);
        Node content = node.getContent();
        if (content != null) {
            Bounds contentLayoutBounds = content.getLayoutBounds();
            if (horizontal) {
                double hmin = node.getHmin();
                double hvalue = hmin;
                double contentWidth = contentLayoutBounds.getWidth();
                if (contentWidth > viewportWidth)
                    hvalue += scrollLeft * (node.getHmax() - hmin) / (contentWidth - viewportWidth);
                node.setHvalue(hvalue);
            } else {
                double vmin = node.getVmin();
                double vvalue = vmin;
                double vmax = node.getVmax();
                double contentHeight = contentLayoutBounds.getHeight();
                if (contentHeight > viewportHeight)
                    vvalue += scrollTop * (vmax - vmin) / (contentHeight - viewportHeight);
                node.setVvalue(vvalue);
            }
        }
    }

    private native void callPerfectScrollbarInitialize(Element element) /*-{
        $wnd.Ps.initialize(element);
        var self = this;
        element.addEventListener('ps-scroll-x', function() { self.@naga.fx.spi.gwt.html.peer.HtmlScrollPanePeer::setScrollLeft(D)(element.scrollLeft)});
        element.addEventListener('ps-scroll-y', function() { self.@naga.fx.spi.gwt.html.peer.HtmlScrollPanePeer::setScrollTop(D)(element.scrollTop)});
    }-*/;

    private native void callPerfectScrollbarUpdate(Element element) /*-{
        element.scrollLeft = this.@naga.fx.spi.gwt.html.peer.HtmlScrollPanePeer::scrollLeft;
        element.scrollTop = this.@naga.fx.spi.gwt.html.peer.HtmlScrollPanePeer::scrollTop;
        $wnd.Ps.update(element);
    }-*/;

    private boolean pending;
    private void scheduleUpdate() {
        if (!pending) {
            pending = true;
            Toolkit.get().scheduler().scheduleDeferred(() -> {
                callPerfectScrollbarUpdate(getElement());
                pending = false;
            });
        }
    }

    @Override
    public void updateWidth(Double width) {
        super.updateWidth(width);
        scheduleUpdate();
    }

    @Override
    public void updateHeight(Double height) {
        super.updateHeight(height);
        scheduleUpdate();
    }
}
