package mongooses.core.backend.activities.options;

import javafx.scene.Node;
import mongooses.core.sharedends.activities.shared.BookingCalendar;
import mongooses.core.sharedends.logic.calendar.CalendarTimeline;
import mongooses.core.sharedends.logic.ui.calendargraphic.CalendarClickEvent;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocumentLine;
import mongooses.core.shared.entities.Option;
import webfx.framework.orm.entity.UpdateStore;

/**
 * @author Bruno Salmon
 */
public class EditableBookingCalendar extends BookingCalendar {

    private final Node parentOwner;
    private boolean editMode;

    public EditableBookingCalendar(boolean amendable, Node parentOwner) {
        super(amendable);
        this.parentOwner = parentOwner;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Override
    protected void onCalendarClick(CalendarClickEvent event) {
        if (!isEditMode())
            super.onCalendarClick(event);
        else {
            CalendarTimeline calendarTimeline = event.getCalendarTimeline();
            Option option = getCalendarTimelineOption(calendarTimeline);
            if (option != null) {
                DayTimeRangeEditor.showDayTimeRangeEditorDialog(calendarTimeline.getDayTimeRange(),
                        event.getCalendarCell().getEpochDay(),
                        calendarTimeline,
                        (newDayTimeRange, dialogCallback) -> {
                            // Creating an update store
                            UpdateStore store = UpdateStore.create(workingDocument.getEventAggregate().getDataSourceModel());
                            // Creating an instance of Option entity
                            Option updatingOption = store.updateEntity(option);
                            // Updating the option time range
                            updatingOption.setTimeRange(newDayTimeRange.getText());
                            // Asking the update record this change in the database
                            store.executeUpdate().setHandler(asyncResult -> {
                                if (asyncResult.failed())
                                    dialogCallback.showException(asyncResult.cause());
                                else {
                                    dialogCallback.closeDialog();
                                    // Updating the UI
                                    option.setTimeRange(newDayTimeRange.getText());
                                    createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, true);
                                }
                            });
                        }, parentOwner
                );
            }
        }
    }

    private static Option getCalendarTimelineOption(CalendarTimeline calendarTimeline) {
        Object source = calendarTimeline.getSource();
        if (source instanceof Option)
            return (Option) source;
        if (source instanceof WorkingDocumentLine)
            return ((WorkingDocumentLine) source).getOption();
        return null;
    }

}
