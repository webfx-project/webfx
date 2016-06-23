package naga.core.spi.toolkit.gwtpolymer.layouts;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.Polymer;
import naga.core.spi.toolkit.gwt.node.GwtParent;
import naga.core.spi.toolkit.layouts.HBox;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerHBox extends GwtParent<Panel> implements HBox<Panel, Widget> {

    public GwtPolymerHBox() {
        this(new HTMLPanel("div", ""));
    }

    public GwtPolymerHBox(Panel node) {
        super(node);
        node.getElement().addClassName("layout horizontal");
        node.getElement().setAttribute("style", "100%");
        Polymer.importHref("iron-flex-layout/iron-flex-layout.html", o -> {
            Document doc = Document.get();
            StyleElement styleElement = doc.createStyleElement();
            styleElement.setAttribute("is", "custom-style");
            styleElement.setAttribute("include", "iron-flex iron-flex-alignment");
            doc.getHead().insertFirst(styleElement);
            return null;
        });
    }
}
