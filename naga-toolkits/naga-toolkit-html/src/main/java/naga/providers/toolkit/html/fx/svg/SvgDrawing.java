package naga.providers.toolkit.html.fx.svg;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.fx.html.view.HtmlNodeView;
import naga.providers.toolkit.html.fx.shared.HtmlSvgNodeView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.Parent;
import naga.toolkit.fx.spi.impl.DrawingImpl;
import naga.toolkit.fx.spi.view.NodeView;

/**
 * @author Bruno Salmon
 */
public class SvgDrawing extends DrawingImpl {

    private final Element defsElement = SvgUtil.createSvgDefs();

    SvgDrawing(SvgDrawingNode svgDrawingNode) {
        super(svgDrawingNode, SvgNodeViewFactory.SINGLETON);
    }

    public Element addDef(Element def) {
        defsElement.appendChild(def);
        return def;
    }

    @Override
    protected void createAndBindRootNodeViewAndChildren(Node rootNode) {
        super.createAndBindRootNodeViewAndChildren(rootNode);
        elemental2.Node parent = drawingNode.unwrapToNativeNode();
        HtmlUtil.setChildren(parent, defsElement, HtmlSvgNodeView.toElement(rootNode, this));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node svgParent = HtmlSvgNodeView.toElement(parent, this);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), node -> HtmlSvgNodeView.toElement(node, this)));
    }

    @Override
    protected NodeView<Node> createNodeView(Node node) {
        NodeView<Node> nodeView = super.createNodeView(node);
        if (nodeView instanceof HtmlNodeView) {
            HtmlNodeView htmlNodeView = (HtmlNodeView) nodeView;
            HTMLElement htmlElement = (HTMLElement) htmlNodeView.getElement();
            Element foreignObject = SvgUtil.createSvgElement("foreignObject");
            foreignObject.setAttribute("width", "100%");
            foreignObject.setAttribute("height", "100%");
            HtmlUtil.setChild(foreignObject, htmlElement);
            htmlNodeView.setContainer(foreignObject);
        }
        return nodeView;
    }
}
