package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLImageElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;

/**
 * @author Bruno Salmon
 */
public class HtmlImage extends HtmlNode<HTMLImageElement> implements naga.toolkit.spi.nodes.controls.Image<HTMLImageElement> {

    public HtmlImage() {
        this(HtmlUtil.createImageElement());
    }

    public HtmlImage(HTMLImageElement image) {
        super(image);
        urlProperty.addListener((observable, oldValue, newValue) -> image.setAttribute("src", newValue));
    }

    private final Property<String> urlProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> urlProperty() {
        return urlProperty;
    }

}
