package mongoose.backend.activities.cloneevent;

import mongoose.client.activities.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.shared.entities.Event;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateService;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public final class CloneEventPresentationLogicActivity extends EventDependentPresentationLogicActivity<CloneEventPresentationModel> {

    public CloneEventPresentationLogicActivity() {
        super(CloneEventPresentationModel::new);
    }

    @Override
    protected void startLogic(CloneEventPresentationModel pm) {
        // Load and display fees groups now but also on event change
        Properties.runNowAndOnPropertiesChange(() -> {
            pm.setName(null);
            pm.setDate(null);
            onEventOptions().setHandler(ar -> {
                if (ar.succeeded()) {
                    Event event = getEvent();
                    pm.setName(event.getName());
                    pm.setDate(event.getStartDate());
                }
            });
        }, pm.eventIdProperty());

        pm.setOnSubmit(event -> {
            LocalDate startDate = pm.getDate();
            UpdateService.executeUpdate(new UpdateArgument("select copy_event(?,?,?)", new Object[]{getEventId(), pm.getName(), startDate}, true, getDataSourceId())).setHandler(ar -> {
/* Temporary commented as this causes a cyclic dependency with bookings activity (TODO fix this)
                if (ar.succeeded())
                    UiScheduler.runInUiThread(() ->
                        new RouteToBookingsRequest(ar.result().getGeneratedKeys()[0], getHistory()).execute()
                );
*/
            });
        });
    }
}
