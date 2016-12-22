package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableActivity;
import mongoose.activities.shared.generic.GenericTableEventDependentPresentationModel;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.function.java.AbcNames;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.fx.scene.layout.Priority;

/**
 * @author Bruno Salmon
 */
public class BookingsActivity extends GenericTableActivity<BookingsViewModel, GenericTableEventDependentPresentationModel> {

    public BookingsActivity() {
        super(GenericTableEventDependentPresentationModel::new);
    }

    @Override
    protected BookingsViewModel buildView() {
        // Building the UI components
        TextField searchBox = new TextField();
        DataGrid table = new DataGrid();
        Button newBookingButton = new Button();
        Button cloneEventButton = new Button();
        CheckBox limitCheckBox = new CheckBox();

        HBox hBox = new HBox(newBookingButton, searchBox, cloneEventButton);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.setMaxWidth(Double.MAX_VALUE);
        searchBox.setMaxHeight(Double.MAX_VALUE);
        hBox.setPrefWidth(Double.MAX_VALUE);
        hBox.setMaxWidth(Double.MAX_VALUE);
        table.setMaxWidth(Double.MAX_VALUE);
        table.setMaxHeight(Double.MAX_VALUE);

        return new BookingsViewModel(new BorderPane(table, hBox, null, limitCheckBox, null), searchBox, table, limitCheckBox, newBookingButton, cloneEventButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(BookingsViewModel vm, GenericTableEventDependentPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        I18n i18n = getI18n();
        i18n.translateText(vm.getNewBookingButton(), "NewBooking").setOnMouseClicked(event -> getHistory().push("/event/" + pm.getEventId() + "/fees"));
        i18n.translateText(vm.getCloneEventButton(), "CloneEvent").setOnMouseClicked(event -> getHistory().push("/event/" + pm.getEventId() + "/clone"));
    }

    protected void bindPresentationModelWithLogic(GenericTableEventDependentPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Document', fields: 'cart.uuid', where: '!cancelled', orderBy: 'ref desc'}")
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
                        "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "'person_age','noteIcon'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_minDeposit', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> {
                    if (document != null) {
                        Expression cartUuidExpression = getDataSourceModel().getDomainModel().parseExpression("cart.uuid", "Document");
                        Object cartUuid = document.evaluate(cartUuidExpression);
                        if (cartUuid != null)
                            getHistory().push("/cart/" + cartUuid);
                    }
                }).start();
    }
}
