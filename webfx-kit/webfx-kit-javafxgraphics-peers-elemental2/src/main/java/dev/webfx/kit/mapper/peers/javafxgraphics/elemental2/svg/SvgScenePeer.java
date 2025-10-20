package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.svg;

import elemental2.dom.DomGlobal;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.SvgRoot;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.SvgRootBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.base.ScenePeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.HtmlSvgNodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlPaints;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.HtmlUtil;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util.SvgUtil;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.platform.util.Numbers;
import dev.webfx.platform.util.collection.Collections;

/**
 * @author Bruno Salmon
 */
public final class SvgScenePeer extends ScenePeerBase implements SvgRoot {

    private final Element container = SvgUtil.createSvgElement();
    private final SvgRoot svgRootDelegate = new SvgRootBase();

    public SvgScenePeer(Scene scene) {
        super(scene);
        HtmlUtil.setAttribute(container, "width", "100%");
        FXProperties.runNowAndOnPropertyChange(this::updateContainerWidth, scene.widthProperty());
        FXProperties.runNowAndOnPropertyChange(this::updateContainerHeight, scene.heightProperty());
        FXProperties.runNowAndOnPropertyChange(this::updateContainerFill, scene.fillProperty());
    }

    private void updateContainerWidth() {
        double width = Numbers.doubleValue(scene.getWidth());
        HtmlUtil.setAttribute(container, "width",
                (width > 0 ?
                        width :
                        scene.getRoot() != null ?
                                scene.getRoot().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight() {
        double height = Numbers.doubleValue(scene.getHeight());
        HtmlUtil.setAttribute(container, "height",
                (height > 0 ?
                        height :
                        scene.getRoot() != null ?
                                scene.getRoot().prefHeight(-1) :
                                0)
                        + "px");
    }

    private void updateContainerFill() {
        HtmlUtil.setAttribute(container, "fill", toPaintAttribute(scene.getFill()));
    }

    private String toPaintAttribute(Paint paint) {
        String value = null;
        if (paint instanceof Color)
            value = HtmlPaints.toSvgCssPaint(paint);
        else if (paint instanceof LinearGradient) {
            Element svgLinearGradient = addDef(SvgUtil.createLinearGradient());
            SvgUtil.updateLinearGradient((LinearGradient) paint, svgLinearGradient);
            value = SvgUtil.getDefUrl(svgLinearGradient);
        }
        return value;
    }

    @Override
    public Element getDefsElement() {
        return svgRootDelegate.getDefsElement();
    }

    @Override
    public Element addDef(Element def) {
        return svgRootDelegate.addDef(def);
    }

    @Override
    public NodePeer pickPeer(double sceneX, double sceneY) {
        Element element = DomGlobal.document.elementFromPoint(sceneX, sceneY);
        return HtmlUtil.getJsJavaObjectAttribute(element, "peer");
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, getDefsElement(), HtmlSvgNodePeer.toContainerElement(scene.getRoot()));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<? extends Node> childrenChange) {
        HtmlSvgNodePeer parentPeer = HtmlSvgNodePeer.toNodePeer(parent);
        HtmlUtil.setChildren(parentPeer.getChildrenContainer(), Collections.map(parent.getChildren(), HtmlSvgNodePeer::toContainerElement));
    }

    @Override
    public void onNodePeerCreated(NodePeer<Node> nodePeer) {
        if (nodePeer instanceof HtmlNodePeer) {
            HtmlNodePeer htmlNodeView = (HtmlNodePeer) nodePeer;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            Element foreignObject = SvgUtil.createSvgElement("foreignObject");
            foreignObject.setAttribute("width", "100%");
            foreignObject.setAttribute("height", "100%");
            HtmlUtil.setChild(foreignObject, htmlElement);
            htmlNodeView.setContainer(foreignObject);
        }
    }
}
