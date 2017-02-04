package mongoose.activities.shared.book.event.shared;

import javafx.event.ActionEvent;
import mongoose.activities.shared.generic.eventdependent.EventDependentPresentationLogicActivity;
import mongoose.entities.DateInfo;
import mongoose.entities.Option;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Factory;
import naga.framework.orm.entity.EntityList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class BookingProcessPresentationLogicActivity<PM extends BookingProcessPresentationModel>
    extends EventDependentPresentationLogicActivity<PM> {

    private final String nextPage;

    public BookingProcessPresentationLogicActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
    }

    @Override
    protected void startLogic(PM pm) {
        pm.setOnPreviousAction(this::onPreviousButtonPressed);
        pm.setOnNextAction(this::onNextButtonPressed);
    }

    private void onPreviousButtonPressed(ActionEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent event) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/book/event/" + getEventId() + "/" + page);
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
        return new FeesGroupBuilder(getEventService())
                .setDateInfo(dateInfo)
                .setDefaultOptions(defaultOptions)
                .setAccommodationOptions(accommodationOptions)
                .build();
    }
}
