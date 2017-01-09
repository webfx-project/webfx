package naga.fx.spi.gwt.html;

import elemental2.HTMLButtonElement;
import elemental2.HTMLElement;
import elemental2.HTMLLabelElement;
import naga.commons.util.collection.Collections;
import naga.fx.properties.Properties;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.naga.tk.ScenePeerBase;
import naga.fx.spi.gwt.html.viewer.HtmlNodeViewer;
import naga.fx.spi.gwt.shared.HtmlSvgNodeViewer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.viewer.NodeViewer;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class HtmlScenePeer extends ScenePeerBase {

    private final HTMLElement container = HtmlUtil.createDivElement();

    public HtmlScenePeer(Scene scene) {
        super(scene, HtmlNodeViewerFactory.SINGLETON);
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

    public elemental2.Node getSceneNode() {
        return container;
    }

    @Override
    public void onRootBound() {
        HtmlUtil.setChildren(container, HtmlSvgNodeViewer.toElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenViewers(Parent parent) {
        if (!(parent instanceof HtmlText)) {
            elemental2.Node parentNode = HtmlSvgNodeViewer.toElement(parent, scene);
            HtmlUtil.setChildren(parentNode, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeViewer.toElement(node, scene)));
        }
    }

    @Override
    public void onNodeViewerCreated(NodeViewer<Node> nodeViewer) {
        if (nodeViewer instanceof HtmlNodeViewer) {
            HtmlNodeViewer htmlNodeView = (HtmlNodeViewer) nodeViewer;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            HtmlUtil.absolutePosition(htmlElement);
            if (htmlElement instanceof HTMLButtonElement || htmlElement instanceof HTMLLabelElement || htmlElement.tagName.equals("SPAN") && !(((HtmlNodeViewer) nodeViewer).getNode() instanceof HtmlText))
                htmlElement.style.whiteSpace = "nowrap";
        }
    }

}
