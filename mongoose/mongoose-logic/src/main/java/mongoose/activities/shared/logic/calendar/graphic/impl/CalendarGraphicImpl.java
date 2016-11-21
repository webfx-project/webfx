package mongoose.activities.shared.logic.calendar.graphic.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.commons.util.async.Handler;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.animation.Animation;
import naga.toolkit.animation.KeyFrame;
import naga.toolkit.animation.KeyValue;
import naga.toolkit.animation.Timeline;
import naga.toolkit.drawing.shapes.Node;
import naga.toolkit.drawing.shapes.Group;
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
    private long firstEpochDay;

    public CalendarGraphicImpl(Calendar calendar, I18n i18n) {
        this.i18n = i18n;
        setCalendar(calendar);
    }

    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        firstEpochDay = calendar.getPeriod().getIncludedStart(TimeUnit.DAYS);
        if (drawingNode != null)
            createOrUpdateDrawingNodeCalendar();
    }

    private final Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty() {
        return calendarClickHandlerProperty;
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
        drawingNode.setRootNode(calendarGroup);
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
        Group calendarGroup = Group.create();
        Group headersGroup = createDayColumnHeadersGroup();
        Group bodyGroup = createBodyGroup();
        bodyGroup.getTransforms().setAll(Translate.create(0d, DayColumnHeaderViewModel.dayColumnHeaderHeight + 1));
        calendarGroup.getChildren().setAll(headersGroup, bodyGroup);
        return calendarGroup;
    }

    private Group createDayColumnHeadersGroup() {
        Group daysHeadGroup = Group.create();
        for (long displayedEpochDay = horizontalDayPositioner.getFirstDisplayedEpochDay(); displayedEpochDay <= horizontalDayPositioner.getLastDisplayedEpochDay(); displayedEpochDay++) {
            DayColumnHeaderViewModel model = new DayColumnHeaderViewModel(displayedEpochDay, DayColumnHeaderViewModel.dayColumnHeaderHeight, i18n);
            horizontalDayPositioner.addHorizontalDayPositioned(model);
            daysHeadGroup.getChildren().add(model.group);
        }
        return daysHeadGroup;
    }

    private Group createBodyGroup() {
        Group bodyGroup = Group.create();
        Collections.forEach(calendar.getTimelines(), timeline -> addTimelineNodes(timeline, bodyGroup.getChildren()));
        verticalDayPositioner.updateVerticalPositions();
        return bodyGroup;
    }

    private void addTimelineNodes(CalendarTimeline timeline, Collection<Node> destCollection) {
        Collections.forEach(timeline.getDateTimeRange().changeTimeUnit(TimeUnit.DAYS).getDaysArray(),
                epochDay -> addBlockNodes(epochDay, timeline.getDayTimeRange(), timeline, destCollection));
    }

    private void addBlockNodes(long epochDay, DayTimeRange dayTimeRange, CalendarTimeline timeline, Collection<Node> destCollection) {
        for (TimeInterval dayTimeInterval : dayTimeRange.getDayTimeSeries(epochDay, TimeUnit.DAYS).getArray())
            destCollection.add(createBlockNode(epochDay, dayTimeInterval, timeline));
    }

    private Node createBlockNode(long epochDay, TimeInterval minuteInterval, CalendarTimeline timeline) {
        DayColumnBodyBlockViewModel model = new DayColumnBodyBlockViewModel(this, epochDay, minuteInterval, timeline, epochDay == firstEpochDay);
        horizontalDayPositioner.addHorizontalDayPositioned(model);
        verticalDayPositioner.addVerticalDayTimePositioned(model);
        return model.getGroup();
    }
}
