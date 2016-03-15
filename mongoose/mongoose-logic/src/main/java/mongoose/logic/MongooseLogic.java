package mongoose.logic;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.orm.filter.StringFilterBuilder;
import naga.core.rx.RxFilter;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.node.BorderPane;
import naga.core.spi.gui.node.SearchBox;
import naga.core.spi.gui.node.Table;
import naga.core.spi.gui.node.ToggleButton;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class MongooseLogic {

    public static void runBackendApplication() {
        // Building the UI components (involved in the reactive filter)
        GuiToolkit toolkit = GuiToolkit.get();
        SearchBox<?> searchBox = toolkit.createNode(SearchBox.class);
        Table table = toolkit.createNode(Table.class);
        ToggleButton<?> limitCheckBox = toolkit.createNode(ToggleButton.class);
        limitCheckBox.setText("Limit to 100");
        limitCheckBox.setSelected(true);

        // Loading the domain model (in the background) and setting up the reactive filter
        Platform.runInBackground(() -> {
            new RxFilter()
                    .setDomainModel(DomainModelSnapshotLoader.getOrLoadDomainModel())
                    .setDataSourceId(3)
                    // Base filter
                    .combine(new StringFilterBuilder("Organization")
                            .setLogicFields("country.(code,continent.code)")
                            .setOrderBy("name"))
                    // Condition
                    .combine(new StringFilterBuilder()
                            .setCondition("!closed"))
                    // Fields to display
                    .combine(new StringFilterBuilder()
                            .setDisplayFields("name,type.name, country.(name + ' (' + continent.name + ')')"))
                    // Search box condition
                    .combine(searchBox, s -> "lower(name) like '%" + s.toLowerCase() + "%'")
                    // Limit condition
                    .combine(limitCheckBox, new StringFilterBuilder()
                            .setLimit("100"))
                    .displayResultInto(table);
        });

        // Displaying the UI
        toolkit.displayRootNode(toolkit.createNode(BorderPane.class)
                .setTop(searchBox)
                .setCenter(table)
                .setBottom(limitCheckBox)); // Showing the first windonw can take a few seconds
        // Requesting the focus on the search box
        searchBox.requestFocus();
    }

}
