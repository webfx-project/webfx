package mongoose.activities.backend.event.bookings;

import mongoose.activities.backend.event.clone.CloneEventRouting;
import mongoose.activities.shared.book.event.fees.FeesRooting;
import mongoose.activities.shared.book.event.options.OptionsRooting;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.activities.shared.logic.work.sync.WorkingDocumentLoader;
import mongoose.domainmodel.functions.AbcNames;
import mongoose.entities.Document;
import mongoose.services.EventService;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.platform.services.log.spi.Logger;

/**
 * @author Bruno Salmon
 */
class BookingsPresentationLogicActivity
        extends EventDependentPresentationLogicActivity<BookingsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    BookingsPresentationLogicActivity() {
        super(BookingsPresentationModel::new);
    }

    private ReactiveExpressionFilter<Document> filter;
    @Override
    protected void startLogic(BookingsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        filter = this.<Document>createReactiveExpressionFilter("{class: 'Document', fields: 'cart.uuid', where: '!cancelled', orderBy: 'ref desc'}")
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
            .combineIfNotNull(pm.eventIdProperty(), s -> "{where: 'event=" + s + "'}")
            // Search box condition
            .combineTrimIfNotEmpty(pm.searchTextProperty(), s ->
                Character.isDigit(s.charAt(0)) ? "{where: 'ref = " + s + "'}"
                : s.contains("@") ? "{where: 'lower(person_email) like `%" + s.toLowerCase() + "%`'}"
                : "{where: 'person_abcNames like `" + AbcNames.evaluate(s, true) + "`'}")
            // Limit condition
            .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
            .applyDomainModelRowStyle()
            .displayResultSetInto(pm.genericDisplayResultSetProperty())
            .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> {
                if (document != null) {
                    EventService eventService = getEventService();
                    WorkingDocumentLoader.load(eventService, document.getPrimaryKey()).setHandler(ar -> {
                        if (ar.failed())
                            Logger.log("Error loading document", ar.cause());
                        else {
                            eventService.setSelectedOptionsPreselection(null);
                            eventService.setWorkingDocument(ar.result());
                            OptionsRooting.routeUsingEventId(pm.getEventId(), getHistory());
                        }
                    });
                    //CartRooting.route(document, getHistory());

                }
            }).start();

        pm.setOnNewBooking(event -> {
            getEventService().setCurrentCart(null);
            FeesRooting.routeUsingEventId(pm.getEventId(), getHistory());
        });
        pm.setOnCloneEvent(event -> CloneEventRouting.routeUsingEventId(pm.getEventId(), getHistory()));
    }

    @Override
    protected void refreshDataOnActive() {
        filter.refreshWhenActive();
    }
}
