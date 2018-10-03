package mongoose.backend.activities.events;

import mongoose.backend.operations.bookings.RouteToBookingsRequest;
import mongoose.client.activities.generic.MongooseDomainPresentationLogicActivityBase;
import mongoose.shared.entities.Event;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
final class EventsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<EventsPresentationModel>
        implements ReactiveExpressionFilterFactoryMixin {

    EventsPresentationLogicActivity() {
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
        this.<Event>createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where2: 'active', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
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
                .displayResultInto(pm.genericDisplayResultProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), event -> new RouteToBookingsRequest(event, getHistory()).execute())
                .start();
    }
}
