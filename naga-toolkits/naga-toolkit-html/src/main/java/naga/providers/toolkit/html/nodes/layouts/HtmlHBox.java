package naga.providers.toolkit.html.nodes.layouts;

import elemental2.Element;
import elemental2.HTMLElement;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.HBox;

/**
 * @author Bruno Salmon
 */
public class HtmlHBox extends HtmlParent<HTMLElement> implements HBox {

    public HtmlHBox() {
        this(HtmlUtil.createSpanElement());
    }

    public HtmlHBox(HTMLElement node) {
        super(node);
    }

    @Override
    protected Element prepareChild(Element child) {
        if (child != Toolkit.unwrapToNativeNode(Collections.last(getChildren())))
            HtmlUtil.setStyleAttribute(child, "margin-right", "5px");
        return HtmlUtil.setStyleAttribute(child, "vertical-align", "middle");
    }
}
