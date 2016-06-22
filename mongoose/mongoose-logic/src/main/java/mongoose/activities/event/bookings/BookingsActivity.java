package mongoose.activities.event.bookings;

import naga.core.orm.entity.Entity;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.function.java.AbcNames;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.BorderPane;
import naga.core.spi.toolkit.nodes.CheckBox;
import naga.core.spi.toolkit.nodes.SearchBox;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.ui.presentation.PresentationActivity;
import naga.core.ui.rx.RxFilter;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class BookingsActivity extends PresentationActivity<BookingsViewModel, BookingsPresentationModel> {

    public BookingsActivity() {
        super(BookingsPresentationModel::new);
    }

    protected BookingsViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createNode(SearchBox.class);
        Table table = toolkit.createNode(Table.class);
        CheckBox limitCheckBox = toolkit.createNode(CheckBox.class);

        return new BookingsViewModel(toolkit.createNode(BorderPane.class)
                .setTop(searchBox)
                .setCenter(table)
                .setBottom(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(BookingsViewModel vm, BookingsPresentationModel pm) {
        // Hard coded initialization
        SearchBox searchBox = vm.getSearchBox();
        CheckBox limitCheckBox = vm.getLimitCheckBox();
        searchBox.setPlaceholder("Search here to narrow the list");
        searchBox.requestFocus();
        limitCheckBox.setText("Limit to 100");

        // Initialization from the presentation model current state
        searchBox.setText(pm.searchTextProperty().getValue());
        limitCheckBox.setSelected(pm.limitProperty().getValue());

        // Binding the UI with the presentation model for further state changes
        // User inputs: the UI state changes are transferred in the presentation model
        pm.searchTextProperty().bind(searchBox.textProperty());
        pm.limitProperty().bind(limitCheckBox.selectedProperty());
        pm.bookingsDisplaySelectionProperty().bind(vm.getTable().displaySelectionProperty());
        // User outputs: the presentation model changes are transferred in the UI
        vm.getTable().displayResultSetProperty().bind(pm.bookingsDisplayResultSetProperty());
    }

    @Override
    protected void initializePresentationModel(BookingsPresentationModel pm) {
        pm.eventIdProperty().setValue(getParams().get("eventId"));
    }


    protected void bindPresentationModelWithLogic(BookingsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        RxFilter rxFilter = createRxFilter("{class: 'Document', fields: 'cart.uuid', where: '!cancelled', orderBy: 'ref desc'}")
                // Condition
                .combine(pm.eventIdProperty(), s -> "{where: 'event=" + s + "'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> {
                    if (Strings.isEmpty(s))
                        return null;
                    s = s.trim();
                    if (Character.isDigit(s.charAt(0)))
                        return "{where: 'ref = " + s + "'}";
                    if (s.contains("@"))
                        return "{where: 'lower(person_email) like `%" + s.toLowerCase() + "%`'}";
                    return "{where: 'person_abcNames like `" + AbcNames.evaluate(s, true) + "`'}";
                })
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                                "'ref'," +
                                "'person_firstName'," +
                                "'person_lastName'," +
                                "'person_age'," +
                                "{expression: 'price_net', format: 'price'}," +
                                "{expression: 'price_minDeposit', format: 'price'}," +
                                "{expression: 'price_deposit', format: 'price'}," +
                                "{expression: 'price_balance', format: 'price'}" +
                        "]")
                .setDisplaySelectionProperty(pm.bookingsDisplaySelectionProperty())
                .displayResultSetInto(pm.bookingsDisplayResultSetProperty());

        rxFilter.getDisplaySelectionProperty().addListener((observable, oldValue, newValue) -> {
            Entity document = rxFilter.getSelectedEntity();
            if (document != null) {
                Expression cartUuidExpression = getDataSourceModel().getDomainModel().parseExpression("cart.uuid", "Document");
                Object cartUuid = document.evaluate(cartUuidExpression);
                if (cartUuid != null)
                    getHistory().push("/cart/" + cartUuid);
            }
        });
    }
}
