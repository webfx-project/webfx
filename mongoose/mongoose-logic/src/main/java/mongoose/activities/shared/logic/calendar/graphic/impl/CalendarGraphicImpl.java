package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.TimeInterval;
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

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class CalendarGraphicImpl implements CalendarGraphic {

    private final Calendar calendar;
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
    public DrawingNode getDrawingNode() {
        if (drawingNode == null)
            createDrawingNode();
        return drawingNode;
    }

    private final List<DayRectViewModel> dayRectViewModels = new ArrayList<>();

    private final Rotate rotate = null; // Rotate.create();

    private void createDrawingNode() {
        // Drawing node (just testing API for now)
        drawingNode = Toolkit.get().createDrawingNode();
        Group timeLineGroup = DrawableFactory.get().createGroup();
        TimeInterval period = calendar.getPeriod().changeTimeUnit(TimeUnit.DAYS);
        for (long day = period.getIncludedStart(); day < period.getExcludedEnd(); day++) {
            DayRectViewModel dayRectViewModel = new DayRectViewModel(LocalDate.ofEpochDay(day), dayRectangleHeight);
            dayRectViewModels.add(dayRectViewModel);
            Group dayRect = dayRectViewModel.group;
            timeLineGroup.getDrawableChildren().add(dayRect);
        }
        drawingNode.getDrawableChildren().setAll(timeLineGroup);
        drawingNode.widthProperty().addListener((observable, oldValue, newWidth) -> updateTotalWidth(newWidth));
        if (rotate != null) {
            rotate.setPivotY(dayRectangleHeight / 2);
            timeLineGroup.getTransforms().setAll(rotate);
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ofSeconds(5), new KeyValue(rotate.angleProperty(), 360d))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }


    private void updateTotalWidth(double totalWidth) {
        double width = totalWidth / dayRectViewModels.size();
        int i = 0;
        for (DayRectViewModel dayRectViewModel : dayRectViewModels)
            dayRectViewModel.setXAndWidth(i++ * width, width - 1);
        if (rotate != null)
            rotate.setPivotX(totalWidth / 2);
    }

    private final static double dayRectangleHeight = 50d;
    private final static LinearGradient dayRectangleFill = LinearGradient.valueOf("from 0% 0% to 0% 100%, 0x75A9A3 0%, 0x375855 100%"); // Color.web("0x609993")
    private final static Font dayTextFont = Font.font("Verdana", 11);
    private final static Font digitTextFont = Font.font("Verdana", 20);
    private final static Font monthTextFont = dayTextFont;
    private final static Color dayTextColor = Color.WHITE;

    private class DayRectViewModel {
        final Rectangle r = DrawableFactory.get().createRectangle();
        final TextShape dayText = DrawableFactory.get().createText();
        final TextShape digitText = DrawableFactory.get().createText();
        final TextShape monthText = DrawableFactory.get().createText();
        final Group group = DrawableFactory.get().createGroup();
        final Translate translate = Translate.create();

        {
            r.setFill(dayRectangleFill);
/*
        r.setStroke(Color.ORANGE);
        r.setStrokeWidth(5d);
        r.getStrokeDashArray().setAll(25d, 20d, 5d, 20d);
        r.setStrokeLineCap(StrokeLineCap.ROUND);
        r.setStrokeLineJoin(StrokeLineJoin.ROUND);
*/
            dayText.setTextAlignment(TextAlignment.CENTER);
            dayText.setFont(dayTextFont);
            dayText.setFill(dayTextColor);
            dayText.setTextOrigin(VPos.TOP);
            dayText.setY(3d);
            digitText.setTextAlignment(TextAlignment.CENTER);
            digitText.setFont(digitTextFont);
            digitText.setFill(dayTextColor);
            digitText.setTextOrigin(VPos.CENTER);
            monthText.setTextAlignment(TextAlignment.CENTER);
            monthText.setFont(monthTextFont);
            monthText.setFill(dayTextColor);
            monthText.setTextOrigin(VPos.BOTTOM);
            group.getDrawableChildren().setAll(r, dayText, digitText, monthText);
            group.getTransforms().setAll(translate);
        }

        DayRectViewModel(LocalDate date, double height) {
            setDate(date);
            setHeight(height);
        }

        void setDate(LocalDate date) {
            i18n.translateText(dayText, date.getDayOfWeek().name());
            digitText.setText("" + date.getDayOfMonth());
            i18n.translateText(monthText, date.getMonth().name());
        }

        void setXAndWidth(double x, double width) {
            setX(x);
            setWidth(width);
        }

        void setX(double x) {
            translate.setX(x);
        }

        void setWidth(double width) {
            r.setWidth(width);
            dayText.setWrappingWidth(width);
            digitText.setWrappingWidth(width);
            monthText.setWrappingWidth(width);
        }

        void setHeight(double height) {
            r.setHeight(height);
            digitText.setY(height / 2);
            monthText.setY(height - 3d);
        }
    }
}
