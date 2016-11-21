package mongoose.activities.shared.logic.calendar.graphic.impl;

import naga.framework.ui.i18n.I18n;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.transform.Translate;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
class DayColumnHeaderViewModel implements HorizontalDayPositioned {

    final static double dayColumnHeaderHeight = 50d;
    private final static LinearGradient dayColumnHeaderFill = LinearGradient.valueOf("from 0% 0% to 0% 100%, 0x75A9A3 0%, 0x375855 100%"); // Color.web("0x609993")
    final static Color dayColumnHeaderTextColor = Color.WHITE;
    private final static String fontFamily = "Verdana";
    private final static Font dayOfWeekTextFont = Font.font(fontFamily, 11);
    private final static Font dayOfMonthTextFont = Font.font(fontFamily, 20);
    private final static Font monthTextFont = dayOfWeekTextFont;

    private final long epochDay;
    private final Rectangle r = Rectangle.create();
    private final TextShape dayOfWeekText = TextShape.create();
    private final TextShape dayOfMonthText = TextShape.create();
    private final TextShape monthText = TextShape.create();
    final Group group = Group.create();
    private final Translate translate = Translate.create();

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
        group.getChildren().setAll(r, dayOfWeekText, dayOfMonthText, monthText);
        group.getTransforms().setAll(translate);
    }

    DayColumnHeaderViewModel(long epochDay, double height, I18n i18n) {
        this.epochDay = epochDay;
        setDate(LocalDate.ofEpochDay(epochDay), i18n);
        setHeight(height);
    }

    @Override
    public long getEpochDay() {
        return epochDay;
    }

    private void setDate(LocalDate date, I18n i18n) {
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
