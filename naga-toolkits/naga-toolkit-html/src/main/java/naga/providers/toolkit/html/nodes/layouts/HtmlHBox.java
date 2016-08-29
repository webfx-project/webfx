package naga.providers.toolkit.html.nodes.layouts;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.spi.nodes.layouts.HBox;

/**
 * @author Bruno Salmon
 */
public class HtmlHBox extends HtmlParent<HTMLElement> implements HBox<HTMLElement, Element> {

    public HtmlHBox() {
        this(HtmlUtil.createSpanElement());
    }

    public HtmlHBox(HTMLElement node) {
        super(node);
    }

    @Override
    protected Element prepareChild(Element child) {
        return HtmlUtil.appendStyle(child, "vertical-align: middle; margin-right: 5px");
    }
}
