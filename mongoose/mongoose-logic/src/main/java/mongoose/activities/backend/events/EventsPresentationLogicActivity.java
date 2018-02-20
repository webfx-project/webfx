package mongoose.activities.backend.events;

import mongoose.activities.backend.event.bookings.BookingsRouting;
import mongoose.activities.shared.generic.MongooseDomainPresentationLogicActivityBase;
import naga.framework.ui.filter.ReactiveExpressionFilterFactoryMixin;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
class EventsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<EventsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    public EventsPresentationLogicActivity() {
        this(EventsPresentationModel::new);
    }

    private EventsPresentationLogicActivity(Factory<EventsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void updatePresentationModelFromContextParameters(EventsPresentationModel pm) {
        pm.setOrganizationId(getParameter("organizationId"));
    }

    @Override
    protected void startLogic(EventsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where2: 'active', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combineTrimIfNotEmpty(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                .combineIfNotNull(pm.organizationIdProperty(), o -> "{where: 'organization=" + o + "'}")
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> "{limit: '" + l + "'}")
                // With bookings condition
                //.combine(pm.withBookingsProperty(), "{where: '(select count(1) from Document where !cancelled and event=e) > 0'}")
                .setExpressionColumns("[" +
                        //"{label: 'Image', expression: 'image(`images/calendar.svg`)'}," +
                        //"{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + bookingsCount + `)`'}" +
                        "{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate)`'}," +
                        "'type'," +
                        "{role: 'background', expression: 'type.background'}" +
                        "]")
                .displayResultSetInto(pm.genericDisplayResultSetProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), event -> BookingsRouting.routeUsingEvent(event, getHistory()))
                .start();
    }
}
