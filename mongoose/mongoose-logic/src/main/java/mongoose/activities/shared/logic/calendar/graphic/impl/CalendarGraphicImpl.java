package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.time.TimeSeries;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.animation.Animation;
import naga.toolkit.animation.KeyFrame;
import naga.toolkit.animation.KeyValue;
import naga.toolkit.animation.Timeline;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Translate;
import naga.toolkit.util.Properties;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class CalendarGraphicImpl implements CalendarGraphic {

    private Calendar calendar;
    private final I18n i18n;
    private DrawingNode drawingNode;

    public CalendarGraphicImpl(Calendar calendar, I18n i18n) {
        this.calendar = calendar;
        this.i18n = i18n;
    }

    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        if (drawingNode != null)
            createOrUpdateDrawingNodeCalendar();
    }

    @Override
    public DrawingNode getDrawingNode() {
        if (drawingNode == null)
            createDrawingNode();
        return drawingNode;
    }

    private void createDrawingNode() {
        drawingNode = Toolkit.get().createDrawingNode();
        drawingNode.widthProperty().addListener((observable, oldValue, newWidth) -> updateTotalWidth(newWidth));
        createOrUpdateDrawingNodeCalendar();
    }

    private HorizontalDayPositioner horizontalDayPositioner;
    private VerticalDayTimePositioner verticalDayPositioner;

    private void createOrUpdateDrawingNodeCalendar() {
        horizontalDayPositioner = new HorizontalDayPositioner(calendar);
        verticalDayPositioner = new VerticalDayTimePositioner(drawingNode.heightProperty());
        Group calendarGroup = createCalendarGroup();
        drawingNode.getDrawableChildren().setAll(calendarGroup);
        updateTotalWidth(drawingNode.getWidth());
        Rotate rotate = null; // Rotate.create();
        if (rotate != null) {
            calendarGroup.getTransforms().setAll(rotate);
            Properties.runOnPropertiesChange(arg -> {
                rotate.setPivotX(drawingNode.getWidth() / 2);
                rotate.setPivotY(drawingNode.getHeight() / 2);
            }, drawingNode.widthProperty(), drawingNode.heightProperty());
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.ofSeconds(5), new KeyValue(rotate.angleProperty(), 360d)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    private void updateTotalWidth(double totalWidth) {
        horizontalDayPositioner.setTotalWidth(totalWidth);
    }

    private Group createCalendarGroup() {
        Group calendarGroup = DrawableFactory.get().createGroup();
        Group headersGroup = createDayColumnHeadersGroup();
        Group bodyGroup = createBodyGroup();
        bodyGroup.getTransforms().setAll(Translate.create(0d, DayColumnHeaderViewModel.dayColumnHeaderHeight + 1));
        calendarGroup.getDrawableChildren().setAll(headersGroup, bodyGroup);
        return calendarGroup;
    }

    private Group createDayColumnHeadersGroup() {
        Group daysHeadGroup = DrawableFactory.get().createGroup();
        for (long displayedEpochDay = horizontalDayPositioner.getFirstDisplayedEpochDay(); displayedEpochDay <= horizontalDayPositioner.getLastDisplayedEpochDay(); displayedEpochDay++) {
            DayColumnHeaderViewModel model = new DayColumnHeaderViewModel(displayedEpochDay, DayColumnHeaderViewModel.dayColumnHeaderHeight, i18n);
            horizontalDayPositioner.addHorizontalDayPositioned(model);
            daysHeadGroup.getDrawableChildren().add(model.group);
        }
        return daysHeadGroup;
    }

    private Group createBodyGroup() {
        Group bodyGroup = DrawableFactory.get().createGroup();
        Collections.forEach(calendar.getTimelines(), timeline -> addTimelineDrawables(timeline, bodyGroup.getDrawableChildren()));
        verticalDayPositioner.updateVerticalPositions();
        return bodyGroup;
    }

    private void addTimelineDrawables(CalendarTimeline timeline, Collection<Drawable> destCollection) {
        DayTimeRange dayTimeRange = timeline.getDayTimeRange();
        TimeSeries series = timeline.getDateTimeRange().getSeries().intersect(dayTimeRange);
        DaysArray daysArray = series.toDaysArray(dayTimeRange).changeTimeUnit(TimeUnit.DAYS);
        Collections.forEach(daysArray, epochDay -> addBlockDrawables(epochDay, dayTimeRange, timeline, destCollection));
    }

    private void addBlockDrawables(long epochDay, DayTimeRange dayTimeRange, CalendarTimeline timeline, Collection<Drawable> destCollection) {
        for (TimeInterval dayTimeInterval : dayTimeRange.getDayTimeSeries(epochDay, TimeUnit.DAYS).getArray()) {
            destCollection.add(createBlockDrawable(epochDay, dayTimeInterval, timeline));
        }
    }

    private Drawable createBlockDrawable(long epochDay, TimeInterval minuteInterval, CalendarTimeline timeline) {
        DayColumnBodyBlockViewModel model = new DayColumnBodyBlockViewModel(calendar, epochDay, minuteInterval, timeline);
        horizontalDayPositioner.addHorizontalDayPositioned(model);
        verticalDayPositioner.addVerticalDayTimePositioned(model);
        return model.getGroup();
    }
}
