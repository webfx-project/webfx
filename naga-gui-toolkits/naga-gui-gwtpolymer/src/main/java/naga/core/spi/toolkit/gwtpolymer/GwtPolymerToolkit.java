package naga.core.spi.toolkit.gwtpolymer;

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

    public GwtPolymerToolkit() {
        registerNodeFactoryAndWrapper(Table.class, GwtPolymerTable::new, VaadinGrid.class, GwtPolymerTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, GwtPolymerCheckBox::new, PaperCheckbox.class, GwtPolymerCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtPolymerSearchBox::new);
    }
}
