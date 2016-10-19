package naga.providers.toolkit.html.nodes.layouts;

import elemental2.Element;
import elemental2.HTMLDivElement;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.spi.nodes.layouts.VBox;

import static naga.providers.toolkit.html.util.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlVBox extends HtmlParent<HTMLDivElement> implements VBox<HTMLDivElement, Element> {

    public HtmlVBox() {
        this(createDiv());
    }

    public HtmlVBox(HTMLDivElement node) {
        super(node);
    }

    @Override
    protected Element prepareChild(Element child) {
        return appendStyle(child, "width: 100%");
    }

    private static HTMLDivElement createDiv() {
        return setStyle(createDivElement(), "width: 100%");
    }

}
