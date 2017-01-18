package naga.fx.spi.gwt;

import elemental2.CSSStyleDeclaration;
import elemental2.HTMLBodyElement;
import elemental2.HTMLElement;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.StagePeer;
import emul.javafx.stage.Modality;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import emul.com.sun.javafx.tk.TKStageListener;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
class GwtSecondaryStagePeer implements StagePeer {

    private final Stage stage;
    private TKStageListener listener;
    private HTMLElement modalBackgroundDiv;
    private final HTMLElement stageDiv = HtmlUtil.createDivElement();
    private final CSSStyleDeclaration stageDivStyle = stageDiv.style;

    GwtSecondaryStagePeer(Stage stage) {
        this.stage = stage;
        stageDivStyle.position = "absolute";
        stageDivStyle.overflow = "hidden";
        stageDivStyle.border = "orange 3px solid";
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Platform.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet)
            stageDivStyle.left = x + "px";
        if (ySet)
            stageDivStyle.top = y + "px";
        if (w < 0 && cw > 0)
            w = cw + 6;
        if (h < 0 && ch > 0)
            h = ch + 6;
        if (w > 0)
            stageDivStyle.width = w + "px";
        if (h > 0)
            stageDivStyle.height = h + "px";
        changedWindowSize();
    }

    private void changedWindowSize() {
        float clientWidth = (float) stageDiv.clientWidth;
        float clientHeight = (float) stageDiv.clientHeight;
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
            HTMLBodyElement body = window.document.body;
            if (visible) {
                if (stage.getModality() != Modality.NONE) {
                    modalBackgroundDiv = HtmlUtil.createDivElement();
                    CSSStyleDeclaration style = modalBackgroundDiv.style;
                    style.position = "absolute";
                    style.top = style.bottom = style.left = style.right = "0";
                    style.background = "black";
                    style.opacity = 0.5;
                    body.appendChild(modalBackgroundDiv);
                }
                body.appendChild(stageDiv);
            } else {
                body.removeChild(stageDiv);
                if (modalBackgroundDiv != null)
                    body.removeChild(modalBackgroundDiv);
            }
            this.visible = visible;
        }
    }

    @Override
    public void onSceneRootChanged() {
        setWindowContent(((HtmlScenePeer) stage.getScene().impl_getPeer()).getSceneNode());
    }

    private void setWindowContent(elemental2.Node content) {
        HtmlUtil.setChild(stageDiv, content);
    }

}
