package mongoose.backend.activities.cloneevent;

import mongoose.backend.operations.routes.bookings.RouteToBookingsRequest;
import mongoose.client.activity.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.shared.entities.Event;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitService;

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
            SubmitService.executeSubmit(SubmitArgument.builder()
                    .setStatement("select copy_event(?,?,?)")
                    .setParameters(getEventId(), pm.getName(), startDate)
                    .setReturnGeneratedKeys(true)
                    .setDataSourceId(getDataSourceId())
                    .build()).setHandler(ar -> {
                if (ar.succeeded())
                    UiScheduler.runInUiThread(() ->
                            new RouteToBookingsRequest(ar.result().getGeneratedKeys()[0], getHistory()).execute()
                    );
            });
        });
    }
}
