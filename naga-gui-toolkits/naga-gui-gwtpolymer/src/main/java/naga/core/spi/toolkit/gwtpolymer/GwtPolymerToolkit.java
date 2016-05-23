package naga.core.spi.toolkit.gwtpolymer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.core.spi.toolkit.gwt.GwtToolkit;
import naga.core.spi.toolkit.gwtpolymer.nodes.GwtPolymerCheckBox;
import naga.core.spi.toolkit.gwtpolymer.nodes.GwtPolymerSearchBox;
import naga.core.spi.toolkit.gwtpolymer.nodes.GwtPolymerTable;
import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.SearchBox;
import naga.core.spi.toolkit.nodes.Table;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerToolkit extends GwtToolkit {

    static {
        String sharedCss =
                "    font-family: 'Roboto', 'Noto', sans-serif;\n" +
                "    -webkit-font-smoothing: antialiased;\n" +
                "    font-size: 16px;\n" +
                "    font-weight: 400;\n" +
                "    line-height: 24px;\n";
        addStyleElement("vaadin-grid, .vaadin-grid-header.vaadin-grid th.vaadin-grid {\n" + sharedCss + "}\n");
        addStyleElement("paper-checkbox {\n" + sharedCss + "}\n");
    }

    private static void addStyleElement(String content) {
        Document doc = Document.get();
        StyleElement styleElement = doc.createStyleElement();
        styleElement.setInnerHTML(content);
        doc.getHead().insertFirst(styleElement);
    }

    public GwtPolymerToolkit() {
        registerNodeFactoryAndWrapper(Table.class, GwtPolymerTable::new, VaadinGrid.class, GwtPolymerTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, GwtPolymerCheckBox::new, PaperCheckbox.class, GwtPolymerCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtPolymerSearchBox::new);
    }
}
