package naga.fx.spi.gwt.html.peer;

import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLImageElement;
import emul.javafx.scene.HasSizeChangedCallback;
import emul.javafx.scene.Parent;
import emul.javafx.scene.control.Labeled;
import emul.javafx.scene.image.Image;
import emul.javafx.scene.image.ImageView;
import naga.fx.spi.gwt.util.HtmlPaints;
import naga.fx.spi.gwt.util.HtmlUtil;
import naga.fx.spi.peer.base.ImageViewPeerBase;
import naga.fx.spi.peer.base.ImageViewPeerMixin;
import naga.platform.services.resource.ResourceService;
import naga.util.Numbers;
import naga.util.Strings;

import static naga.fx.spi.gwt.util.HtmlUtil.createImageElement;
import static naga.fx.spi.gwt.util.HtmlUtil.createNodeFromHtml;

/**
 * @author Bruno Salmon
 */
public class HtmlImageViewPeer
        <N extends ImageView, NB extends ImageViewPeerBase<N, NB, NM>, NM extends ImageViewPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements ImageViewPeerMixin<N, NB, NM>, HasSizeChangedCallback {

    //private Double loadedWidth, loadedHeight;

    public HtmlImageViewPeer() {
        this((NB) new ImageViewPeerBase(), createImageElement());
    }

    public HtmlImageViewPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLImageElement e = (HTMLImageElement) getElement();
        e.onload = evt -> {
            onLoad();
            return null;
        };
    }

    private Runnable sizeChangedCallback;

    @Override
    public void setSizeChangedCallback(Runnable sizeChangedCallback) {
        this.sizeChangedCallback = sizeChangedCallback;
    }

    @Override
    public void updateImage(Image image) {
        // Trying to inline svg images when possible to allow css rules such as svg {fill: currentColor} which is useful
        // to have the same color for the image and the text (in a button for example).
        //loadedWidth = loadedHeight = null;
        String imageUrl = image == null ? null : image.getUrl();
        if (tryInlineSvg(imageUrl))
            onLoad();
        else
            setElementAttribute("src", imageUrl);
    }

    private void onLoad() {
        N node = getNode();
        Image image = node.getImage();
        if (image != null) {
            HTMLElement element = getElement();
            if (element instanceof HTMLImageElement) {
                HTMLImageElement imageElement = (HTMLImageElement) element;
                image.setWidth((double) imageElement.naturalWidth);
                image.setHeight((double) imageElement.naturalHeight);
            }
        }
        if (sizeChangedCallback != null) // && loadedWidth == null && loadedHeight == null && Numbers.doubleValue(node.getFitWidth()) == 0 && Numbers.doubleValue(node.getFitHeight()) == 0)
            sizeChangedCallback.run();
    }

    @Override
    public void updateFitWidth(Double fitWidth) {
        setElementAttribute("width", Numbers.doubleValue(fitWidth) == 0 ? null : toPx(fitWidth));
    }

    @Override
    public void updateFitHeight(Double fitHeight) {
        setElementAttribute("height", Numbers.doubleValue(fitHeight) == 0 ? null : toPx(fitHeight));
    }

    @Override
    public void updateX(Double x) {
        setElementStyleAttribute("left", toPx(x));
    }

    @Override
    public void updateY(Double y) {
        setElementStyleAttribute("top", toPx(y));
    }

    boolean tryInlineSvg(String url) {
        // First checking the extension is svg
        if (Strings.endsWith(url, ".svg")) {
            // We do inline svg only for images that have been included in the resources
            String svgFile = ResourceService.getText(url).result();
            if (svgFile != null) { // Yes the images is in the resources so we have the content already
                // Removing all what is before the svg tag (ex: <?xml ...?>)
                int svgTagIndex = svgFile.indexOf("<svg");
                if (svgTagIndex != -1)
                    svgFile = svgFile.substring(svgTagIndex);
                // Creating the svg element from the file content
                Element svgNode = createNodeFromHtml(svgFile);
                // Setting width and height if defined
                ImageView imageView = getNode();
                double fitWidth = imageView.getFitWidth();
                if (fitWidth > 0)
                    svgNode.setAttribute("width", fitWidth);
                double fitHeight = imageView.getFitHeight();
                if (fitHeight > 0)
                    svgNode.setAttribute("height", fitHeight);
                // Applying the same color (in case svg is monochrome) as the text if in a label
                Parent parent = getNode().getParent();
                if (parent instanceof Labeled)
                    applyTextFillToSvg(svgNode, HtmlPaints.toHtmlCssPaint(((Labeled) parent).getTextFill()));
                // Switching the node from image to svg
                setContainer(svgNode);
                HtmlUtil.replaceNode(getElement(), svgNode);
                return true;
            }
        }
        return false;
    }

    public static void applyTextFillToSvg(Element svgNode, String fill) {
        if ("SVG".equalsIgnoreCase(svgNode.tagName))
            setElementAttribute(svgNode, "fill", fill);
    }

    // Overriding HtmlLayoutMeasurer for the inline svg case -> 2 problems in this case:
    // 1) the element to measure is the container (svg node) and not the element (empty img)
    // 2) there is no SvgLayoutMeasurer at the moment (should be based on getBBox)
    // For now, we run with the following code that at least works when fitWith and fitHeight are set

/*
    @Override
    public Bounds getLayoutBounds() {
        return new BoundingBox(0, 0, 0, prefWidth(-1), prefHeight(-1), 0);
    }

    @Override
    public double minWidth(double height) {
        return prefWidth(height);
    }

    @Override
    public double maxWidth(double height) {
        return prefWidth(height);
    }

    @Override
    public double minHeight(double width) {
        return prefHeight(width);
    }

    @Override
    public double maxHeight(double width) {
        return prefHeight(width);
    }

    @Override
    public double prefWidth(double height) {
        double fitWidth = getNode().getFitWidth();
        return fitWidth > 0 ? fitWidth : measureWidth(height);
    }

    @Override
    public double prefHeight(double width) {
        double fitHeight = getNode().getFitHeight();
        return fitHeight > 0 ? fitHeight : measureHeight(width);
    }

    @Override
    public double measureWidth(double height) {
        if (loadedWidth != null)
            return loadedWidth;
        HTMLElement element = getElement();
        double width;
        if (element instanceof HTMLImageElement)
            width = ((HTMLImageElement) element).naturalWidth;
        else
            width = sizeAndMeasure(height, true);
        if (width > 0) {
            loadedWidth = width;
            if (sizeChangedCallback != null && loadedHeight == null)
                sizeChangedCallback.run();
        }
        return width;
    }

    @Override
    public double measureHeight(double width) {
        if (loadedHeight != null)
            return loadedHeight;
        HTMLElement element = getElement();
        double height;
        if (element instanceof HTMLImageElement)
            height = ((HTMLImageElement) element).naturalHeight;
        else
            height = sizeAndMeasure(width, false);
        if (height > 0) {
            loadedHeight = height;
            if (sizeChangedCallback != null && loadedWidth == null)
                sizeChangedCallback.run();
        }
        return height;
    }
*/
}
