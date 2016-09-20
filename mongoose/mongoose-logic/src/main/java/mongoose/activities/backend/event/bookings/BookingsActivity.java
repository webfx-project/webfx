package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.GenericTableActivity;
import mongoose.activities.shared.generic.GenericTableEventDependentPresentationModel;
import mongoose.activities.shared.generic.GenericTableViewModel;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.function.java.AbcNames;

/**
 * @author Bruno Salmon
 */
public class BookingsActivity extends GenericTableActivity<GenericTableViewModel, GenericTableEventDependentPresentationModel> {

    public BookingsActivity() {
        super(GenericTableEventDependentPresentationModel::new);
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
