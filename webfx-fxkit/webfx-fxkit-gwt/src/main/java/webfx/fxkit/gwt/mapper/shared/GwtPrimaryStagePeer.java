package webfx.fxkit.gwt.mapper.shared;

import javafx.scene.Scene;
import javafx.stage.Stage;
import webfx.fxkit.gwt.mapper.html.HtmlScenePeer;
import webfx.fxkit.gwt.mapper.util.HtmlUtil;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.ScenePeerBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.StagePeerBase;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;

import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.window;


/**
 * @author Bruno Salmon
 */
public final class GwtPrimaryStagePeer extends StagePeerBase {

    public GwtPrimaryStagePeer(Stage stage) {
        super(stage);
        // Disabling browser horizontal and vertical scroll bars
        HtmlUtil.setStyleAttribute(document.documentElement, "overflow", "hidden");
        // Removing the default margin around the body so it fills the whole browser tab
        HtmlUtil.setStyleAttribute(document.body, "margin", "0");
        // Checking the window size on each pulse (window.onsize is not enough because it doesn't detect vertical scroll bar apparition)
        changedWindowSize();
        UiScheduler.schedulePeriodicInAnimationFrame(this::changedWindowSize, AnimationFramePass.PROPERTY_CHANGE_PASS);
    }

    @Override
    protected ScenePeerBase getScenePeer() {
        Scene scene = stage.getScene();
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
        setWindowContent(((HtmlScenePeer) stage.getScene().impl_getPeer()).getSceneNode());
    }

    private void setWindowContent(elemental2.dom.Node content) {
        HtmlUtil.setBodyContent(content);
    }

}
