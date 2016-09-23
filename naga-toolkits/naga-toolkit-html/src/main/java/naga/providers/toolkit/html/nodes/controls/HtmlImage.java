package naga.providers.toolkit.html.nodes.controls;

import elemental2.Element;
import elemental2.HTMLElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.util.Strings;
import naga.platform.spi.Platform;
import naga.providers.toolkit.html.nodes.HtmlNode;

import static naga.providers.toolkit.html.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlImage extends HtmlNode<Element> implements naga.toolkit.spi.nodes.controls.Image<Element> {

    public HtmlImage() {
        this(createImageElement());
    }

    public HtmlImage(HTMLElement image) {
        super(image);
        urlProperty.addListener((observable, oldValue, url) -> updateUrl(url));
        widthProperty.addListener((observable, oldValue, width) -> image.setAttribute("width", width));
        heightProperty.addListener((observable, oldValue, height) -> image.setAttribute("height", height));
    }

    protected void updateUrl(String url) {
        // Trying to inline svg images when possible to allow css rules such as svg {fill: currentColor} which is useful
        // to have the same color for the image and the text (in a button for example).
        if (tryInlineSvg(url))
            return;
        node.setAttribute("src", url);
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
                if (getWidth() != null)
                    svgNode.setAttribute("width", getWidth());
                if (getHeight() != null)
                    svgNode.setAttribute("height", getHeight());
                // Replacing the node in the dom (only if it was already attached)
                replaceNode(node, svgNode, false);
                // Switching the node from image to svg (what will be returned by unwrapToNativeNode())
                node = svgNode;
                return true;
            }
        }
        return false;
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

}
