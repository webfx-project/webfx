package mongoose.activities.event.letters;

import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class LettersActivity extends PresentationActivity<LettersViewModel, LettersPresentationModel> {

    public LettersActivity() {
        super(LettersPresentationModel::new);
    }

    protected LettersViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createSearchBox();
        Table table = toolkit.createTable();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new LettersViewModel(toolkit.createVPage()
                .setHeader(searchBox)
                .setCenter(table)
                .setFooter(limitCheckBox)
                , searchBox, table, limitCheckBox);
    }

    protected void bindViewModelWithPresentationModel(LettersViewModel vm, LettersPresentationModel pm) {
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
    protected void initializePresentationModel(LettersPresentationModel pm) {
        pm.eventIdProperty().setValue(getParameter("eventId"));
    }


    protected void bindPresentationModelWithLogic(LettersPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Letter', where: 'active', orderBy: 'id'}")
                // Condition
                .combine(pm.eventIdProperty(), s -> "{where: 'event=" + s + "'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
                .setExpressionColumns("[" +
                        "'name'," +
                        "'type'" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.bookingsDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.bookingsDisplaySelectionProperty(), letter -> {
                    if (letter != null) {
                    }
                });
    }
}
