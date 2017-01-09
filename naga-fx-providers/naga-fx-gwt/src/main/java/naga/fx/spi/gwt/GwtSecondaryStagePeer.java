package naga.fx.spi.gwt;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLElement;
import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.stage.Stage;
import naga.fx.stage.Window;
import naga.fx.sun.tk.TKStageListener;
import naga.platform.spi.Platform;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
class GwtSecondaryStagePeer implements StagePeer {

    private final Stage stage;
    private TKStageListener listener;
    private final HTMLElement container = HtmlUtil.createDivElement();
    private final CSSStyleDeclaration style = container.style;

    GwtSecondaryStagePeer(Stage stage) {
        this.stage = stage;
        style.position = "absolute";
        style.overflow = "hidden";
        style.border = "orange 3px solid";
        style.visibility = "hidden";
        window.document.body.appendChild(container);
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        Platform.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet)
            style.left = x + "px";
        if (ySet)
            style.top = y + "px";
        if (w < 0 && cw > 0)
            w = cw + 6;
        if (h < 0 && ch > 0)
            h = ch + 6;
        if (w > 0)
            style.width = w + "px";
        if (h > 0)
            style.height = h + "px";
        changedWindowSize();
    }

    private void changedWindowSize() {
        float clientWidth = (float) container.clientWidth;
        float clientHeight = (float) container.clientHeight;
        if (listener != null)
            listener.changedSize(clientWidth, clientHeight);
        ((HtmlScenePeer) stage.getScene().impl_getPeer()).changedWindowSize(clientWidth, clientHeight);
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public Window getWindow() {
        return stage;
    }

    private boolean visible;
    @Override
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            if (visible)
                style.visibility = null;
            else
                window.document.body.removeChild(container);
            this.visible = visible;
        }
    }

    @Override
    public void onSceneRootChanged() {
        setWindowContent(((HtmlScenePeer) stage.getScene().impl_getPeer()).getSceneNode());
    }

    private void setWindowContent(elemental2.Node content) {
        HtmlUtil.setChild(container, content);
    }

}
