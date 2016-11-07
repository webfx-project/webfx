package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.DaysArray;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.time.TimeSeries;
import naga.commons.util.collection.Collections;
import naga.commons.util.collection.HashList;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.animation.Animation;
import naga.toolkit.animation.KeyFrame;
import naga.toolkit.animation.KeyValue;
import naga.toolkit.animation.Timeline;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.transform.Rotate;
import naga.toolkit.transform.Translate;
import naga.toolkit.util.Properties;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        horizontalDayPositioner = new HorizontalDayPositioner();
        verticalDayPositioner = new VerticalDayTimePositioner();
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

    private interface HorizontalDayPositioned {

        long getEpochDay();

        void setXAndWidth(double x, double width);
    }

    private interface VerticalDayTimePositioned {

        TimeInterval getDayTimeMinuteInterval();

        void setYAndHeight(double y, double height);
    }

    private class HorizontalDayPositioner {

        private final Collection<HorizontalDayPositioned> horizontalDayPositionedCollection = new ArrayList<>();
        private long firstDisplayedEpochDay = -1;
        private long lastDisplayedEpochDay = -1;

        private void initFirstAndLastDisplayedEpochDays() {
            TimeInterval period = calendar.getPeriod().changeTimeUnit(TimeUnit.DAYS);
            firstDisplayedEpochDay = period.getIncludedStart();
            lastDisplayedEpochDay = period.getExcludedEnd() - 1;
        }

        void addHorizontalDayPositioned(HorizontalDayPositioned horizontalDayPositioned) {
            horizontalDayPositionedCollection.add(horizontalDayPositioned);
        }

        long getFirstDisplayedEpochDay() {
            if (firstDisplayedEpochDay == -1)
                initFirstAndLastDisplayedEpochDays();
            return firstDisplayedEpochDay;
        }

        long getLastDisplayedEpochDay() {
            if (lastDisplayedEpochDay == -1)
                initFirstAndLastDisplayedEpochDays();
            return lastDisplayedEpochDay;
        }

        void setTotalWidth(double totalWidth) {
            long totalDisplayedDays = lastDisplayedEpochDay - firstDisplayedEpochDay + 1;
            double dayWidth = totalWidth / totalDisplayedDays;
            for (HorizontalDayPositioned horizontalDayPositioned : horizontalDayPositionedCollection)
                horizontalDayPositioned.setXAndWidth((horizontalDayPositioned.getEpochDay() - firstDisplayedEpochDay) * dayWidth, dayWidth - 1);
        }
    }

    private static final double slotHeight = 35d;
    private final static String fontFamily = "Verdana";
    private final static Font slotFont = Font.font(fontFamily, 13);

    private class VerticalDayTimePositioner {

        private long firstDisplayedMinute = 24 * 60;
        private long lastDisplayedMinute = 0;

        private final Collection<VerticalDayTimePositioned> verticalDayTimePositionedCollection = new ArrayList<>();

        void addVerticalDayTimePositioned(VerticalDayTimePositioned verticalDayTimePositioned) {
            verticalDayTimePositionedCollection.add(verticalDayTimePositioned);
            TimeInterval minuteInterval = verticalDayTimePositioned.getDayTimeMinuteInterval();
            firstDisplayedMinute = Math.min(firstDisplayedMinute, minuteInterval.getIncludedStart());
            lastDisplayedMinute = Math.max(lastDisplayedMinute, minuteInterval.getExcludedEnd());
        }

        void updateVerticalPositions() {
            List<TimeInterval> slots = new HashList<>();
            for (VerticalDayTimePositioned verticalDayTimePositioned : verticalDayTimePositionedCollection)
                slots.add(verticalDayTimePositioned.getDayTimeMinuteInterval());
            slots.sort((i1, i2) -> Long.compare(i1.getIncludedStart(), i2.getIncludedStart()));
            for (VerticalDayTimePositioned verticalDayTimePositioned : verticalDayTimePositionedCollection) {
                TimeInterval minuteInterval = verticalDayTimePositioned.getDayTimeMinuteInterval();
                int slotIndex = slots.indexOf(minuteInterval);
                verticalDayTimePositioned.setYAndHeight(slotIndex * slotHeight, slotHeight - 1);
            }
            drawingNode.setHeight(dayColumnHeaderHeight + slots.size() * slotHeight);
        }
    }

    private void updateTotalWidth(double totalWidth) {
        horizontalDayPositioner.setTotalWidth(totalWidth);
    }

    private Group createCalendarGroup() {
        Group calendarGroup = DrawableFactory.get().createGroup();
        Group headersGroup = createDayColumnHeadersGroup();
        Group bodyGroup = createBodyGroup();
        bodyGroup.getTransforms().setAll(Translate.create(0d, dayColumnHeaderHeight + 1));
        calendarGroup.getDrawableChildren().setAll(headersGroup, bodyGroup);
        return calendarGroup;
    }

    private Group createDayColumnHeadersGroup() {
        Group daysHeadGroup = DrawableFactory.get().createGroup();
        for (long displayedEpochDay = horizontalDayPositioner.getFirstDisplayedEpochDay(); displayedEpochDay <= horizontalDayPositioner.getLastDisplayedEpochDay(); displayedEpochDay++) {
            DayColumnHeaderViewModel model = new DayColumnHeaderViewModel(displayedEpochDay, dayColumnHeaderHeight);
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
        //System.out.println(displayNameProperty.getValue() + " " + epochDay + " " + dayTimeRange.getText());
        for (TimeInterval dayTimeInterval : dayTimeRange.getDayTimeSeries(epochDay, TimeUnit.DAYS).getArray()) {
            destCollection.add(createBlockDrawable(epochDay, dayTimeInterval, timeline));
        }
    }

    private Drawable createBlockDrawable(long epochDay, TimeInterval minuteInterval, CalendarTimeline timeline) {
        DayColumnBodyBlockViewModel model = new DayColumnBodyBlockViewModel(epochDay, minuteInterval, timeline);
        horizontalDayPositioner.addHorizontalDayPositioned(model);
        verticalDayPositioner.addVerticalDayTimePositioned(model);
        return model.group;
    }

    private final static double dayColumnHeaderHeight = 50d;
    private final static LinearGradient dayColumnHeaderFill = LinearGradient.valueOf("from 0% 0% to 0% 100%, 0x75A9A3 0%, 0x375855 100%"); // Color.web("0x609993")
    private final static Color dayColumnHeaderTextColor = Color.WHITE;
    private final static Font dayOfWeekTextFont = Font.font(fontFamily, 11);
    private final static Font dayOfMonthTextFont = Font.font(fontFamily, 20);
    private final static Font monthTextFont = dayOfWeekTextFont;

    private class DayColumnHeaderViewModel implements HorizontalDayPositioned {
        final long epochDay;
        final Rectangle r = DrawableFactory.get().createRectangle();
        final TextShape dayOfWeekText = DrawableFactory.get().createText();
        final TextShape dayOfMonthText = DrawableFactory.get().createText();
        final TextShape monthText = DrawableFactory.get().createText();
        final Group group = DrawableFactory.get().createGroup();
        final Translate translate = Translate.create();

        {
            r.setFill(dayColumnHeaderFill);
/*
        r.setStroke(Color.ORANGE);
        r.setStrokeWidth(5d);
        r.getStrokeDashArray().setAll(25d, 20d, 5d, 20d);
        r.setStrokeLineCap(StrokeLineCap.ROUND);
        r.setStrokeLineJoin(StrokeLineJoin.ROUND);
*/
            dayOfWeekText.setTextAlignment(TextAlignment.CENTER);
            dayOfWeekText.setFont(dayOfWeekTextFont);
            dayOfWeekText.setFill(dayColumnHeaderTextColor);
            dayOfWeekText.setTextOrigin(VPos.TOP);
            dayOfWeekText.setY(3d);
            dayOfMonthText.setTextAlignment(TextAlignment.CENTER);
            dayOfMonthText.setFont(dayOfMonthTextFont);
            dayOfMonthText.setFill(dayColumnHeaderTextColor);
            dayOfMonthText.setTextOrigin(VPos.CENTER);
            monthText.setTextAlignment(TextAlignment.CENTER);
            monthText.setFont(monthTextFont);
            monthText.setFill(dayColumnHeaderTextColor);
            monthText.setTextOrigin(VPos.BOTTOM);
            group.getDrawableChildren().setAll(r, dayOfWeekText, dayOfMonthText, monthText);
            group.getTransforms().setAll(translate);
        }

        DayColumnHeaderViewModel(long epochDay, double height) {
            this.epochDay = epochDay;
            setDate(LocalDate.ofEpochDay(epochDay));
            setHeight(height);
        }

        @Override
        public long getEpochDay() {
            return epochDay;
        }

        void setDate(LocalDate date) {
            i18n.translateText(dayOfWeekText, date.getDayOfWeek().name());
            dayOfMonthText.setText("" + date.getDayOfMonth());
            i18n.translateText(monthText, date.getMonth().name());
        }


        @Override
        public void setXAndWidth(double x, double width) {
            setX(x);
            setWidth(width);
        }

        void setX(double x) {
            translate.setX(x);
        }

        void setWidth(double width) {
            r.setWidth(width);
            dayOfWeekText.setWrappingWidth(width);
            dayOfMonthText.setWrappingWidth(width);
            monthText.setWrappingWidth(width);
        }

        void setHeight(double height) {
            r.setHeight(height);
            dayOfMonthText.setY(height / 2);
            monthText.setY(height - 3d);
        }
    }

    private static class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned {
        final long epochDay;
        final TimeInterval dayTimeMinuteInterval;
        final Rectangle rectangle = DrawableFactory.get().createRectangle();
        final TextShape blockText = DrawableFactory.get().createText();
        final Group group = DrawableFactory.get().createGroup();
        final Translate translate = Translate.create();

        {
            blockText.setFont(slotFont);
            blockText.setTextAlignment(TextAlignment.CENTER);
            blockText.setFill(dayColumnHeaderTextColor);
            blockText.setTextOrigin(VPos.CENTER);
            group.getDrawableChildren().setAll(rectangle, blockText);
            group.getTransforms().setAll(translate);
        }

        private DayColumnBodyBlockViewModel(long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline) {
            this.epochDay = epochDay;
            this.dayTimeMinuteInterval = dayTimeMinuteInterval;
            blockText.textProperty().bind(timeline.displayNameProperty());
            rectangle.setFill(timeline.getTimelineFill());
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
}
