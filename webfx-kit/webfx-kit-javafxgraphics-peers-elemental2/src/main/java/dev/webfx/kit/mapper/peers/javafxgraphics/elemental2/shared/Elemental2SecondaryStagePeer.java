package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared;

import elemental2.dom.CSSProperties;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.StagePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public final class Elemental2SecondaryStagePeer extends StagePeerBase {

    private HTMLElement modalBackgroundDiv;
    private final HTMLElement stageDiv = HtmlUtil.createDivElement();
    private final CSSStyleDeclaration stageDivStyle = stageDiv.style;

    public Elemental2SecondaryStagePeer(Stage stage) {
        super(stage);
        stageDivStyle.position = "absolute";
        stageDivStyle.overflow = "hidden";
        stageDivStyle.border = "orange 3px solid";
    }

    @Override
    protected ScenePeerBase getScenePeer() {
        Scene scene = getWindow().getScene();
        return scene == null ? null : (ScenePeerBase) scene.impl_getPeer();
    }

    @Override
    protected double getPeerWindowWidth() {
        return stageDiv.clientWidth;
    }

    @Override
    protected double getPeerWindowHeight() {
        return stageDiv.clientHeight;
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //Logger.log("x = " + x + ", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet)
            stageDivStyle.left = x + "px";
        if (ySet)
            stageDivStyle.top = y + "px";
        if (w < 0 && cw > 0)
            w = cw + 6;
        if (h < 0 && ch > 0)
            h = ch + 6;
        if (w > 0)
            stageDivStyle.width = CSSProperties.WidthUnionType.of(w + "px");
        if (h > 0)
            stageDivStyle.height = CSSProperties.HeightUnionType.of(h + "px");
        changedWindowSize();
    }

    @Override
    public void setTitle(String title) {
    }

    private boolean visible;
    @Override
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            HTMLBodyElement body = document.body;
            if (visible) {
                if (getStage().getModality() != Modality.NONE) {
                    modalBackgroundDiv = HtmlUtil.createDivElement();
                    CSSStyleDeclaration style = modalBackgroundDiv.style;
                    style.position = "absolute";
                    style.top = style.bottom = style.left = style.right = "0";
                    style.background = "black";
                    style.opacity = CSSProperties.OpacityUnionType.of(0.5);
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
        setWindowContent(((HtmlScenePeer) getWindow().getScene().impl_getPeer()).getSceneHtmlElement());
    }

    private void setWindowContent(elemental2.dom.Node content) {
        HtmlUtil.setChild(stageDiv, content);
    }

}
