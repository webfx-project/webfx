package naga.core.spi.toolkit.gwtpolymer.nodes;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.Polymer;
import naga.core.spi.toolkit.gwt.nodes.GwtParent;
import naga.core.spi.toolkit.nodes.VBox;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerVBox extends GwtParent<Panel> implements VBox<Panel, Widget> {

    public GwtPolymerVBox() {
        this(new HTMLPanel("div", ""));
    }

    public GwtPolymerVBox(Panel node) {
        super(node);
        node.getElement().addClassName("layout vertical");
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
