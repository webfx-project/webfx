package naga.fx.spi.gwt;

import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import emul.com.sun.javafx.tk.TKStageListener;
import naga.platform.spi.Platform;

import static elemental2.Global.document;
import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
class GwtPrimaryStagePeer implements StagePeer {

    private final Stage stage;
    private TKStageListener listener;

    GwtPrimaryStagePeer(Stage stage) {
        this.stage = stage;
        document.body.style.overflow = "hidden";
        window.onresize = a -> {
            changedWindowSize();
            return null;
        };
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
        changedWindowSize();
        listener.changedLocation(0, 0);
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        Platform.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        changedWindowSize();
    }

    private void changedWindowSize() {
        float innerWidth = (float) window.innerWidth;
        float innerHeight = (float) window.innerHeight;
        if (listener != null)
            listener.changedSize(innerWidth, innerHeight);
        ((HtmlScenePeer) stage.getScene().impl_getPeer()).changedWindowSize(innerWidth, innerHeight);
    }

    @Override
    public void setTitle(String title) {
        document.title = title;
    }

    @Override
    public Window getWindow() {
        return stage;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public void onSceneRootChanged() {
        setWindowContent(((HtmlScenePeer) stage.getScene().impl_getPeer()).getSceneNode());
    }

    private void setWindowContent(elemental2.Node content) {
        HtmlUtil.setBodyContent(content);
    }

}
