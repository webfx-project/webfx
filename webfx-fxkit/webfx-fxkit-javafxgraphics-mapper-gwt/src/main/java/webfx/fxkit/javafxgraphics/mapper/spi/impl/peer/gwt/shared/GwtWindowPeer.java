package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLElement;
import javafx.scene.Scene;
import javafx.stage.Window;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl.ScenePeerBase;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl.WindowPeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlScenePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.util.HtmlUtil;

import static elemental2.dom.DomGlobal.document;

public class GwtWindowPeer extends WindowPeerBase {

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
        document.body.appendChild(windowElement); // Adding to the DOM
    }

    @Override
    protected ScenePeerBase getScenePeer() {
        Scene scene = getWindow().getScene();
        return scene == null ? null : (ScenePeerBase) scene.impl_getPeer();
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Logger.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet)
            windowStyle.left = x + "px";
        if (ySet)
            windowStyle.top = y + "px";
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
