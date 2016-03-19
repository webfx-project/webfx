package mongoose.backend.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialSearch;
import mongoose.logic.MongooseLogic;
import mongoose.logic.OrganizationsPresentationModel;
import naga.core.ngui.routing.UiState;
import naga.core.spi.bus.client.WebSocketBusOptions;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.toolkit.web.WebToolkit;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.gwt.GwtPlatform;
import naga.core.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class MongooseBackendGwtApplication implements EntryPoint {

    /* No need for GwtPlatform.register(); as the platform will be found by the customized ServiceLoader provided in the super-source */

    @Override
    public void onModuleLoad() {
        replaceSplashScreen(Document.get().createBRElement());

        registerResourceBundles();
        GuiToolkit.register(new WebToolkit());
        MongooseLogic.organizationsUiRouterHandler.setUiBuilder(this::buildOrganizationsUi);

        MongooseLogic.runBackendApplication();
    }

    private void buildOrganizationsUi(UiState uiState) {

        OrganizationsPresentationModel pm = (OrganizationsPresentationModel) uiState.presentationModel();

        DockLayoutPanel p = new DockLayoutPanel(Style.Unit.EM);

        MaterialSearch search = new MaterialSearch();
        search.setPlaceholder("Enter your centre name to narrow the list");
        p.addNorth(search, 5);
        search.addValueChangeHandler(event -> pm.searchTextProperty().setValue(search.getValue()));
        search.addKeyUpHandler(event -> pm.searchTextProperty().setValue(search.getValue()));

        MaterialCheckBox limitCheckBox = new MaterialCheckBox("Limit to 100");
        limitCheckBox.setValue(true);
        p.addSouth(limitCheckBox, 2.5);
        pm.limitProperty().setValue(limitCheckBox.getValue());
        limitCheckBox.addClickHandler(event -> pm.limitProperty().setValue(limitCheckBox.getValue()));

        DataGrid<Integer> table = new DataGrid<>();
        table.setWidth("100%");
        table.setHeight("100%");
        table.addStyleName("bordered");

        table.addColumn(new TextColumn<Integer>() {
            @Override
            public String getValue(Integer row) {
                return (String) pm.organizationDisplayResultProperty().getValue().getValue(row, 0);
            }}, "Name");
        table.addColumn(new TextColumn<Integer>() {
            @Override
            public String getValue(Integer row) {
                return (String) pm.organizationDisplayResultProperty().getValue().getValue(row, 1);
            }}, "Type");
        table.addColumn(new TextColumn<Integer>() {
            @Override
            public String getValue(Integer row) {
                return (String) pm.organizationDisplayResultProperty().getValue().getValue(row, 2);
            }}, "Country");

        pm.organizationDisplayResultProperty().addListener((observable, oldValue, newValue) -> {
            table.setRowCount(0, true);
            table.setRowData(new IdentityList(newValue.getRowCount()));
            table.setRowCount(newValue.getRowCount(), true);
            table.redraw(); // otherwise the change on setRowData() is not considered
        });

        p.add(table);
        RootLayoutPanel.get().add(p);
    }

    private static void registerResourceBundles() {
        GwtPlatform.registerBundle(MongooseBackendGwtBundle.B);
    }

    private static native void replaceSplashScreen(Element e) /*-{ var preloader = $wnd.document.getElementById("preloader"); preloader.innerHTML = ""; preloader.appendChild(e); }-*/;


}
