package naga.fx.spi.gwt.html;

import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLLabelElement;
import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;
import naga.commons.util.collection.Collections;
import naga.fx.properties.Properties;
import naga.fx.spi.gwt.html.peer.HtmlHtmlTextPeer;
import naga.fx.spi.gwt.html.peer.HtmlNodePeer;
import naga.fx.spi.gwt.shared.HtmlSvgNodePeer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.ScenePeerBase;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class HtmlScenePeer extends ScenePeerBase {

    private final HTMLElement container = HtmlUtil.createDivElement();

    public HtmlScenePeer(Scene scene) {
        super(scene, HtmlNodePeerFactory.SINGLETON);
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(), scene.widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(), scene.heightProperty());
    }

    public void changedWindowSize(float width, float height) {
        if (listener != null)
            listener.changedSize(width, height);
    }

    private void updateContainerWidth() {
        double width = scene.getWidth();
        HtmlUtil.setStyleAttribute(container, "width",
                (width > 0 ?
                        width :
                        scene.getRoot() != null ?
                                scene.getRoot().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight() {
        double height = scene.getHeight();
        HtmlUtil.setStyleAttribute(container, "height",
                (height > 0 ?
                        height :
                        scene.getRoot() != null ?
                                scene.getRoot().prefHeight(-1) :
                                0)
                        + "px");
    }

    public elemental2.dom.Node getSceneNode() {
        return container;
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, HtmlSvgNodePeer.toElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenPeers(Parent parent) {
        if (!(parent instanceof HtmlText)) {
            elemental2.dom.Node parentNode = HtmlSvgNodePeer.toElement(parent, scene);
            HtmlUtil.setChildren(parentNode, Collections.map(parent.getChildren(), node -> HtmlSvgNodePeer.toElement(node, scene)));
        }
    }

    @Override
    public void onNodePeerCreated(NodePeer<Node> nodePeer) {
        if (nodePeer instanceof HtmlNodePeer) {
            HtmlNodePeer htmlNodePeer = (HtmlNodePeer) nodePeer;
            HTMLElement htmlElement = (HTMLElement) htmlNodePeer.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            if (htmlNodePeer instanceof HtmlHtmlTextPeer)
                htmlElement.style.whiteSpace = "normal";
            else if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement || htmlElement.tagName.equals("SPAN"))
                htmlElement.style.whiteSpace = "nowrap";
        }
    }

}
