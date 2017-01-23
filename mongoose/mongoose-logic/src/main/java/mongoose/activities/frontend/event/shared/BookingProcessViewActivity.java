package mongoose.activities.frontend.event.shared;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.EventDependentViewDomainActivity;
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
import naga.framework.orm.entity.EntityList;
import naga.framework.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessViewActivity extends EventDependentViewDomainActivity {

    private final String nextPage;

    protected Button previousButton;
    protected Button nextButton;

    public BookingProcessViewActivity(String nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public Node buildUi() {
        createViewNodes();
        return assemblyViewNodes();
    }

    protected void createViewNodes() {
        I18n i18n = getI18n();
        if (previousButton == null)
            previousButton = i18n.translateText(new Button(), "Back");
        if (nextButton == null)
            nextButton = i18n.translateText(new Button(), "Next");
        previousButton.setOnAction(this::onPreviousButtonPressed);
        nextButton.setOnAction(this::onNextButtonPressed);
    }

    protected Node assemblyViewNodes() {
        return new BorderPane(null, null, null, new HBox(previousButton, nextButton), null);
    }

    private void onPreviousButtonPressed(ActionEvent event) {
        getHistory().goBack();
    }

    protected void onNextButtonPressed(ActionEvent event) {
        goToNextBookingProcessPage(nextPage);
    }

    protected void goToNextBookingProcessPage(String page) {
        getHistory().push("/event/" + getEventId() + "/" + page);
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
        return createNewDateTimeRangeWorkingDocument(getEvent().computeMaxDateTimeRange());
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
