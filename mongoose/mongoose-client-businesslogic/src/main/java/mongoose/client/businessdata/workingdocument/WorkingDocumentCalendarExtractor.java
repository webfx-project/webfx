package mongoose.client.businessdata.workingdocument;

import javafx.beans.property.Property;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import mongoose.client.businesslogic.option.OptionLogic;
import mongoose.client.businessdata.calendar.Calendar;
import mongoose.client.businessdata.calendar.CalendarTimeline;
import mongoose.client.businessdata.calendar.impl.CalendarImpl;
import mongoose.client.businessdata.calendar.impl.CalendarTimelineImpl;
import mongoose.shared.entities.ItemFamilyType;
import mongoose.shared.entities.Label;
import mongoose.shared.entities.Option;
import mongoose.shared.entities.markers.HasItemFamilyType;
import mongoose.shared.businessdata.time.DateTimeRange;
import mongoose.shared.businessdata.time.DayTimeRange;
import mongoose.client.entities.util.Labels;
import webfx.platform.shared.util.Objects;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class WorkingDocumentCalendarExtractor {

    private final static Paint TEACHING_FILL = Color.web("0xF5A463");
    private final static Paint ACCOMMODATION_FILL = Color.web("0x484A61");
    private final static Paint MEALS_FILL = Color.web("0xA44F5F");
    private final static Paint TRANSPORT_FILL = Color.web("0x8CA76A");
    private final static Paint UNATTENDED_FILL = Color.DARKGRAY;
    private final static Paint NOTHING_FILL = Color.LIGHTGRAY;

    public static Calendar extractCalendar(WorkingDocument wd) {
        return extractCalendar(wd, null);
    }

    public static Calendar extractCalendar(WorkingDocument wd, WorkingDocument maxWd) {
        Map<Object, OptionTimeline> optionTimelines = new HashMap<>();
        // Gathering options coming from document lines
        addWorkingDocumentIntoOptionTimelines(wd, false, optionTimelines);
        // And those coming from options preselection
        addWorkingDocumentIntoOptionTimelines(maxWd, true, optionTimelines);
        // Generating calendar timelines from option timelines
        Collection<CalendarTimeline> timelines = new ArrayList<>();
        DateTimeRange calendarDateTimeRange = Objects.coalesce(maxWd, wd).getDateTimeRange().changeTimeUnit(TimeUnit.DAYS);
        for (OptionTimeline ot : optionTimelines.values())
            timelines.add(new CalendarTimelineImpl(calendarDateTimeRange, ot.option.getParsedTimeRangeOrParent(), null, NOTHING_FILL, ot.option));
        for (OptionTimeline ot : optionTimelines.values())
            ot.addToCalendarTimelines(timelines, calendarDateTimeRange);
        return new CalendarImpl(calendarDateTimeRange.getInterval(), timelines);
    }

    private static void addWorkingDocumentIntoOptionTimelines(WorkingDocument wd, boolean isMax, Map<Object, OptionTimeline> optionTimelines) {
        if (wd != null)
            for (WorkingDocumentLine wdl : wd.getWorkingDocumentLines()) {
                Option option = wdl.getOption();
                if (OptionLogic.isOptionDisplayableOnCalendar(option, isMax)) {
                    OptionTimeline optionTimeline = optionTimelines.get(option.getPrimaryKey());
                    if (optionTimeline != null)
                        optionTimeline.addWorkingDocumentLine(wdl, isMax);
                    else
                        optionTimelines.put(option.getPrimaryKey(), new OptionTimeline(option, wdl, isMax));
                }
            }
    }

    private static Paint getItemFamilyFillColor(HasItemFamilyType hasItemFamilyType) {
        return getItemFamilyFillColor(hasItemFamilyType.getItemFamilyType());
    }

    private static Paint getItemFamilyFillColor(ItemFamilyType itemFamilyType) {
        switch (itemFamilyType) {
            case TEACHING: return TEACHING_FILL;
            case ACCOMMODATION: return ACCOMMODATION_FILL;
            case MEALS: return MEALS_FILL;
            case TRANSPORT: return TRANSPORT_FILL;
        }
        return null;
    }

    private static final class OptionTimeline {
        final Option option;
        final List<WorkingDocumentLine> workingDocumentLines = new ArrayList<>(); // List because possible multi-lines with same option (ex: early breakfast and festival breakfast)
        WorkingDocumentLine maxWorkingDocumentLine;

        OptionTimeline(Option option, WorkingDocumentLine workingDocumentLine, boolean isMax) {
            this.option = option;
            addWorkingDocumentLine(workingDocumentLine, isMax);
        }

        void addWorkingDocumentLine(WorkingDocumentLine wdl, boolean isMax) {
            if (isMax)
                maxWorkingDocumentLine = wdl;
            else
                workingDocumentLines.add(wdl);
        }

        void addToCalendarTimelines(Collection<CalendarTimeline> timelines, DateTimeRange calendarDateTimeRange) {
            DayTimeRange dayTimeRange = option.getParsedTimeRangeOrParent();
            //timelines.add(new CalendarTimelineImpl(calendarDateTimeRange, dayTimeRange, null, NOTHING_FILL));
            Label label = Labels.bestLabelOrName(!option.isAccommodation() ? option : option.getParent() /* normally: night */);
            Property<String> displayNameProperty = Labels.translateLabel(label);
            addWorkingDocumentLineToCalendarTimelines(timelines, maxWorkingDocumentLine, dayTimeRange, displayNameProperty);
            for (WorkingDocumentLine workingDocumentLine : workingDocumentLines)
                addWorkingDocumentLineToCalendarTimelines(timelines, workingDocumentLine, dayTimeRange, displayNameProperty);
        }

        private void addWorkingDocumentLineToCalendarTimelines(Collection<CalendarTimeline> timelines, WorkingDocumentLine workingDocumentLine, DayTimeRange dayTimeRange, Property<String> displayNameProperty) {
            if (workingDocumentLine != null) {
                DateTimeRange dateTimeRange = new DateTimeRange(workingDocumentLine.getDaysArray());
                if (!dateTimeRange.isEmpty()) {
                    Paint fill = workingDocumentLine == maxWorkingDocumentLine ? UNATTENDED_FILL : Objects.coalesce(getItemFamilyFillColor(option), UNATTENDED_FILL);
                    timelines.add(new CalendarTimelineImpl(dateTimeRange, dayTimeRange, displayNameProperty, fill, workingDocumentLine));
                }
            }
        }
    }
}
