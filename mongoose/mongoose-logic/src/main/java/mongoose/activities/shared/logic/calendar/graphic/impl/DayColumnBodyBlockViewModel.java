package mongoose.activities.shared.logic.calendar.graphic.impl;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarCell;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.transform.Translate;

/**
 * @author Bruno Salmon
 */
class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned, CalendarCell {

    private final static Font textFont = Font.font("Verdana", 13);
    private final static Font timeFont = Font.font("Verdana", 10);
    private final static Paint timeFill = Color.WHITE;

    private final long epochDay;
    private final TimeInterval dayTimeMinuteInterval;
    private final Rectangle rectangle = Rectangle.create();
    private final TextShape blockText = TextShape.create();
    private final TextShape startTimeText;
    private final TextShape endTimeText;
    private final Group group = Group.create();
    private final Translate translate = Translate.create();

    {
        blockText.setFont(textFont);
        blockText.setTextAlignment(TextAlignment.CENTER);
        blockText.setFill(DayColumnHeaderViewModel.dayColumnHeaderTextColor);
        blockText.setTextOrigin(VPos.CENTER);
        group.getChildren().setAll(rectangle, blockText);
        group.getTransforms().setAll(translate);
    }

    DayColumnBodyBlockViewModel(CalendarGraphic calendarGraphic, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline, boolean displayTimes) {
        this.epochDay = epochDay;
        this.dayTimeMinuteInterval = dayTimeMinuteInterval;
        Property<String> displayNameProperty = timeline.displayNameProperty();
        if (displayNameProperty != null)
            blockText.textProperty().bind(displayNameProperty);
        rectangle.setFill(timeline.getTimelineFill());
        rectangle.setOnMouseClicked(event -> {
            if (calendarGraphic.getCalendarClickHandler() != null)
                calendarGraphic.getCalendarClickHandler().handle(new CalendarClickEvent(event, this, timeline));
        });
        if (displayTimes) {
            startTimeText = TextShape.create();
            startTimeText.setFont(timeFont);
            startTimeText.setTextAlignment(TextAlignment.LEFT);
            startTimeText.setFill(timeFill);
            startTimeText.setTextOrigin(VPos.TOP);
            startTimeText.setY(1d);
            startTimeText.setText(dayTimeMinuteInterval.getStartText());
            endTimeText = TextShape.create();
            endTimeText.setFont(timeFont);
            endTimeText.setTextAlignment(TextAlignment.LEFT);
            endTimeText.setFill(timeFill);
            endTimeText.setTextOrigin(VPos.BOTTOM);
            endTimeText.setText(dayTimeMinuteInterval.getEndText());
            group.getChildren().addAll(startTimeText, endTimeText);
        } else
            startTimeText = endTimeText = null;
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public long getEpochDay() {
        return epochDay;
    }

    @Override
    public TimeInterval getDayTimeMinuteInterval() {
        return dayTimeMinuteInterval;
    }

    @Override
    public void setXAndWidth(double x, double width) {
        translate.setX(x);
        rectangle.setWidth(width);
        blockText.setWrappingWidth(width);
        if (startTimeText != null) {
            startTimeText.setWrappingWidth(width - 2);
            endTimeText.setWrappingWidth(width - 2);
        }
    }

    @Override
    public void setYAndHeight(double y, double height) {
        translate.setY(y);
        rectangle.setHeight(height);
        blockText.setY(height / 2);
        if (endTimeText != null)
            endTimeText.setY(height - 1d);
    }
}
