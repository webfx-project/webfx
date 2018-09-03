package mongooses.core.activities.backend.cloneevent;

import mongooses.core.operations.bothends.route.RouteToBookingsRequest;
import mongooses.core.activities.sharedends.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongooses.core.entities.Event;
import webfx.fxkits.core.properties.Properties;
import webfx.fxkits.core.spi.FxKit;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateService;

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
                    FxKit.get().scheduler().runInUiThread(() ->
                        new RouteToBookingsRequest(ar.result().getGeneratedKeys()[0], getHistory()).execute()
                );
            });
        });
    }
}
