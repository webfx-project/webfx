package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared;

import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.WindowPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import javafx.scene.Scene;
import javafx.stage.Window;

import static elemental2.dom.DomGlobal.document;

public class GwtWindowPeer extends WindowPeerBase {

    // Variable set by HtmlScenePeer that contains the correction to apply on window.screenY, because browsers return
    // a wrong value! What WebFX expects from window.screenY is to return the position on the screen of the top left
    // corner of the browser PAGE (good). However, browsers (at least on macOS) return the position of the top left
    // corner of the browser WINDOW (bad). There can be a quite big difference between the 2, due to the presence of
    // browser tabs, bookmarks bar, etc...
    public static double windowScreenYCorrection;
    // We actually don't create a separate window like in JavaFX, but simply simulate a window in the DOM
    private final HTMLElement windowElement = HtmlUtil.createElement("fx-window");
    private final CSSStyleDeclaration windowStyle = windowElement.style;

    public GwtWindowPeer(Window window) {
        super(window);
        windowStyle.position = "absolute";
        windowStyle.overflow = "hidden";
        windowStyle.boxShadow = "0px 8px 12px rgba(0, 0, 0, 0.16), 0 4px 8px rgba(0, 0, 0, 0.23)";
        //windowStyle.border = "orange 3px solid";
        // HACK: the window won't resize properly if not in the DOM before showing (otherwise root layout bound will be 0)
        windowStyle.opacity = CSSProperties.OpacityUnionType.of(0); // Setting opacity to 0 so it's not visible
        // Adding the window to the DOM (it should appear in front of all other elements since it is the last child)
        document.body.appendChild(windowElement);
    }

    @Override
    protected ScenePeerBase getScenePeer() {
        Scene scene = getWindow().getScene();
        return scene == null ? null : (ScenePeerBase) scene.impl_getPeer();
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        // Console.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        // Note: x & y here are screenX and screenY. But because this window is in reality in the DOM, we need to
        // transform these screen coordinates into page coordinates by subtracting the current window screen position.
        if (xSet)
            windowStyle.left = (x - DomGlobal.window.screenX)  + "px";
        if (ySet)
            windowStyle.top = (y - DomGlobal.window.screenY + windowScreenYCorrection) + "px";
        if (w < 0 && cw > 0)
            w = cw; // + 6;
        if (h < 0 && ch > 0)
            h = ch; // + 6;
        if (w > 0)
            windowStyle.width = CSSProperties.WidthUnionType.of(w + "px");
        if (h > 0)
            windowStyle.height = CSSProperties.HeightUnionType.of(h + "px");
    }

    @Override
    public void setTitle(String title) {
    }

    private boolean firstShow = true;
    private boolean visible;
    @Override
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            if (visible) {
                if (firstShow) {
                    windowStyle.opacity = CSSProperties.OpacityUnionType.of(1);
                    firstShow = false;
                } else
                    document.body.appendChild(windowElement);
            } else {
                document.body.removeChild(windowElement);
            }
            this.visible = visible;
        }
    }

    @Override
    public void onSceneRootChanged() {
        setWindowContent(((HtmlScenePeer) getWindow().getScene().impl_getPeer()).getSceneNode());
    }

    private void setWindowContent(elemental2.dom.Node content) {
        HtmlUtil.setChild(windowElement, content);
    }
}
