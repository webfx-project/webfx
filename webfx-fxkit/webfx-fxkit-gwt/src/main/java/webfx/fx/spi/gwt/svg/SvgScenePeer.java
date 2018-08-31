package webfx.fx.spi.gwt.svg;

import com.google.gwt.core.client.JavaScriptObject;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import emul.javafx.collections.ListChangeListener;
import webfx.util.Numbers;
import webfx.util.collection.Collections;
import webfx.fx.properties.Properties;
import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;
import webfx.fx.spi.gwt.shared.HtmlSvgNodePeer;
import webfx.fx.spi.peer.NodePeer;
import webfx.fx.spi.peer.base.ScenePeerBase;
import webfx.fx.spi.gwt.html.peer.HtmlNodePeer;
import webfx.fx.spi.gwt.util.HtmlUtil;
import webfx.fx.spi.gwt.util.SvgUtil;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public class SvgScenePeer extends ScenePeerBase {

    private final Element container = SvgUtil.createSvgElement();
    private final Element defsElement = SvgUtil.createSvgDefs();

    public SvgScenePeer(Scene scene) {
        super(scene, SvgNodePeerFactory.SINGLETON);
        HtmlUtil.setAttribute(container, "width", "100%");
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), scene.widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), scene.heightProperty());
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

    public Element addDef(Element def) {
        defsElement.appendChild(def);
        return def;
    }

    @Override
    public NodePeer pickPeer(double sceneX, double sceneY) {
        Element element = document.elementFromPoint(sceneX, sceneY);
        return (NodePeer) HtmlUtil.getJsJavaObjectAttribute((JavaScriptObject) (Object) element, "peer");
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, defsElement, HtmlSvgNodePeer.toContainerElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<Node> childrenChange) {
        HtmlSvgNodePeer parentPeer = HtmlSvgNodePeer.toNodePeer(parent, scene);
        HtmlUtil.setChildren(parentPeer.getChildrenContainer(), Collections.map(parent.getChildren(), node -> HtmlSvgNodePeer.toContainerElement(node, scene)));
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
