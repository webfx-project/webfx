package mongoose.activities.shared.logic.calendar.graphic.impl;

import javafx.beans.property.Property;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarCell;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.paint.Color;
import naga.toolkit.fx.scene.paint.Paint;
import naga.toolkit.fx.scene.shape.*;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.scene.transform.Translate;

/**
 * @author Bruno Salmon
 */
class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned, CalendarCell {

    private final static Font textFont = Font.font("Verdana", 13);
    private final static Font timeFont = Font.font("Verdana", 10);
    private final static Paint timeFill = Color.WHITE;

    private final long epochDay;
    private final TimeInterval dayTimeMinuteInterval;
    private final Rectangle rectangle = new Rectangle();
    private final Text blockText = new Text();
    private final Text startTimeText;
    private final Text endTimeText;
    private final Group group = new Group();
    private final Translate translate = Translate.create();

    {
        blockText.setFont(textFont);
        blockText.setTextAlignment(TextAlignment.CENTER);
        blockText.setFill(DayColumnHeaderViewModel.dayColumnHeaderTextColor);
        blockText.setTextOrigin(VPos.CENTER);
        blockText.setMouseTransparent(true);
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
            startTimeText = new Text();
            startTimeText.setFont(timeFont);
            startTimeText.setTextAlignment(TextAlignment.LEFT);
            startTimeText.setFill(timeFill);
            startTimeText.setTextOrigin(VPos.TOP);
            startTimeText.setY(1d);
            startTimeText.setText(dayTimeMinuteInterval.getStartText());
            startTimeText.setMouseTransparent(true);
            endTimeText = new Text();
            endTimeText.setFont(timeFont);
            endTimeText.setTextAlignment(TextAlignment.LEFT);
            endTimeText.setFill(timeFill);
            endTimeText.setTextOrigin(VPos.BOTTOM);
            endTimeText.setText(dayTimeMinuteInterval.getEndText());
            endTimeText.setMouseTransparent(true);
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
