package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableActivity;
import mongoose.activities.shared.generic.GenericTableEventDependentPresentationModel;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.function.java.AbcNames;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;

/**
 * @author Bruno Salmon
 */
public class BookingsActivity extends GenericTableActivity<BookingsViewModel, GenericTableEventDependentPresentationModel> {

    public BookingsActivity() {
        super(GenericTableEventDependentPresentationModel::new);
    }

    @Override
    protected BookingsViewModel buildView(Toolkit toolkit) {
        // Building the UI components
        SearchBox searchBox = toolkit.createSearchBox();
        Table table = toolkit.createTable();
        Button newBookingButton = toolkit.createButton();
        CheckBox limitCheckBox = toolkit.createCheckBox();

        return new BookingsViewModel(toolkit.createVPage()
                .setHeader(toolkit.createHBox(newBookingButton, searchBox))
                .setCenter(table)
                .setFooter(limitCheckBox)
                , searchBox, table, limitCheckBox, newBookingButton);
    }

    @Override
    protected void bindViewModelWithPresentationModel(BookingsViewModel vm, GenericTableEventDependentPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        // Hard coded initialization
        I18n i18n = getI18n();
        i18n.translateText(vm.getNewBookingButton(), "NewBooking").actionEventObservable().subscribe(actionEvent -> getHistory().push("/event/" + pm.getEventId() + "/fees"));
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
