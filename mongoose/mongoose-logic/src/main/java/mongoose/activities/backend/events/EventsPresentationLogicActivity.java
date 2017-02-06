package mongoose.activities.backend.events;

import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public class EventsPresentationLogicActivity extends DomainPresentationLogicActivityImpl<EventsPresentationModel> {

    public EventsPresentationLogicActivity() {
        this(EventsPresentationModel::new);
    }

    public EventsPresentationLogicActivity(Factory<EventsPresentationModel> presentationModelFactory) {
        super(presentationModelFactory);
    }

    @Override
    protected void updatePresentationModelFromRouteParameters(EventsPresentationModel pm) {
        pm.setOrganizationId(getParameter("organizationId"));
    }

    @Override
    protected void startLogic(EventsPresentationModel pm) {
        // Loading the domain model and setting up the reactive filter
        createReactiveExpressionFilter("{class: 'Event', alias: 'e', fields2: '(select count(1) from Document where !cancelled and event=e) as bookingsCount', where: 'active', orderBy: 'startDate desc,id desc'}")
                // Search box condition
                .combine(pm.searchTextProperty(), s -> s == null ? null : "{where: 'lower(name) like `%" + s.toLowerCase() + "%`'}")
                .combine(pm.organizationIdProperty(), o -> o == null ? null : "{where: 'organization=" + o + "'}")
                // Limit condition
                .combine(pm.limitProperty(), "{limit: '100'}")
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
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), event -> {
                    if (event != null)
                        getHistory().push("/event/" + event.getPrimaryKey() + "/bookings");
                }).start();
    }
}
