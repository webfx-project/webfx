package mongoose.activities.shared.logic.ui.calendargraphic.impl;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarClickEvent;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarGraphic;
import naga.commons.util.async.Handler;
import naga.commons.util.collection.Collections;
import naga.commons.util.tuples.Unit;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static naga.framework.ui.controls.LayoutUtil.setMinSizeToZeroAndPrefSizeToInfinite;

/**
 * @author Bruno Salmon
 */
public class CalendarGraphicImpl implements CalendarGraphic {

    private Calendar calendar;
    private final I18n i18n;
    private Pane rootNode;
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
        if (rootNode != null)
            createOrUpdateRootNodeCalendar();
    }

    private final Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Handler<CalendarClickEvent>> calendarClickHandlerProperty() {
        return calendarClickHandlerProperty;
    }

    @Override
    public Node getNode() {
        if (rootNode == null)
            createRootNode();
        return rootNode;
    }

    private void createRootNode() {
        rootNode = setMinSizeToZeroAndPrefSizeToInfinite(new Pane());
        rootNode.widthProperty().addListener((observable, oldValue, newWidth) -> updateTotalWidth(newWidth.doubleValue()));
        createOrUpdateRootNodeCalendar();
    }

    private HorizontalDayPositioner horizontalDayPositioner;
    private VerticalDayTimePositioner verticalDayPositioner;
    private Group calendarGroup;

    private void createOrUpdateRootNodeCalendar() {
        boolean create = horizontalDayPositioner == null;
        if (create) {
            horizontalDayPositioner = new HorizontalDayPositioner(calendar);
            verticalDayPositioner = new VerticalDayTimePositioner((Property) rootNode.prefHeightProperty());
        } else {
            horizontalDayPositioner.setCalendar(calendar);
            verticalDayPositioner.init();
        }
        createOrUpdateCalendarGroup(create);
        if (create) {
            rootNode.getChildren().setAll(calendarGroup);
            Rotate rotate = null; // Rotate.create();
            if (rotate != null) {
                calendarGroup.getTransforms().setAll(rotate);
                Properties.runOnPropertiesChange(arg -> {
                    rotate.setPivotX(rootNode.getWidth() / 2);
                    rotate.setPivotY(rootNode.getHeight() / 2);
                }, rootNode.widthProperty(), rootNode.heightProperty());
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(5), new KeyValue(rotate.angleProperty(), 360d)));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        }
        updateTotalWidth(rootNode.getWidth());
    }

    private void updateTotalWidth(double totalWidth) {
        horizontalDayPositioner.setTotalWidth(totalWidth);
    }

    private Group headersGroup;
    private Group bodyGroup;

    private void createOrUpdateCalendarGroup(boolean create) {
        createDayColumnHeadersGroup(create);
        createBodyGroup(create);
        bodyGroup.getTransforms().setAll(new Translate(0, DayColumnHeaderViewModel.dayColumnHeaderHeight + 1));
        if (create) {
            calendarGroup = new Group(headersGroup, bodyGroup);
            calendarGroup.setAutoSizeChildren(false);
        }
    }


    private Group createDayColumnHeadersGroup(boolean create) {
        if (create) {
            headersGroup = new Group();
            headersGroup.setAutoSizeChildren(false);
        }
        int index = 0;
        for (long displayedEpochDay = horizontalDayPositioner.getFirstDisplayedEpochDay(); displayedEpochDay <= horizontalDayPositioner.getLastDisplayedEpochDay(); displayedEpochDay++) {
            HorizontalDayPositioned hdp = horizontalDayPositioner.getHorizontalDayPositioned(index++);
            if (hdp instanceof DayColumnHeaderViewModel)
                ((DayColumnHeaderViewModel) hdp).init(displayedEpochDay, i18n);
            else {
                if (hdp != null)
                    horizontalDayPositioner.removeHorizontalDayPositioned(hdp);
                DayColumnHeaderViewModel model = new DayColumnHeaderViewModel(displayedEpochDay, i18n);
                horizontalDayPositioner.addHorizontalDayPositioned(index, model);
                headersGroup.getChildren().add(model.getNode());
            }
        }
        // TODO: Remove extra headers
        return headersGroup;
    }

    private void createBodyGroup(boolean create) {
        if (create) {
            bodyGroup = new Group();
            bodyGroup.setAutoSizeChildren(false);
        }
        Unit<Integer> index = create ? null : new Unit<>(0);
        ObservableList<Node> bodyNodes = bodyGroup.getChildren();
        Collections.forEach(calendar.getTimelines(), timeline -> addTimelineNodes(timeline, bodyNodes, index));
        while (!create && bodyNodes.size() > index.get()) {
            Node node = bodyNodes.remove(index.get().intValue());
            DayColumnBodyBlockViewModel model = (DayColumnBodyBlockViewModel) node.getProperties().get("model");
            horizontalDayPositioner.removeHorizontalDayPositioned(model);
            verticalDayPositioner.removeVerticalDayTimePositioned(model);
        }
        verticalDayPositioner.updateVerticalPositions();
    }

    private void addTimelineNodes(CalendarTimeline timeline, List<Node> bodyNodes, Unit<Integer> index) {
        Collections.forEach(timeline.getDateTimeRange().changeTimeUnit(TimeUnit.DAYS).getDaysArray(),
                epochDay -> addBlockNodes(epochDay, timeline.getDayTimeRange(), timeline, bodyNodes, index));
    }

    private void addBlockNodes(long epochDay, DayTimeRange dayTimeRange, CalendarTimeline timeline, List<Node> bodyNodes, Unit<Integer> index) {
        for (TimeInterval dayTimeInterval : dayTimeRange.getDayTimeSeries(epochDay, TimeUnit.DAYS).getArray())
            addBlockNode(epochDay, dayTimeInterval, timeline, bodyNodes, index);
    }

    private void addBlockNode(long epochDay, TimeInterval minuteInterval, CalendarTimeline timeline, List<Node> bodyNodes, Unit<Integer> index) {
        Node node = index == null ? null : Collections.get(bodyNodes, index.get());
        if (node == null) {
            DayColumnBodyBlockViewModel model = new DayColumnBodyBlockViewModel(this, epochDay, minuteInterval, timeline, epochDay == firstEpochDay ? Boolean.TRUE : null);
            horizontalDayPositioner.addHorizontalDayPositioned(model);
            verticalDayPositioner.addVerticalDayTimePositioned(model);
            node = model.getNode();
            node.getProperties().put("model", model);
            if (index == null)
                bodyNodes.add(node);
            else
                bodyNodes.add(index.get(), node);
        } else {
            DayColumnBodyBlockViewModel model = (DayColumnBodyBlockViewModel) node.getProperties().get("model");
            model.init(this, epochDay, minuteInterval, timeline, epochDay == firstEpochDay ? Boolean.TRUE : null);
            verticalDayPositioner.updateVerticalDayTimePositioned(model);
        }
        if (index != null)
            index.set(index.get() + 1);
    }
}
