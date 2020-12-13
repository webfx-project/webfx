package dev.webfx.kit.mapper.peers.javafxcontrols.gwt.html;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import dev.webfx.platform.client.services.uischeduler.UiScheduler;

/**
 * @author Bruno Salmon
 */
public final class HtmlScrollPanePeer
        <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements ScrollPanePeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlScrollPanePeer() {
        this((NB) new ScrollPanePeerBase(), HtmlUtil.createElement("fx-scrollpane"));
    }

    public HtmlScrollPanePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        // Important: perfect scrollbar expect a standard HTML container (won't work with fx-scrollpane)
        HTMLElement psContainer = HtmlUtil.setStyle(HtmlUtil.createDivElement(), "position: absolute; width: 100%; height: 100%");
        setChildrenContainer(psContainer);
        HtmlUtil.setChildren(getElement(), psContainer);
        node.setOnChildrenLayout(HtmlScrollPanePeer.this::scheduleUpdate);
    }

    private double scrollTop, scrollLeft;
    private boolean syncing;

    private void setScrollTop(double scrollTop) {
        this.scrollTop = scrollTop;
        vSyncModelFromUi();
    }

    private void setScrollLeft(double scrollLeft) {
        this.scrollLeft = scrollLeft;
        hSyncModelFromUi();
    }

    private void hSyncModelFromUi() {
        if (!syncing)
            syncModelFromUi(true, false);
    }

    private void vSyncModelFromUi() {
        if (!syncing)
            syncModelFromUi(false, true);
    }

    private void syncModelFromUi(boolean horizontal, boolean vertical) {
        syncing = true;
        N scrollPane = getNode();
        double viewportWidth = scrollPane.getWidth();
        double viewportHeight = scrollPane.getHeight();
        Bounds viewportBounds = new BoundingBox(scrollLeft, scrollTop, viewportWidth, viewportHeight);
        scrollPane.setViewportBounds(viewportBounds);
        Node content = scrollPane.getContent();
        if (content != null) {
            Bounds contentLayoutBounds = content.getLayoutBounds();
            if (horizontal) {
                double hmin = scrollPane.getHmin();
                double hvalue = hmin;
                double contentWidth = contentLayoutBounds.getWidth();
                if (contentWidth > viewportWidth)
                    hvalue += scrollLeft * (scrollPane.getHmax() - hmin) / (contentWidth - viewportWidth);
                scrollPane.setHvalue(hvalue);
            }
            if (vertical) {
                double vmin = scrollPane.getVmin();
                double vvalue = vmin;
                double vmax = scrollPane.getVmax();
                double contentHeight = contentLayoutBounds.getHeight();
                if (contentHeight > viewportHeight)
                    vvalue += scrollTop * (vmax - vmin) / (contentHeight - viewportHeight);
                scrollPane.setVvalue(vvalue);
            }
        }
        syncing = false;
    }

    private void hSyncUiFromModel() {
        if (!syncing)
            syncUiFromModel(true, false);
    }

    private void vSyncUiFromModel() {
        if (!syncing)
            syncUiFromModel(false, true);
    }

    private void syncUiFromModel(boolean horizontal, boolean vertical) {
        syncing = true;
        N scrollPane = getNode();
        Node content = scrollPane.getContent();
        if (content != null) {
            Bounds contentLayoutBounds = content.getLayoutBounds();
            if (horizontal) {
                double hmin = scrollPane.getHmin();
                double hvalue = scrollPane.getHvalue();
                double contentWidth = contentLayoutBounds.getWidth();
                double viewportWidth = scrollPane.getWidth();
                if (contentWidth > viewportWidth)
                    scrollLeft = (contentWidth - viewportWidth) * (hvalue - hmin) / (scrollPane.getHmax() - hmin);
                else
                    scrollLeft = 0;
            }
            if (vertical) {
                double vmin = scrollPane.getVmin();
                double vvalue = scrollPane.getVvalue();
                double contentHeight = contentLayoutBounds.getHeight();
                double viewportHeight = scrollPane.getHeight();
                if (contentHeight > viewportHeight)
                    scrollTop = (contentHeight - viewportHeight) * (vvalue - vmin) / (scrollPane.getVmax() - vmin);
                else
                    scrollTop = 0;
            }
        }
        scheduleUpdate();
        syncing = false;
    }

    private boolean pending, psInitialized;
    private void scheduleUpdate() {
        if (!pending) {
            pending = true;
            UiScheduler.scheduleDeferred(() -> {
                Element psContainer = getChildrenContainer();
                if (!psInitialized) {
                    N node = getNode();
                    callPerfectScrollbarInitialize(psContainer, node.getHbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER, node.getvbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER);
                    psInitialized = true;
                }
                callPerfectScrollbarUpdate(psContainer);
                pending = false;
            });
        }
    }

    private native void callPerfectScrollbarInitialize(Element psContainer, boolean suppressScrollX, boolean suppressScrollY) /*-{
        psContainer.ps = new $wnd.PerfectScrollbar(psContainer, {suppressScrollX: suppressScrollX, suppressScrollY: suppressScrollY});
        var self = this;
        psContainer.addEventListener('ps-scroll-x', function() { self.@HtmlScrollPanePeer::setScrollLeft(D)(psContainer.scrollLeft)});
        psContainer.addEventListener('ps-scroll-y', function() { self.@HtmlScrollPanePeer::setScrollTop(D)(psContainer.scrollTop)});
    }-*/;

    private native void callPerfectScrollbarUpdate(Element psContainer) /*-{
        psContainer.scrollLeft = this.@HtmlScrollPanePeer::scrollLeft;
        psContainer.scrollTop = this.@HtmlScrollPanePeer::scrollTop;
        psContainer.ps.update();
    }-*/;

    @Override
    public void updateWidth(Number width) {
        super.updateWidth(width);
        hSyncUiFromModel();
    }

    @Override
    public void updateHeight(Number height) {
        super.updateHeight(height);
        vSyncUiFromModel();
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
        hSyncUiFromModel();
    }

    @Override
    public void updateHmax(Number hmax) {
    }

    @Override
    public void updateVmin(Number vmin) {
    }

    @Override
    public void updateVvalue(Number vValue) {
        vSyncUiFromModel();
    }

    @Override
    public void updateVmax(Number vmax) {
    }

}
