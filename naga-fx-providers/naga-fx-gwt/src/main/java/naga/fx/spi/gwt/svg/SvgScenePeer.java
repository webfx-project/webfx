package naga.fx.spi.gwt.svg;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.fx.properties.Properties;
import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;
import naga.fx.spi.gwt.shared.HtmlSvgNodePeer;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.ScenePeerBase;
import naga.fx.spi.gwt.html.peer.HtmlNodePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.gwt.util.SvgUtil;

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
    public void onRootBound() {
        HtmlUtil.setChildren(container, defsElement, HtmlSvgNodePeer.toElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent) {
        elemental2.Node svgParent = HtmlSvgNodePeer.toElement(parent, scene);
        HtmlUtil.setChildren(svgParent, Collections.map(parent.getChildren(), node -> HtmlSvgNodePeer.toElement(node, scene)));
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
