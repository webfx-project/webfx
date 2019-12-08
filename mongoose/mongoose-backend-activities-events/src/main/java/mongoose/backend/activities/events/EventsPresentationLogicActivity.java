package mongoose.backend.activities.events;

import mongoose.backend.operations.routes.bookings.RouteToBookingsRequest;
import mongoose.client.activity.MongooseDomainPresentationLogicActivityBase;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.platform.shared.util.function.Factory;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
final class EventsPresentationLogicActivity
        extends MongooseDomainPresentationLogicActivityBase<EventsPresentationModel> {

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
        ReactiveVisualMapper.createPushReactiveChain(this)
                .always("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where2: 'active', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .ifTrimNotEmpty(pm.searchTextProperty(), s -> where("lower(name) like ?", "%" + s.toLowerCase() + "%"))
                .ifNotNull(pm.organizationIdProperty(), o -> where("organization=?", o))
                // Limit condition
                .ifPositive(pm.limitProperty(), l -> limit("?", l))
                .setEntityColumns("[" +
                        //"{label: 'Image', expression: 'image(`images/calendar.svg`)'}," +
                        //"{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate) + ` (` + bookingsCount + `)`'}" +
                        "{label: 'Event', expression: 'icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate)`'}," +
                        "'type'," +
                        "{role: 'background', expression: 'type.background'}" +
                        "]")
                .visualizeResultInto(pm.genericVisualResultProperty())
                .setVisualSelectionProperty(pm.genericVisualSelectionProperty())
                .setSelectedEntityHandler(event -> new RouteToBookingsRequest(event, getHistory()).execute())
                .start();
    }
}
