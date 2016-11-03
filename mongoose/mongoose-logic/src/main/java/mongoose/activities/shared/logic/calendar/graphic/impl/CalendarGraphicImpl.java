package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.time.TimeInterval;
import naga.framework.ui.i18n.I18n;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;

import java.time.LocalDate;
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

    private void createDrawingNode() {
        // Drawing node (just testing API for now)
        drawingNode = Toolkit.get().createDrawingNode();
        double width = 200d;
        double height = 50d;
        Group timeLineGroup = DrawableFactory.get().createGroup();
        TimeInterval period = calendar.getPeriod().changeTimeUnit(TimeUnit.DAYS);
        int i = 0;
        for (long day = period.getIncludedStart(); day < period.getExcludedEnd(); day++)
            timeLineGroup.getDrawableChildren().add(createDayRect(LocalDate.ofEpochDay(day), i++ * width, width - 1, height));
        drawingNode.getDrawableChildren().setAll(timeLineGroup);
/*
        Rotate rotate = Rotate.create();
        rotate.setPivotX( width * i / 2);
        rotate.setPivotY(height / 2);
        timeLineGroup.getTransforms().setAll(rotate);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.ofSeconds(5), new KeyValue(rotate.angleProperty(), 360d)
        ));
        timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.setAutoReverse(true);
        timeline.play();
*/
    }

    private Group createDayRect(LocalDate date, double x, double width, double height) {
        String dayOfWeekName = date.getDayOfWeek().name();
        String dayOfMonth = "" + date.getDayOfMonth();
        String monthName = date.getMonth().name();

        Rectangle r = DrawableFactory.get().createRectangle();
        r.setX(x);
        r.setWidth(width);
        r.setHeight(height);
        //r.setFill(Color.web("0x609993"));
        r.setFill(LinearGradient.valueOf("from 0% 0% to 0% 100%, 0x75A9A3 0%, 0x375855 100%"));
/*
        r.setStroke(Color.ORANGE);
        r.setStrokeWidth(5d);
        r.getStrokeDashArray().setAll(25d, 20d, 5d, 20d);
        r.setStrokeLineCap(StrokeLineCap.ROUND);
        r.setStrokeLineJoin(StrokeLineJoin.ROUND);
*/
        TextShape dayText = DrawableFactory.get().createText();
        i18n.translateText(dayText, dayOfWeekName);
        dayText.setWrappingWidth(width);
        dayText.setTextAlignment(TextAlignment.CENTER);
        dayText.setFont(Font.font("Verdana", 11));
        dayText.setFill(Color.WHITE);
        dayText.setTextOrigin(VPos.TOP);
        dayText.setX(x);
        dayText.setY(3d);
        TextShape digitText = DrawableFactory.get().createText();
        digitText.setText(dayOfMonth);
        digitText.setWrappingWidth(width);
        digitText.setTextAlignment(TextAlignment.CENTER);
        digitText.setFont(Font.font("Verdana", 20));
        digitText.setFill(Color.WHITE);
        digitText.setTextOrigin(VPos.CENTER);
        digitText.setX(x);
        digitText.setY(height / 2);
        TextShape monthText = DrawableFactory.get().createText();
        i18n.translateText(monthText, monthName);
        monthText.setWrappingWidth(width);
        monthText.setTextAlignment(TextAlignment.CENTER);
        monthText.setFont(Font.font("Verdana", 11));
        monthText.setFill(Color.WHITE);
        monthText.setTextOrigin(VPos.BOTTOM);
        monthText.setX(x);
        monthText.setY(height - 3d);
        Group group = DrawableFactory.get().createGroup();
        group.getDrawableChildren().setAll(r, dayText, digitText, monthText);
        return group;
    }
}
