package mongoose.backend.activities.events;

import mongoose.backend.operations.routes.bookings.RouteToBookingsRequest;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import mongoose.shared.entities.Event;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilterFactoryMixin;
import webfx.platform.shared.util.function.Factory;

import static webfx.framework.client.orm.dql.DqlStatement.limit;
import static webfx.framework.client.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class EventsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<EventsPresentationModel>
        implements ReactiveVisualFilterFactoryMixin {

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
        // Setting up the reactive filter
        this.<Event>createReactiveVisualFilter("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where2: 'active', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                .combineIfNotNull(pm.organizationIdProperty(), o -> where("organization=?", o))
                // Limit condition
                .combineIfPositive(pm.limitProperty(), l -> limit("?", l))
                // With bookings condition
                //.combineIfTrue(pm.withBookingsProperty(), "{where: '(select count(1) from Document where !cancelled and event=e) > 0'}")
                .setEntityColumns("[" +
                        //"{label: 'Image', expression: 'image(`images/calendar.svg`)'}," +
                        //"{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + bookingsCount + `)`'}" +
                        "{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate)`'}," +
                        "'type'," +
                        "{role: 'background', expression: 'type.background'}" +
                        "]")
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setSelectedEntityHandler(pm.genericVisualSelectionProperty(), event -> new RouteToBookingsRequest(event, getHistory()).execute())
                .start();
    }
}
