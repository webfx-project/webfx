package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.function.java.AbcNames;
import naga.framework.ui.filter.ReactiveExpressionFilter;

/**
 * @author Bruno Salmon
 */
public class BookingsPresentationLogicActivity extends EventDependentPresentationLogicActivity<BookingsPresentationModel> {

    public BookingsPresentationLogicActivity() {
        super(BookingsPresentationModel::new);
    }

    private ReactiveExpressionFilter filter;
    @Override
    protected void startLogic(BookingsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        filter = createReactiveExpressionFilter("{class: 'Document', fields: 'cart.uuid', where: '!cancelled', orderBy: 'ref desc'}")
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
                .combine(pm.limitProperty(), l -> l ? "{columns: `[" +
                        "'ref'," +
                        "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "'person_age','noteIcon'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_minDeposit', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]`}"
                        : "{columns: `[" +
                        "'ref'," +
                        "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "'person_age','noteIcon'" +
                        "]`}")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> {
                    if (document != null) {
                        Expression cartUuidExpression = getDataSourceModel().getDomainModel().parseExpression("cart.uuid", "Document");
                        Object cartUuid = document.evaluate(cartUuidExpression);
                        if (cartUuid != null)
                            getHistory().push("/book/cart/" + cartUuid);
                    }
                }).start();

        pm.setOnNewBooking(event -> getHistory().push("/book/event/" + pm.getEventId() + "/fees"));
        pm.setOnCloneEvent(event -> getHistory().push("/event/" + pm.getEventId() + "/clone"));
    }
}
