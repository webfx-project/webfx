package mongoose.client.controls.calendargraphic.impl;

import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import webfx.framework.client.services.i18n.I18nControls;
import webfx.framework.client.ui.util.background.BackgroundUtil;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public final class DayColumnHeaderViewModel implements HorizontalDayPositioned {

    final static double dayColumnHeaderHeight = 50d;
    private final static LinearGradient dayColumnHeaderFill = LinearGradient.valueOf("from 0% 0% to 0% 100%, 0x75A9A3 0%, 0x375855 100%"); // Color.web("0x609993")
    final static Color dayColumnHeaderTextColor = Color.WHITE;
    private final static String fontFamily = "Verdana";
    private final static Font dayOfWeekTextFont = Font.font(fontFamily, 11);
    private final static Font dayOfMonthTextFont = Font.font(fontFamily, 20);
    private final static Font monthTextFont = dayOfWeekTextFont;

    private long epochDay;
    private final Pane rootPane = new Pane();
    private final Text dayOfWeekText = new Text();
    private final Text dayOfMonthText = new Text();
    private final Text monthText = new Text();
    private final Translate translate = new Translate();

    {
        dayOfWeekText.setTextAlignment(TextAlignment.CENTER);
        dayOfWeekText.setFont(dayOfWeekTextFont);
        dayOfWeekText.setFill(dayColumnHeaderTextColor);
        dayOfWeekText.setTextOrigin(VPos.TOP);
        dayOfWeekText.setY(3d);
        dayOfWeekText.wrappingWidthProperty().bind(rootPane.widthProperty());
        dayOfMonthText.setTextAlignment(TextAlignment.CENTER);
        dayOfMonthText.setFont(dayOfMonthTextFont);
        dayOfMonthText.setFill(dayColumnHeaderTextColor);
        dayOfMonthText.setTextOrigin(VPos.CENTER);
        dayOfMonthText.wrappingWidthProperty().bind(rootPane.widthProperty());
        monthText.setTextAlignment(TextAlignment.CENTER);
        monthText.setFont(monthTextFont);
        monthText.setFill(dayColumnHeaderTextColor);
        monthText.setTextOrigin(VPos.BOTTOM);
        monthText.wrappingWidthProperty().bind(rootPane.widthProperty());
        rootPane.setBackground(BackgroundUtil.newBackground(dayColumnHeaderFill));
        rootPane.getChildren().setAll(dayOfWeekText, dayOfMonthText, monthText);
        rootPane.getTransforms().setAll(translate);
        rootPane.heightProperty().addListener((observable, oldValue, height) -> {
            dayOfMonthText.setY((double) height / 2);
            monthText.setY((double) height - 3d);
        });
    }

    public DayColumnHeaderViewModel(long epochDay) {
        init(epochDay);
    }

    void init(long epochDay) {
        this.epochDay = epochDay;
        setDate(LocalDate.ofEpochDay(epochDay));
        setHeight(dayColumnHeaderHeight);
    }

    @Override
    public long getEpochDay() {
        return epochDay;
    }

    public Pane getNode() {
        return rootPane;
    }

    private void setDate(LocalDate date) {
        I18nControls.bindI18nProperties(dayOfWeekText, date.getDayOfWeek().name());
        dayOfMonthText.setText("" + date.getDayOfMonth());
        I18nControls.bindI18nProperties(monthText, date.getMonth().name());
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
        rootPane.resize(width, rootPane.getHeight());
        rootPane.setPrefWidth(width); // In case it is displayed in a layout
    }

    void setHeight(double height) {
        rootPane.resize(rootPane.getWidth(), height);
        rootPane.setPrefHeight(height); // In case it is displayed in a layout
    }
}
