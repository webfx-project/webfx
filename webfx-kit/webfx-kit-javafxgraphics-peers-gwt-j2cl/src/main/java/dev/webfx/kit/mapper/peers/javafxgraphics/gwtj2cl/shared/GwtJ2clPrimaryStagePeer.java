package dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared;

import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.StagePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.util.HtmlUtil;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.uischeduler.UiScheduler;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.window;


/**
 * @author Bruno Salmon
 */
public final class GwtJ2clPrimaryStagePeer extends StagePeerBase {

    // Can be set to true when debugging Scheduler for other purposes than this class (reduces Scheduler sollicitations,
    private static final boolean SKIP_TRACK_WINDOW_POSITION_DEBUG_FLAG = false; // especially in Browser dev tools)

    // Variable that will contain the correction to apply on HTML window.screenY, because browsers return a wrong value!
    // What WebFX expects from window.screenY is to return the position on the screen of the top-left corner of the
    // browser PAGE (good). However, browsers (at least on macOS) return the position of the top-left corner of the
    // browser WINDOW (bad). There can be a quite big difference between the 2, due to the presence of browser tabs,
    // bookmarks bar, etc...
    static double windowScreenYCorrection;

    private Scheduled windowPositionWatcher;

    public GwtJ2clPrimaryStagePeer(Stage stage) {
        super(stage);
        // Workaround for iOS Safari not returning the correct initial window height in PWA with <meta name='apple-mobile-web-app-status-bar-style' content='black-translucent'>
        document.body.style.set("padding-top", "env(safe-area-inset-top)");
        document.body.style.set("padding-bottom", "env(safe-area-inset-bottom)");
        UiScheduler.scheduleDelay(500, () -> {
            document.body.style.set("padding-top", "0");
            document.body.style.set("padding-bottom", "0");
        });

        // Considering the current window size
        changedWindowSize();
        // And subsequent changes in the future
        window.addEventListener("resize", e -> changedWindowSize());
        // Now we are managing the window position, and also the correction on window.screenY. We can't have the correct
        // value of the window position right now, before having computed the window.screenY correction, which requires
        // a mouse event, so we first set a one-time listener that will be called as soon as the user moves the mouse.
        document.addEventListener("mousemove", new EventListener() {
            @Override
            public void handleEvent(Event e) {
                // Now we have a mouse event that we can pass to computeWindowScreenYCorrection()
                computeWindowScreenYCorrection((MouseEvent) e);
                // And therefore we can now set the correct position of the window in JavaFX.
                fireEventIfLocationChanged();
                // One-time listener => we can remove it now.
                document.removeEventListener("mousemove", this);
            }
        });
        // Now we are managing the subsequent window position changes. There is no listener in HTML for that, so we will
        // need to set a periodic timer. However, we can assume that this is necessary only when the mouse is outside
        // the page. So we start that timer only when the mouse leaves the page.
        if (!SKIP_TRACK_WINDOW_POSITION_DEBUG_FLAG) {
            document.addEventListener("mouseleave", e -> {
                if (windowPositionWatcher == null)
                    windowPositionWatcher = UiScheduler.schedulePeriodicInAnimationFrame(this::fireEventIfLocationChanged);
            });
        }
        // And we stop that timer when the mouse enters the page again.
        document.addEventListener("mouseenter", e -> {
            if (windowPositionWatcher != null) {
                windowPositionWatcher.cancel();
                windowPositionWatcher = null;
            }
            // We also compute the window.screenY correction again, just in case the user made a change that affected
            // the window.screenY correction when he was outside the page, such as showing or hiding the bookmark bar.
            computeWindowScreenYCorrection((MouseEvent) e);
            fireEventIfLocationChanged();
        });
    }

    private static void computeWindowScreenYCorrection(MouseEvent me) {
        // We set the correction for window.screenY knowing that me.pageY is giving the correct value in the page
        // coordinates.
        windowScreenYCorrection = me.screenY - window.screenY - me.pageY;
    }

    private void fireEventIfLocationChanged() {
        float screenX = window.screenX; // window.screenX returns the correct value
        float screenY = window.screenY + (float) windowScreenYCorrection; // window.screenY needs the correction
        if (screenX != getWindow().getX() || screenY != getWindow().getY()) { // Has the position changed?
            listener.changedLocation(screenX, screenY); // If yes, we notify JavaFX
        }
    }

    @Override
    protected ScenePeerBase getScenePeer() {
        Scene scene = getWindow().getScene();
        return scene == null ? null : (ScenePeerBase) scene.impl_getPeer();
    }

    @Override
    protected double getPeerWindowWidth() {
        return window.innerWidth;
    }

    @Override
    protected double getPeerWindowHeight() {
        return window.innerHeight;
    }

    @Override
    public void setTitle(String title) {
        document.title = title;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public void onSceneRootChanged() {
        setWindowContent(((HtmlScenePeer) getWindow().getScene().impl_getPeer()).getSceneHtmlElement());
    }

    private void setWindowContent(elemental2.dom.Node content) {
        HtmlUtil.setBodyContent(content);
    }
}
