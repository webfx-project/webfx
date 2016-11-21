package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.view.SvgNodeView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Parent;
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
        HtmlUtil.setChildren(parent, defsElement, getSvgNodeElement(rootNode));
    }

    @Override
    protected void updateParentAndChildrenViews(Parent parent) {
        elemental2.Node svgParent = getSvgNodeElement(parent);
        HtmlUtil.setChildren(svgParent, Collections.convert(parent.getChildren(), this::getSvgNodeElement));
    }

    private SvgNodeView getOrCreateAndBindSvgDrawableView(Node node) {
        NodeView nodeView = getOrCreateAndBindNodeView(node); // Should be a FxDrawableView (but may be UnimplementedNodeView if no view factory is registered for this node)
        if (nodeView instanceof SvgNodeView) // Should be a SvgNodeView
            return (SvgNodeView) nodeView;
        // Shouldn't happen unless no view factory is registered for this node (probably UnimplementedNodeView was returned)
        return null; // returning null in this case to indicate there is no view to show
    }

    private Element getSvgNodeElement(Node node) {
        SvgNodeView svgDrawableView = getOrCreateAndBindSvgDrawableView(node);
        return svgDrawableView == null ? null : svgDrawableView.getElement();
    }

}
