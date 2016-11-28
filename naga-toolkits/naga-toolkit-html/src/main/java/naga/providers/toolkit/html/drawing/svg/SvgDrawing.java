package naga.providers.toolkit.html.drawing.svg;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.html.view.HtmlNodeView;
import naga.providers.toolkit.html.drawing.svg.view.SvgNodeView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.Parent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.NodeView;

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
        HtmlUtil.setChildren(parent, defsElement, getNodeElementForParent(rootNode));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node svgParent = getNodeElementForParent(parent);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), this::getNodeElementForParent));
    }

    private Element getNodeElementForParent(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node); // Should be a SvgNodeView or a HtmlNodeView
        if (nodeView instanceof SvgNodeView) // SvgNodeView case
            return ((SvgNodeView) nodeView).getElement();
        if (nodeView instanceof HtmlNodeView) // HtmlNodeView case
            return ((HtmlNodeView) nodeView).getContainer();
        // Shouldn't happen unless no view factory is registered for this node (probably UnimplementedNodeView was returned)
        return null; // returning null in this case to indicate there is no view to show
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
