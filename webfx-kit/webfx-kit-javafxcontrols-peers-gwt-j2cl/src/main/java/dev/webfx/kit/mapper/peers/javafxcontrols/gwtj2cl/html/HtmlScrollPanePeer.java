package dev.webfx.kit.mapper.peers.javafxcontrols.gwtj2cl.html;

import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerBase;
import dev.webfx.kit.mapper.peers.javafxcontrols.base.ScrollPanePeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.SceneRequester;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlRegionPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.dom.AddEventListenerOptions;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.TouchEvent;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.function.Consumer;

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
    private boolean cssScrollDetected;

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
        if (USE_PERFECT_SCROLLBAR) {
            // Important: perfect scrollbar expect a standard HTML container (won't work with fx-scrollpane)
            HTMLElement psContainer = HtmlUtil.setStyle(HtmlUtil.createDivElement(), "position: absolute; width: 100%; height: 100%");
            setChildrenContainer(psContainer);
            HtmlUtil.setChildren(element, psContainer);
        } else { // CSS mode which relies on the overflow-x & overflow-y values
            // Important: the CSS mode will react to touch events only if the Scene touch events thar are passed to
            // JavaFX originates from an event listener in passive mode, which is not the case by default with webfx,
            // because the passive mode doesn't let us stop the propagation which is a feature that the application code
            // requests when calling event.consume(). But it's very likely ok to disable this feature during a touch
            // scroll to enjoy the smoothness of the scroll in passive mode on mobiles.
            node.addEventHandler(TouchEvent.ANY, e -> {
                // The purpose of this handler is to detect any touch event that targets the scroll pane while still in
                // standard mode (non-passive). By chance, this detection happens just before it is passed to the scene,
                // and it's the only and last opportunity to activate the passive mode. Without catching this event
                // and switching to passive mode, the CSS overflow scrollbars wouldn't react to the touch events.
                if (!HtmlSvgNodePeer.isScrolling()) {
                    HtmlSvgNodePeer.setScrolling(true);
                    // It's also important to detect the end of this touch scroll to go back to the standard mode, so we
                    // set up a periodic timer for that.
                    UiScheduler.schedulePeriodicInAnimationFrame(100, new Consumer<>() {
                        private long lastMillis = System.currentTimeMillis();
                        private double lastScrollTop = element.scrollTop, lastScrollLeft = element.scrollLeft;
                        private double scrollTopInertia, scrollLeftInertia;
                        @Override
                        public void accept(Scheduled scheduled) {
                            // We check if the scroll has become stationary since last call
                            double deltaTop  = element.scrollTop  - lastScrollTop;
                            double deltaLeft = element.scrollLeft - lastScrollLeft;
                            boolean stationary = deltaTop == 0 && deltaLeft == 0 && !cssScrollDetected; // also considering any scroll event
                            // Note:
                            long nowMillis = System.currentTimeMillis();
                            long delayMillis = nowMillis - lastMillis;
                            // Skipping suspicious false stop detection for 2s
                            if (stationary && delayMillis < 2000 && (Math.abs(scrollTopInertia) > 0.05 || Math.abs(scrollLeftInertia) > 0.05)) {
                                return;
                            }
                            scrollTopInertia = deltaTop / delayMillis;
                            scrollLeftInertia = deltaLeft / delayMillis;
                            // if since the last period no scroll events have been generated and the element looks stationary
                            if (stationary) {
                                // we consider it's the end of the touch scroll and go back to the standard mode
                                HtmlSvgNodePeer.setScrolling(false);
                                scheduled.cancel(); // we can stop this periodic check
                            } else {
                                lastScrollLeft = element.scrollLeft;
                                lastScrollTop = element.scrollTop;
                                lastMillis = nowMillis;
                                cssScrollDetected = false;
                            }
                        }
                    });
                }
            });
            AddEventListenerOptions passiveOption = AddEventListenerOptions.create();
            passiveOption.setPassive(true);
            // We intercept the JS scroll events to update the ScrollPane position in JavaFX when the html one changes
            element.addEventListener("scroll", e -> {
                if (element.scrollTop != scrollTop)
                    setScrollTop(element.scrollTop);
                if (element.scrollLeft != scrollLeft)
                    setScrollLeft(element.scrollLeft);
                // Also if this happens during a touch scroll, we report the detection of the scroll and reschedule the
                // css scroll end detector.
                cssScrollDetected = true;
            }, passiveOption);
        }
        node.setOnChildrenLayout(HtmlScrollPanePeer.this::scheduleUiUpdate);
        // The following listener is to reestablish the scroll position on scene change. For ex when the user 1) switches
        // to another page through UI routing and then 2) come back, this node is removed from the scene graph at 1) =>
        // scene = null until 2) => scene reestablished, but Perfect scrollbar lost its state when removed from the DOM.
        // This listener will trigger a schedule update at 2) which will restore the perfect scrollbar state (scrollTop
        // & scrollLeft will be reapplied).
        FXProperties.runOnPropertiesChange(this::scheduleUiUpdate, node.sceneProperty());
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
