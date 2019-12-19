package mongoose.client.controls.calendargraphic.impl;

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
import mongoose.client.controls.calendargraphic.CalendarClickEvent;
import mongoose.client.controls.calendargraphic.CalendarGraphic;
import mongoose.client.businessdata.calendar.Calendar;
import mongoose.client.businessdata.calendar.CalendarTimeline;
import mongoose.shared.businessdata.time.DayTimeRange;
import mongoose.shared.businessdata.time.TimeInterval;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.tuples.Unit;
import webfx.kit.util.properties.Properties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static webfx.framework.client.ui.util.layout.LayoutUtil.setMinSizeToZeroAndPrefSizeToInfinite;

/**
 * @author Bruno Salmon
 */
public final class CalendarGraphicImpl implements CalendarGraphic {

    private Calendar calendar;
    private Pane rootNode;
    private long firstEpochDay;

    public CalendarGraphicImpl(Calendar calendar) {
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
    public Handler<CalendarClickEvent> getCalendarClickHandler() {
        return calendarClickHandlerProperty.getValue();
    }

    @Override
    public void setCalendarClickHandler(Handler<CalendarClickEvent> calendarClickEventHandler) {
        calendarClickHandlerProperty().setValue(calendarClickEventHandler);
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
            verticalDayPositioner = new VerticalDayTimePositioner(rootNode.prefHeightProperty());
            rootNode.minHeightProperty().bind(rootNode.prefHeightProperty());
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
                Properties.runOnPropertiesChange(() -> {
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
                ((DayColumnHeaderViewModel) hdp).init(displayedEpochDay);
            else {
                if (hdp != null)
                    horizontalDayPositioner.removeHorizontalDayPositioned(hdp);
                DayColumnHeaderViewModel model = new DayColumnHeaderViewModel(displayedEpochDay);
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
