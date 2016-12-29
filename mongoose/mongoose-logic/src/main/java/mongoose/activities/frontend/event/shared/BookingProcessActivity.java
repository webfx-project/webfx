package mongoose.activities.frontend.event.shared;

import mongoose.activities.shared.generic.EventDependentActivity;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.DateInfo;
import mongoose.entities.Option;
import naga.commons.util.async.Future;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Factory;
import naga.framework.orm.entity.EntityList;
import naga.fx.scene.control.Button;
import naga.fx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessActivity
        <VM extends BookingProcessViewModel, PM extends BookingProcessPresentationModel>

        extends EventDependentActivity<VM, PM> {

    private final String nextPage;

    public BookingProcessActivity(Factory<PM> presentationModelFactory, String nextPage) {
        super(presentationModelFactory);
        this.nextPage = nextPage;
    }

    protected void bindViewModelWithPresentationModel(VM vm, PM pm) {
        Button previousButton = vm.getPreviousButton();
        if (previousButton != null)
            getI18n().translateText(previousButton, "Back").setOnMouseClicked(this::onPreviousButtonPressed);
        Button nextButton = vm.getNextButton();
        if (nextButton != null)
            getI18n().translateText(nextButton, "Next").setOnMouseClicked(this::onNextButtonPressed);
    }

    private void onPreviousButtonPressed(MouseEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(MouseEvent event) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/event/" + getEventId() + "/" + page);
    }

    protected void bindPresentationModelWithLogic(PM pm) {
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

    protected WorkingDocument createNewDateTimeRangeWorkingDocument(DateTimeRange workingDocumentDateTimeRange) {
        OptionsPreselection selectedOptionsPreselection = getSelectedOptionsPreselection();
        return selectedOptionsPreselection == null ? null : selectedOptionsPreselection.createNewWorkingDocument(workingDocumentDateTimeRange).applyBusinessRules();
    }

    protected WorkingDocument createNewMaxDateTimeRangeWorkingDocument() {
        return createNewDateTimeRangeWorkingDocument(getEventMaxDateTimeRange());
    }

    protected CalendarGraphic createOrUpdateCalendarGraphicFromOptionsPreselection(OptionsPreselection optionsPreselection, CalendarGraphic calendarGraphic) {
        return createOrUpdateCalendarGraphicFromWorkingDocument(optionsPreselection.getWorkingDocument(), calendarGraphic);
    }

    protected CalendarGraphic createOrUpdateCalendarGraphicFromWorkingDocument(WorkingDocument workingDocument, CalendarGraphic calendarGraphic) {
        Calendar calendar = createCalendarFromWorkingDocument(workingDocument);
        if (calendarGraphic == null)
            calendarGraphic = CalendarGraphic.create(calendar, getI18n());
        else
            calendarGraphic.setCalendar(calendar);
        return calendarGraphic;
    }

    private Calendar createCalendarFromWorkingDocument(WorkingDocument workingDocument) {
        return CalendarExtractor.createFromWorkingDocument(workingDocument, createNewMaxDateTimeRangeWorkingDocument(), getI18n());
    }

}
