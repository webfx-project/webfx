package naga.providers.toolkit.html.fx.html.view;

import elemental2.Element;
import naga.commons.util.Strings;
import naga.platform.spi.Platform;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.view.base.ImageViewViewBase;
import naga.toolkit.fx.spi.view.base.ImageViewViewMixin;

import static naga.providers.toolkit.html.util.HtmlUtil.createImageElement;
import static naga.providers.toolkit.html.util.HtmlUtil.createNodeFromHtml;

/**
 * @author Bruno Salmon
 */
public class HtmlImageViewView
        extends HtmlNodeView<ImageView, ImageViewViewBase, ImageViewViewMixin>
        implements ImageViewViewMixin, HtmlLayoutMeasurable {

    public HtmlImageViewView() {
        super(new ImageViewViewBase(), createImageElement());
    }

    @Override
    public void updateImageUrl(String imageUrl) {
        // Trying to inline svg images when possible to allow css rules such as svg {fill: currentColor} which is useful
        // to have the same color for the image and the text (in a button for example).
        if (tryInlineSvg(imageUrl))
            return;
        setElementAttribute("src", imageUrl);
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
}
