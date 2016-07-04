package naga.core.spi.toolkit.gwtpolymer.layouts;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.PolymerWidget;
import naga.core.spi.toolkit.gwt.node.GwtParent;
import naga.core.spi.toolkit.layouts.VBox;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerVBox extends GwtParent<Panel> implements VBox<Panel, Widget> {

    public GwtPolymerVBox() {
        this(new LayoutFlowPanel());
    }

    public GwtPolymerVBox(Panel node) {
        super(node);
        node.getElement().addClassName("layout vertical");
        node.getElement().setAttribute("style", "width: 100%");
        Polymer.importHref("iron-flex-layout/iron-flex-layout.html", o -> {
            Document doc = Document.get();
            StyleElement styleElement = doc.createStyleElement();
            styleElement.setAttribute("is", "custom-style");
            styleElement.setAttribute("include", "iron-flex iron-flex-alignment");
            doc.getHead().insertFirst(styleElement);
            return null;
        });
    }

    @Override
    protected Widget prepareChildWidget(Widget child) {
        // This code should'nt be here but part of the UI building
        if (!(child instanceof PolymerWidget)) // ex: hbox
            child.addStyleName("flex");
        else
            child.setWidth("100%"); // ex: slider
        return child;
    }
}
