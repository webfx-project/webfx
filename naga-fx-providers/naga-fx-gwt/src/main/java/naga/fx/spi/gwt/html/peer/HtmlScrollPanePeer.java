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
        callPerfectScrollbarInitialize(element, node.getHbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER, node.getvbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER);
        HtmlUtil.onNodeInsertedIntoDocument(element, this::scheduleUpdate);
    }

    private double scrollTop, scrollLeft;
    private boolean syncing;

    private void setScrollTop(double scrollTop) {
        this.scrollTop = scrollTop;
        syncModelFromUi(false, true);
    }

    private void setScrollLeft(double scrollLeft) {
        this.scrollLeft = scrollLeft;
        syncModelFromUi(true, false);
    }

    private void syncModelFromUi(boolean horizontal, boolean vertical) {
        syncing = true;
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
            }
            if (vertical) {
                double vmin = node.getVmin();
                double vvalue = vmin;
                double vmax = node.getVmax();
                double contentHeight = contentLayoutBounds.getHeight();
                if (contentHeight > viewportHeight)
                    vvalue += scrollTop * (vmax - vmin) / (contentHeight - viewportHeight);
                node.setVvalue(vvalue);
            }
        }
        syncing = false;
    }

    private void syncUiFromModel(boolean horizontal, boolean vertical) {
        syncing = true;
        N node = getNode();
        Node content = node.getContent();
        if (content != null) {
            Bounds contentLayoutBounds = content.getLayoutBounds();
            if (horizontal) {
                double hmin = node.getHmin();
                double hvalue = node.getHvalue();
                double contentWidth = contentLayoutBounds.getWidth();
                double viewportWidth = node.getWidth();
                if (contentWidth > viewportWidth)
                    scrollLeft = (contentWidth - viewportWidth) * (hvalue - hmin) / (node.getHmax() - hmin);
                else
                    scrollLeft = 0;
            }
            if (vertical) {
                double vmin = node.getVmin();
                double vvalue = node.getVvalue();
                double contentHeight = contentLayoutBounds.getHeight();
                double viewportHeight = node.getHeight();
                if (contentHeight > viewportHeight)
                    scrollTop = (contentHeight - viewportHeight) * (vvalue - vmin) / (node.getVmax() - vmin);
                else
                    scrollTop = 0;
            }
        }
        syncing = false;
    }

    private native void callPerfectScrollbarInitialize(Element element, boolean suppressScrollX, boolean suppressScrollY) /*-{
        $wnd.Ps.initialize(element, {suppressScrollX: suppressScrollX, suppressScrollY: suppressScrollY});
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

    @Override
    public void updateHbarPolicy(ScrollPane.ScrollBarPolicy hbarPolicy) {
    }

    @Override
    public void updateVbarPolicy(ScrollPane.ScrollBarPolicy vbarPolicy) {
    }

    @Override
    public void updateHmin(Number hmin) {
    }

    @Override
    public void updateHvalue(Number hValue) {
        if (!syncing) {
            syncUiFromModel(true, false);
            scheduleUpdate();
        }
    }

    @Override
    public void updateHmax(Number hmax) {
    }

    @Override
    public void updateVmin(Number vmin) {
    }

    @Override
    public void updateVvalue(Number vValue) {
        if (!syncing) {
            syncUiFromModel(false, true);
            scheduleUpdate();
        }
    }

    @Override
    public void updateVmax(Number vmax) {
    }

}
