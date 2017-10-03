package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.services.EventService;
import naga.commons.util.Strings;
import mongoose.domainmodel.functions.AbcNames;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.platform.spi.Platform;

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
                .combine("{columns: `[" +
                        "'ref'," +
                        "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
                        "'person_firstName'," +
                        "'person_lastName'," +
                        "'person_age','noteIcon'," +
                        "{expression: 'price_net', format: 'price'}," +
                        "{expression: 'price_minDeposit', format: 'price'}," +
                        "{expression: 'price_deposit', format: 'price'}," +
                        "{expression: 'price_balance', format: 'price'}" +
                        "]`}")
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
                .combine(pm.limitProperty(), l -> l.intValue() < 0 ? null : "{limit: '" + l + "'}")
                .applyDomainModelRowStyle()
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> {
                    if (document != null) {
                        EventService eventService = getEventService();
                        WorkingDocument.load(eventService, document.getPrimaryKey()).setHandler(ar -> {
                            if (ar.failed())
                                Platform.log("Error loading document", ar.cause());
                            else {
                                eventService.setSelectedOptionsPreselection(null);
                                eventService.setWorkingDocument(ar.result());
                                getHistory().push("/book/event/" + pm.getEventId() + "/options");
                            }
                        });

                        /*
                        Expression cartUuidExpression = getDataSourceModel().getDomainModel().parseExpression("cart.uuid", "Document");
                        Object cartUuid = document.evaluate(cartUuidExpression);
                        if (cartUuid != null)
                            getHistory().push("/book/cart/" + cartUuid);
*/
                    }
                }).start();

        pm.setOnNewBooking(event -> {
            getEventService().setCurrentCart(null);
            getHistory().push("/book/event/" + pm.getEventId() + "/fees");
        });
        pm.setOnCloneEvent(event -> getHistory().push("/event/" + pm.getEventId() + "/clone"));
    }

    @Override
    protected void refreshDataOnActive() {
        filter.refreshWhenActive();
    }
}
