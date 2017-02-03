package naga.fx.spi.gwt.html.peer;

import elemental2.Element;
import elemental2.HTMLElement;
import elemental2.HTMLImageElement;
import emul.javafx.geometry.BoundingBox;
import emul.javafx.geometry.Bounds;
import emul.javafx.scene.image.Image;
import emul.javafx.scene.image.ImageView;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.fx.spi.peer.base.ImageViewPeerBase;
import naga.fx.spi.peer.base.ImageViewPeerMixin;
import naga.platform.spi.Platform;

import static naga.fx.spi.gwt.util.HtmlUtil.createImageElement;
import static naga.fx.spi.gwt.util.HtmlUtil.createNodeFromHtml;

/**
 * @author Bruno Salmon
 */
public class HtmlImageViewPeer
        <N extends ImageView, NB extends ImageViewPeerBase<N, NB, NM>, NM extends ImageViewPeerMixin<N, NB, NM>>

        extends HtmlNodePeer<N, NB, NM>
        implements ImageViewPeerMixin<N, NB, NM>, HtmlLayoutMeasurable {

    public HtmlImageViewPeer() {
        this((NB) new ImageViewPeerBase(), createImageElement());
    }

    public HtmlImageViewPeer(NB base, HTMLElement element) {
        super(base, element);
        HTMLImageElement e = (HTMLImageElement) getElement();
        e.onload = evt -> {
            clearCache();
            N node = getNode();
            if (sizeChangedCallback != null && Numbers.doubleValue(node.getFitWidth()) == 0 && Numbers.doubleValue(node.getFitHeight()) == 0)
                sizeChangedCallback.run();
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
        String imageUrl = image == null ? null : image.getUrl();
        if (tryInlineSvg(imageUrl))
            return;
        setElementAttribute("src", imageUrl);
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
    }

    @Override
    public void updateY(Double y) {
    }

    boolean tryInlineSvg(String url) {
        // First checking the extension is svg
        if (Strings.endsWith(url, ".svg")) {
            // We do inline svg only for images that have been included in the resources
            String svgFile = Platform.getResourceService().getText(url).result();
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
                // Switching the node from image to svg
                setContainer(svgNode);
                return true;
            }
        }
        return false;
    }

    // Overriding HtmlLayoutMeasurer for the inline svg case -> 2 problems in this case:
    // 1) the element to measure is the container (svg node) and not the element (empty img)
    // 2) there is no SvgLayoutMeasurer at the moment (should be based on getBBox)
    // For now, we run with the following code that at least works when fitWith and fitHeight are set

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
}
