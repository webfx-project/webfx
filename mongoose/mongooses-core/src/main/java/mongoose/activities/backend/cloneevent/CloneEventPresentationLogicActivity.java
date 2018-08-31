package mongoose.activities.backend.cloneevent;

import mongoose.operations.bothends.route.RouteToBookingsRequest;
import mongoose.activities.bothends.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.entities.Event;
import webfx.fx.properties.Properties;
import webfx.fx.spi.Toolkit;
import webfx.platform.services.update.UpdateArgument;
import webfx.platform.services.update.UpdateService;

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
                if (ar.succeeded())
                    Toolkit.get().scheduler().runInUiThread(() ->
                        new RouteToBookingsRequest(ar.result().getGeneratedKeys()[0], getHistory()).execute()
                );
            });
        });
    }
}
