package mongoose.activities.frontend.event.shared;

import mongoose.entities.DateInfo;
import mongoose.entities.Option;
import mongoose.services.EventService;
import mongoose.services.EventServiceMixin;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Factory;
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.presentation.PresentationActivity;
import naga.toolkit.spi.events.ActionEvent;
import naga.toolkit.spi.nodes.controls.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessActivity<VM extends BookingProcessViewModel, PM extends BookingProcessPresentationModel>
        extends PresentationActivity<VM, PM> implements EventServiceMixin {

    private final String nextPage;

    public BookingProcessActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        Button previousButton = vm.getPreviousButton();
        if (previousButton != null)
            getI18n().translateText(previousButton, "Back").actionEventObservable().subscribe(this::onPreviousButtonPressed);
        Button nextButton = vm.getNextButton();
        if (nextButton != null)
            getI18n().translateText(nextButton, "Next").actionEventObservable().subscribe(this::onNextButtonPressed);
    }

    private void onPreviousButtonPressed(ActionEvent actionEvent) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent actionEvent) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/event/" + getEventId() + "/" + page);
    }

    protected void initializePresentationModel(PM pm) {
        pm.setEventId(getEventId());
    }

    protected void bindPresentationModelWithLogic(PM pm) {
    }

    private Object getEventId() {
        return getParameter("eventId");
    }

    public EventService getEventService() { // Mainly to make EventServiceMixin work
        return EventService.getOrCreate(getEventId(), getDataSourceModel());
    }

    protected Future<FeesGroup[]> onFeesGroup() {
        return onEventOptions().map(this::createFeesGroups);
    }

    private FeesGroup[] createFeesGroups() {
        List<FeesGroup> feesGroups = new ArrayList<>();
        EntityList<DateInfo> dateInfos = getEventDateInfos();
        List<Option> defaultOptions = selectDefaultOptions();
        List<Option> accommodationOptions = selectOptions(o -> o.isConcrete() && o.isAccommodation());
        if (dateInfos.isEmpty())
            populateFeesGroups(null, defaultOptions, accommodationOptions, feesGroups);
        else
            for (DateInfo dateInfo : dateInfos)
                populateFeesGroups(dateInfo, defaultOptions, accommodationOptions, feesGroups);
        return Collections.toArray(feesGroups, FeesGroup[]::new);
    }

    private void populateFeesGroups(DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions, List<FeesGroup> feesGroups) {
        feesGroups.add(createFeesGroup(dateInfo, defaultOptions, accommodationOptions));
    }

    private FeesGroup createFeesGroup(DateInfo dateInfo, List<Option> defaultOptions, List<Option> accommodationOptions) {
        return new FeesGroupBuilder()
                .setDateInfo(dateInfo)
                .setEvent(getEvent())
                .setDefaultOptions(defaultOptions)
                .setAccommodationOptions(accommodationOptions)
                .build();
    }
}
