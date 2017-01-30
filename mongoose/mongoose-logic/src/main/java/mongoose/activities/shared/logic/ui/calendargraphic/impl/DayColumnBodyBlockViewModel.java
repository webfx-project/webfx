package mongoose.activities.shared.logic.ui.calendargraphic.impl;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarCell;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarClickEvent;
import mongoose.activities.shared.logic.ui.calendargraphic.CalendarGraphic;

/**
 * @author Bruno Salmon
 */
public class DayColumnBodyBlockViewModel implements HorizontalDayPositioned, VerticalDayTimePositioned, CalendarCell {

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
        rootPane.widthProperty().addListener((observable, oldValue, width) -> {
            blockText.setWrappingWidth((double) width);
            if (startTimeText != null) {
                startTimeText.setWrappingWidth((double) width - 2);
                endTimeText.setWrappingWidth((double) width - 2);
            }
        });
        rootPane.heightProperty().addListener((observable, oldValue, height) -> {
            blockText.setY((double) height / 2);
            if (endTimeText != null)
                endTimeText.setY((double) height - 1d);
        });
    }

    public DayColumnBodyBlockViewModel(CalendarGraphic calendarGraphic, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline, boolean displayTimes) {
        init(calendarGraphic, epochDay, dayTimeMinuteInterval, timeline, displayTimes);
    }

    void init(CalendarGraphic calendarGraphic, long epochDay, TimeInterval dayTimeMinuteInterval, CalendarTimeline timeline, boolean displayTimes) {
        this.epochDay = epochDay;
        this.dayTimeMinuteInterval = dayTimeMinuteInterval;
        Property<String> displayNameProperty = timeline.displayNameProperty();
        if (displayNameProperty != null)
            blockText.textProperty().bind(displayNameProperty);
        else {
            blockText.textProperty().unbind();
            blockText.setText(null);
        }
        rootPane.setBackground(new Background(new BackgroundFill(timeline.getBackgroundFill(), null, null)));
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
                startTimeText.setTextAlignment(TextAlignment.LEFT);
                startTimeText.setFill(timeFill);
                startTimeText.setTextOrigin(VPos.TOP);
                startTimeText.setY(1d);
                startTimeText.setMouseTransparent(true);
            }
            startTimeText.setText(dayTimeMinuteInterval.getStartText());
            if (endTimeText == null) {
                endTimeText = new Text();
                endTimeText.setFont(timeFont);
                endTimeText.setTextAlignment(TextAlignment.LEFT);
                endTimeText.setFill(timeFill);
                endTimeText.setTextOrigin(VPos.BOTTOM);
                endTimeText.setMouseTransparent(true);
            }
            endTimeText.setText(dayTimeMinuteInterval.getEndText());
            if (children.size() != 4)
                children.setAll(blockText, startTimeText, endTimeText);
        }
        setYAndHeight(0, VerticalDayTimePositioner.slotHeight);
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
