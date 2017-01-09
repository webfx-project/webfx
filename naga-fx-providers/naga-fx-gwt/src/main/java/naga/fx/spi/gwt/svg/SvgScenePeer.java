package naga.fx.spi.gwt.svg;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.fx.properties.Properties;
import naga.fx.scene.Node;
import naga.fx.scene.Parent;
import naga.fx.scene.Scene;
import naga.fx.naga.tk.ScenePeerBase;
import naga.fx.spi.gwt.html.viewer.HtmlNodeViewer;
import naga.fx.spi.gwt.shared.HtmlSvgNodeViewer;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.gwt.util.SvgUtil;
import naga.fx.spi.viewer.NodeViewer;

/**
 * @author Bruno Salmon
 */
public class SvgScenePeer extends ScenePeerBase {

    private final Element container = SvgUtil.createSvgElement();
    private final Element defsElement = SvgUtil.createSvgDefs();

    public SvgScenePeer(Scene scene) {
        super(scene, SvgNodeViewerFactory.SINGLETON);
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
        HtmlUtil.setChildren(container, defsElement, HtmlSvgNodeViewer.toElement(scene.getRoot(), scene));
    }

    @Override
    public void updateParentAndChildrenViewers(Parent parent) {
        elemental2.Node svgParent = HtmlSvgNodeViewer.toElement(parent, scene);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeViewer.toElement(node, scene)));
    }

    @Override
    public void onNodeViewerCreated(NodeViewer<Node> nodeViewer) {
        if (nodeViewer instanceof HtmlNodeViewer) {
            HtmlNodeViewer htmlNodeView = (HtmlNodeViewer) nodeViewer;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            Element foreignObject = SvgUtil.createSvgElement("foreignObject");
            foreignObject.setAttribute("width", "100%");
            foreignObject.setAttribute("height", "100%");
            HtmlUtil.setChild(foreignObject, htmlElement);
            htmlNodeView.setContainer(foreignObject);
        }
    }
}
