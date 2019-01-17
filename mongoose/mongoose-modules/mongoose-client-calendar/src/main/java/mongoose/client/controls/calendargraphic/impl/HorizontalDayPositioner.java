package mongoose.client.controls.calendargraphic.impl;

import mongoose.client.businessdata.calendar.Calendar;
import mongoose.shared.businessdata.time.TimeInterval;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
final class HorizontalDayPositioner {

    private Calendar calendar;
    private final List<HorizontalDayPositioned> horizontalDayPositionedList = new ArrayList<>();
    private long firstDisplayedEpochDay = -1;
    private long lastDisplayedEpochDay = -1;

    HorizontalDayPositioner(Calendar calendar) {
        setCalendar(calendar);
    }

    void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        firstDisplayedEpochDay = -1;
        lastDisplayedEpochDay = -1;
    }

    private void initFirstAndLastDisplayedEpochDays() {
        TimeInterval period = calendar.getPeriod().changeTimeUnit(TimeUnit.DAYS);
        firstDisplayedEpochDay = period.getIncludedStart();
        lastDisplayedEpochDay = period.getExcludedEnd() - 1;
    }

    HorizontalDayPositioned getHorizontalDayPositioned(int index) {
        return Collections.get(horizontalDayPositionedList, index);
    }

    void addHorizontalDayPositioned(int index, HorizontalDayPositioned horizontalDayPositioned) {
        if (index < horizontalDayPositionedList.size())
            horizontalDayPositionedList.add(index, horizontalDayPositioned);
        else
            addHorizontalDayPositioned(horizontalDayPositioned);
    }

    void addHorizontalDayPositioned(HorizontalDayPositioned horizontalDayPositioned) {
        horizontalDayPositionedList.add(horizontalDayPositioned);
    }

    void removeHorizontalDayPositioned(HorizontalDayPositioned horizontalDayPositioned) {
        horizontalDayPositionedList.remove(horizontalDayPositioned);
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
        for (HorizontalDayPositioned horizontalDayPositioned : horizontalDayPositionedList) {
            double x = (horizontalDayPositioned.getEpochDay() - firstDisplayedEpochDay) * dayWidth;
            double snapX = snap(x);
            horizontalDayPositioned.setXAndWidth(snapX, snap(x + dayWidth - 1) - snapX);
        }
    }

    private static double snap(double value) {
        return Math.round(value);
    }
}
