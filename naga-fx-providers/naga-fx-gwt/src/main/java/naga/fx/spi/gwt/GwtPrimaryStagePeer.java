package naga.fx.spi.gwt;

import emul.com.sun.javafx.tk.TKStageListener;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.StagePeer;

import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.window;


/**
 * @author Bruno Salmon
 */
class GwtPrimaryStagePeer implements StagePeer {

    private final Stage stage;
    private TKStageListener listener;

    GwtPrimaryStagePeer(Stage stage) {
        this.stage = stage;
        // Disabling horizontal scroll bar (but allowing vertical scroll bar)
        HtmlUtil.setStyleAttribute(document.documentElement, "overflow-x", "hidden");
        // Checking the window size on each pulse (window.onsize is not enough because it doesn't detect vertical scroll bar apparition)
        Toolkit.get().scheduler().schedulePeriodicAnimationFrame(this::changedWindowSize, false);
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
        listener.changedLocation(0, 0);
        lastWidth = lastHeight = 0; // to force listener call in changedWindowSize()
        Toolkit.get().scheduler().requestNextPulse(); // to ensure changedWindowSize() will be called very soon
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Platform.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        changedWindowSize();
    }

    private float lastWidth, lastHeight;

    private void changedWindowSize() {
        float width = (float) document.documentElement.clientWidth; //window.innerWidth;
        float height = (float) window.innerHeight - 1; // -1 is to avoid the window vertical scroll bar in EditLetterViewActivity
        if (width == lastWidth && height == lastHeight)
            return;
        if (listener != null)
            listener.changedSize(width, height);
        ((HtmlScenePeer) stage.getScene().impl_getPeer()).changedWindowSize(width, height);
        lastWidth = width;
        lastHeight = height;
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

    private void setWindowContent(elemental2.dom.Node content) {
        HtmlUtil.setBodyContent(content);
    }

}
