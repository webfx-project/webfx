package naga.toolkit.providers.gwtpolymer.nodes.layouts;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.PolymerWidget;
import naga.toolkit.providers.gwt.nodes.GwtParent;
import naga.toolkit.spi.nodes.layouts.HBox;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerHBox extends GwtParent<Panel> implements HBox<Panel, Widget> {

    public GwtPolymerHBox() {
        this(new LayoutFlowPanel());
    }

    public GwtPolymerHBox(Panel node) {
        super(node);
        node.getElement().addClassName("layout horizontal");
        //node.getElement().setAttribute("style", "height: 100%;");
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
        if (!(child instanceof PolymerWidget)) // ex: not button but gauge and chart
            child.addStyleName("flex");
        return child;
    }

}
