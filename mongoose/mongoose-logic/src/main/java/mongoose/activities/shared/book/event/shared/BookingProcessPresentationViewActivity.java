package mongoose.activities.shared.book.event.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import mongoose.activities.shared.generic.MongooseSectionFactoryMixin;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarExtractor;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarGraphic;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Event;
import mongoose.services.EventService;
import naga.util.Objects;
import naga.framework.activity.presentation.view.impl.PresentationViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class BookingProcessPresentationViewActivity<PM extends BookingProcessPresentationModel>
        extends PresentationViewActivityImpl<PM>
        implements MongooseButtonFactoryMixin, MongooseSectionFactoryMixin {

    protected Button previousButton;
    protected Button nextButton;

    @Override
    protected void createViewNodes(PM pm) {
        if (previousButton == null)
            previousButton = newButton("<<Back");
        if (nextButton == null)
            nextButton = newButton("Next>>");
        previousButton.onActionProperty().bind(pm.onPreviousActionProperty());
        nextButton.onActionProperty().bind(pm.onNextActionProperty());
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(null, null, null, new HBox(previousButton, nextButton), null);
    }

    protected CalendarGraphic createOrUpdateCalendarGraphicFromOptionsPreselection(OptionsPreselection optionsPreselection, CalendarGraphic calendarGraphic, EventService eventService) {
        return createOrUpdateCalendarGraphicFromWorkingDocument(optionsPreselection.getWorkingDocument(), calendarGraphic, eventService);
    }

    protected CalendarGraphic createOrUpdateCalendarGraphicFromWorkingDocument(WorkingDocument workingDocument, CalendarGraphic calendarGraphic, EventService eventService) {
        Calendar calendar = createCalendarFromWorkingDocument(workingDocument, eventService);
        if (calendarGraphic == null)
            calendarGraphic = CalendarGraphic.create(calendar, getI18n());
        else
            calendarGraphic.setCalendar(calendar);
        return calendarGraphic;
    }

    private Calendar createCalendarFromWorkingDocument(WorkingDocument workingDocument, EventService eventService) {
        return CalendarExtractor.createFromWorkingDocument(workingDocument, createNewMaxDateTimeRangeWorkingDocument(eventService), getI18n());
    }

    protected WorkingDocument createNewDateTimeRangeWorkingDocument(DateTimeRange workingDocumentDateTimeRange, EventService eventService) {
        OptionsPreselection selectedOptionsPreselection = eventService.getSelectedOptionsPreselection();
        return selectedOptionsPreselection == null ? null : selectedOptionsPreselection.createNewWorkingDocument(workingDocumentDateTimeRange).applyBusinessRules();
    }

    protected WorkingDocument createNewMaxDateTimeRangeWorkingDocument(EventService eventService) {
        return createNewDateTimeRangeWorkingDocument(getEventMaxDateTimeRange(eventService.getEvent()), eventService);
    }

    protected DateTimeRange getEventMaxDateTimeRange(Event event) {
        return Objects.coalesce(event.getParsedMaxDateTimeRange(), event.getParsedDateTimeRange());
    }
}
