package dev.webfx.kit.mapper.peers.javafxcontrols.elemental2.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.perfectscrollbar.elemental2.PerfectScrollbar;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.dom.AddEventListenerOptions;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * @author Bruno Salmon
 */
public final class HtmlScrollPanePeer
    <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

    extends HtmlRegionPeer<N, NB, NM>
    implements ScrollPanePeerMixin<N, NB, NM> {

    private static final boolean USE_CSS = OperatingSystem.isMobile(); // much smoother experience on mobiles but requires passive mode
    private static final boolean USE_PERFECT_SCROLLBAR = !USE_CSS; // the perfect scrollbars looks better on desktops than in CSS mode

    private double scrollTop, scrollLeft;
    private boolean syncing;

    public HtmlScrollPanePeer() {
        this((NB) new ScrollPanePeerBase(), HtmlUtil.createElement("fx-scrollpane"));
    }

    public HtmlScrollPanePeer(NB base, HTMLElement element) {
        super(base, element);
    }

    @Override
    public void bind(N node, SceneRequester sceneRequester) {
        super.bind(node, sceneRequester);
        HTMLElement element = getElement();
        if (USE_PERFECT_SCROLLBAR) { // better on desktops
            // Important: perfect scrollbar expect a standard HTML container (doesn't work with fx-scrollpane element)
            HTMLElement psContainer = HtmlUtil.setStyle(HtmlUtil.createDivElement(), "position: absolute; width: 100%; height: 100%");
            setChildrenContainer(psContainer);
            HtmlUtil.setChildren(element, psContainer);
        } else { // CSS mode which relies on the overflow-x & overflow-y values, better on mobiles (much smoother)
            AddEventListenerOptions passiveOption = AddEventListenerOptions.create();
            passiveOption.setPassive(true);
            // We intercept the JS scroll events to update the ScrollPane position in JavaFX when the HTML one changes
            element.addEventListener("scroll", e -> {
                if (element.scrollTop != scrollTop) {
                    // If the vertical scroll is disabled, the browser might still do some automatic scroll if the
                    // content is higher than the viewport, but this is not the JavaFX behavior, so we prevent this:
                    if (node.getvbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER)
                        element.scrollTop = scrollTop; // reverting the vertical scroll made by the browser
                    else // otherwise (general case), the scroll was initiated by the user and we consider it
                        setScrollTop(element.scrollTop); // this will update the vertical position in the JavaFX model
                }
                if (element.scrollLeft != scrollLeft) {
                    // If the horizontal scroll is disabled, the browser might still do some automatic scroll if the
                    // content is wider than the viewport, but this is not the JavaFX behavior, so we prevent this:
                    if (node.getHbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER)
                        element.scrollLeft = scrollLeft; // reverting the horizontal scroll made by the browser
                    else // otherwise (general case), the scroll was initiated by the user and we consider it
                        setScrollLeft(element.scrollLeft); // this will update the horizontal position in the JavaFX model
                }
            }, passiveOption);
        }
        node.setOnChildrenLayout(HtmlScrollPanePeer.this::scheduleUiUpdate);
        // The following listener is to reestablish the scroll position on scene change. For ex when the user 1) switches
        // to another page through UI routing and then 2) comes back, this node is removed from the scene graph at 1) =>
        // scene = null until 2) => scene reestablished, but Perfect scrollbar lost its state when removed from the DOM.
        // This listener will trigger a schedule update at 2) which will restore the perfect scrollbar state (scrollTop
        // & scrollLeft will be reapplied).
        FXProperties.runOnPropertiesChange(this::scheduleUiUpdate,
            node.sceneProperty(), // on scene change (removing from or reinserting to the DOM)
            node.parentProperty(), // also on parent change (moving inside the DOM)
            node.maxHeightProperty() // also on maxHeight change to make it work with WebFX Extras TransitionPane
            // otherwise the leaving ScrollPane is reset to the top while animating the transition.
        );
    }

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
        // We set the viewport bounds as follows: the width and height are exactly the same as the scroll pane because
        // the bars of PerfectScrollbar.js are an overlay on top of the content - which is nicer than the OpenJFX
        // ScrollPane by the way. For minX and minY, JavaFX doesn't specify the meaning of these values; they depend on
        // the skin implementation, so here we set them to scrollLeft and ScrollTop. ScrollPane will use these values
        // in its implementation of the IScrollPane WebFX interface used by Node for the localToParent() and
        // parentToLocal() coordinates conversion.
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
        scheduleUiUpdate();
        syncing = false;
    }

    private boolean pending, psInitialized;
    private void scheduleUiUpdate() {
        if (!pending) {
            pending = true;
            UiScheduler.scheduleDeferred(() -> {
                if (USE_PERFECT_SCROLLBAR) {
                    Element psContainer = getChildrenContainer();
                    if (!psInitialized) {
                        N node = getNode();
                        callPerfectScrollbarInitialize(psContainer, node.getHbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER, node.getvbarPolicy() == ScrollPane.ScrollBarPolicy.NEVER);
                        psInitialized = true;
                    }
                    callPerfectScrollbarUpdate(psContainer);
                } else {
                    HTMLElement element = getElement();
                    element.scrollTop = scrollTop;
                    element.scrollLeft = scrollLeft;
                }
                pending = false;
            });
        }
    }

    private void callPerfectScrollbarInitialize(Element psContainer, boolean suppressScrollX, boolean suppressScrollY) {
        PerfectScrollbar ps = new PerfectScrollbar(psContainer, JsPropertyMap.of("suppressScrollX", suppressScrollX, "suppressScrollY", suppressScrollY));
        Js.asPropertyMap(psContainer).set("ps", ps);
        psContainer.addEventListener("ps-scroll-x", e -> setScrollLeft(psContainer.scrollLeft));
        psContainer.addEventListener("ps-scroll-y", e -> setScrollTop(psContainer.scrollTop));
    }

    private void callPerfectScrollbarUpdate(Element psContainer) {
        psContainer.scrollLeft = scrollLeft;
        psContainer.scrollTop = scrollTop;
        PerfectScrollbar ps = Js.cast(Js.asPropertyMap(psContainer).get("ps"));
        ps.update();
    }

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

    private String scrollbarPolicyToOverflow(ScrollPane.ScrollBarPolicy scrollBarPolicy) {
        if (scrollBarPolicy != null) {
            switch (scrollBarPolicy) {
                case ALWAYS:
                    return "scroll";
                case AS_NEEDED:
                    return "auto";
                case NEVER:
                    return "hidden";
            }
        }
        return "hidden";
    }

    @Override
    public void updateHbarPolicy(ScrollPane.ScrollBarPolicy hbarPolicy) {
        if (USE_CSS) {
            setElementStyleAttribute("overflow-x", scrollbarPolicyToOverflow(hbarPolicy));
        }
        // Note: with PerfectScrollbar, This value is considered during initialisation only
    }

    @Override
    public void updateVbarPolicy(ScrollPane.ScrollBarPolicy vbarPolicy) {
        if (USE_CSS) {
            setElementStyleAttribute("overflow-y", scrollbarPolicyToOverflow(vbarPolicy));
        }
        // Note: with PerfectScrollbar, This value is considered during initialisation only
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
