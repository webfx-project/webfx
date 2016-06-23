package mongoose.backend.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.vaadin.polymer.paper.widget.PaperCheckbox;
import com.vaadin.polymer.paper.widget.PaperInput;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import mongoose.activities.organizations.OrganizationsViewModel;
import naga.core.ui.presentation.ViewBuilder;
import naga.core.spi.toolkit.gwt.node.GwtNode;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPolymerUi extends Composite {

    interface OrganizationsUiBinder extends UiBinder<HTMLPanel, OrganizationsPolymerUi> {}

    private static OrganizationsUiBinder uiBinder = GWT.create(OrganizationsUiBinder.class);

    public static ViewBuilder<OrganizationsViewModel> viewBuilder = toolkit -> {
        OrganizationsPolymerUi ui = new OrganizationsPolymerUi();
        return new OrganizationsViewModel(new GwtNode(ui.content.getParent()),
                (SearchBox) toolkit.wrapNativeNode(ui.searchInput),
                (Table) toolkit.wrapNativeNode(ui.grid),
                (CheckBox) toolkit.wrapNativeNode(ui.limitCheckBox));
    };

    @UiField
    HTMLPanel content;
    @UiField
    PaperInput searchInput;
    @UiField
    VaadinGrid grid;
    @UiField
    PaperCheckbox limitCheckBox;

    public OrganizationsPolymerUi() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}


