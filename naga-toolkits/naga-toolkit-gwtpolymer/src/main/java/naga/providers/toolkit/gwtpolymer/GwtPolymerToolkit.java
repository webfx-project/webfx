package naga.providers.toolkit.gwtpolymer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.vaadin.polymer.paper.widget.PaperButton;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.paper.widget.PaperSlider;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.providers.toolkit.gwt.GwtToolkit;
import naga.providers.toolkit.gwtpolymer.nodes.controls.*;
import naga.providers.toolkit.gwtpolymer.nodes.layouts.GwtPolymerHBox;
import naga.providers.toolkit.gwtpolymer.nodes.layouts.GwtPolymerVBox;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.providers.toolkit.gwtpolymer.nodes.layouts.GwtPolymerVPage;
import naga.toolkit.spi.nodes.layouts.HBox;

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
        addStyleElement("paper-button {\n" + sharedCss + "}\n");
    }

    private static void addStyleElement(String content) {
        Document doc = Document.get();
        StyleElement styleElement = doc.createStyleElement();
        styleElement.setInnerHTML(content);
        doc.getHead().insertFirst(styleElement);
    }

    public GwtPolymerToolkit() {
        registerNodeFactory(VPage.class, GwtPolymerVPage::new);
        registerNodeFactory(VBox.class, GwtPolymerVBox::new);
        registerNodeFactory(HBox.class, GwtPolymerHBox::new);
        registerNodeFactoryAndWrapper(Table.class, GwtPolymerTable::new, VaadinGrid.class, GwtPolymerTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, GwtPolymerCheckBox::new, PaperCheckbox.class, GwtPolymerCheckBox::new);
        registerNodeFactoryAndWrapper(Button.class, GwtPolymerButton::new, PaperButton.class, GwtPolymerButton::new);
        registerNodeFactoryAndWrapper(SearchBox.class, GwtPolymerSearchBox::new, PaperInput.class, GwtPolymerSearchBox::new);
        registerNodeFactoryAndWrapper(Slider.class, GwtPolymerSlider::new, PaperSlider.class, GwtPolymerSlider::new);
    }
}
