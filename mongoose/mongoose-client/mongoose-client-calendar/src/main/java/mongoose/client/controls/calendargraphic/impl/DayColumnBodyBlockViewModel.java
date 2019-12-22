package mongoose.client.controls.calendargraphic.impl;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import mongoose.client.controls.calendargraphic.CalendarClickEvent;
import mongoose.client.controls.calendargraphic.CalendarGraphic;
import mongoose.client.businessdata.calendar.CalendarTimeline;
import mongoose.shared.businessdata.time.TimeInterval;
import mongoose.client.controls.calendargraphic.CalendarCell;
import webfx.framework.client.ui.util.background.BackgroundUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public final class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned, CalendarCell {

    private final static String fontFamily = "Verdana";
    private final static Font textFont = Font.font(fontFamily, 13);
    private final static Font timeFont = Font.font(fontFamily, 10);
    private final static Paint timeFill = Color.WHITE;

    private long epochDay;
    private TimeInterval dayTimeMinuteInterval;
    private final Pane rootPane = new Pane();
    private final Text blockText = new Text();
    private Text startTimeText;
    private Text endTimeText;
    private final Translate translate = new Translate();

    {
        blockText.setFont(textFont);
        blockText.setTextAlignment(TextAlignment.CENTER);
        blockText.setFill(DayColumnHeaderViewModel.dayColumnHeaderTextColor);
        blockText.setTextOrigin(VPos.CENTER);
        blockText.setMouseTransparent(true);
        rootPane.getTransforms().setAll(translate);
        rootPane.widthProperty().addListener((observable, oldValue, width) -> onWidthChanged());
        rootPane.heightProperty().addListener((observable, oldValue, height) -> onHeightChanged());
    }

    public DayColumnBodyBlockViewModel(CalendarGraphic calendarGraphic, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline, Boolean displayTimes) {
        init(calendarGraphic, epochDay, dayTimeMinuteInterval, timeline, displayTimes);
    }

    void init(CalendarGraphic calendarGraphic, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline, Boolean displayTimes) {
        this.epochDay = epochDay;
        // Setting dayTimeMinuteInterval for vertical positioning (=> same row as the timeline even for exceptions)
        this.dayTimeMinuteInterval = timeline.getDayTimeRange().getDayTimeInterval(epochDay, TimeUnit.MINUTES); //dayTimeMinuteInterval;
        TextAlignment timeTextAlignment = TextAlignment.LEFT;
        if (displayTimes == null) {
            displayTimes = !this.dayTimeMinuteInterval.equals(dayTimeMinuteInterval);
            timeTextAlignment = TextAlignment.RIGHT;
        }
        Property<String> displayNameProperty = timeline.displayNameProperty();
        if (displayNameProperty != null)
            blockText.textProperty().bind(displayNameProperty);
        else {
            blockText.textProperty().unbind();
            blockText.setText(null);
        }
        rootPane.setBackground(BackgroundUtil.newBackground(timeline.getBackgroundFill()));
        if (calendarGraphic != null)
            rootPane.setOnMouseClicked(event -> {
                if (calendarGraphic.getCalendarClickHandler() != null)
                    calendarGraphic.getCalendarClickHandler().handle(new CalendarClickEvent(event, this, timeline));
            });
        ObservableList<Node> children = rootPane.getChildren();
        if (!displayTimes) {
            startTimeText = endTimeText = null;
            if (children.size() != 2)
                children.setAll(blockText);
        } else {
            if (startTimeText == null) {
                startTimeText = new Text();
                startTimeText.setFont(timeFont);
                startTimeText.setTextAlignment(timeTextAlignment);
                startTimeText.setFill(timeFill);
                startTimeText.setTextOrigin(VPos.TOP);
                startTimeText.setY(1d);
                startTimeText.setMouseTransparent(true);
            }
            startTimeText.setText(dayTimeMinuteInterval.getStartText());
            if (endTimeText == null) {
                endTimeText = new Text();
                endTimeText.setFont(timeFont);
                endTimeText.setTextAlignment(timeTextAlignment);
                endTimeText.setFill(timeFill);
                endTimeText.setTextOrigin(VPos.BOTTOM);
                endTimeText.setMouseTransparent(true);
            }
            endTimeText.setText(dayTimeMinuteInterval.getEndText());
            if (children.size() != 4)
                children.setAll(blockText, startTimeText, endTimeText);
            if (rootPane.getParent() != null)
                onWidthChanged();
        }
        setYAndHeight(0, VerticalDayTimePositioner.slotHeight);
    }

    private void onWidthChanged() {
        double width = rootPane.getWidth();
        blockText.setWrappingWidth(width);
        if (startTimeText != null) {
            startTimeText.setWrappingWidth(width - 2);
            endTimeText.setWrappingWidth(width - 2);
        }
    }

    private void onHeightChanged() {
        double height = rootPane.getHeight();
        blockText.setY(height / 2);
        if (endTimeText != null)
            endTimeText.setY(height - 1d);
    }

    public Pane getNode() {
        return rootPane;
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
        rootPane.resize(width, rootPane.getHeight());
        rootPane.setPrefWidth(width); // In case it is displayed in a layout
    }

    @Override
    public void setYAndHeight(double y, double height) {
        translate.setY(y);
        rootPane.resize(rootPane.getWidth(), height);
        rootPane.setPrefHeight(height); // In case it is displayed in a layout
    }
}
