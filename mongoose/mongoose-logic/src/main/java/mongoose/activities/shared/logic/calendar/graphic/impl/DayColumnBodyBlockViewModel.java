package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarCell;
import mongoose.activities.shared.logic.calendar.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.transform.Translate;

/**
 * @author Bruno Salmon
 */
class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned, CalendarCell {

    private final static Font slotFont = Font.font("Verdana", 13);

    private final long epochDay;
    private final TimeInterval dayTimeMinuteInterval;
    private final Rectangle rectangle = DrawableFactory.get().createRectangle();
    private final TextShape blockText = DrawableFactory.get().createText();
    private final Group group = DrawableFactory.get().createGroup();
    private final Translate translate = Translate.create();

    {
        blockText.setFont(slotFont);
        blockText.setTextAlignment(TextAlignment.CENTER);
        blockText.setFill(DayColumnHeaderViewModel.dayColumnHeaderTextColor);
        blockText.setTextOrigin(VPos.CENTER);
        group.getDrawableChildren().setAll(rectangle, blockText);
        group.getTransforms().setAll(translate);
    }

    DayColumnBodyBlockViewModel(Calendar calendar, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline) {
        this.epochDay = epochDay;
        this.dayTimeMinuteInterval = dayTimeMinuteInterval;
        blockText.textProperty().bind(timeline.displayNameProperty());
        rectangle.setFill(timeline.getTimelineFill());
        rectangle.setOnMouseClicked(event -> {
            if (calendar.getCalendarClickHandler() != null)
                calendar.getCalendarClickHandler().handle(new CalendarClickEvent(event, this, timeline));
        });
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
    }

    @Override
    public void setYAndHeight(double y, double height) {
        translate.setY(y);
        rectangle.setHeight(height);
        blockText.setY(height / 2);
    }
}
