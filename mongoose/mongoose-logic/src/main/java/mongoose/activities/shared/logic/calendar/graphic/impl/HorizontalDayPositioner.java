package mongoose.activities.shared.logic.calendar.graphic.impl;

import mongoose.activities.shared.logic.calendar.Calendar;
import mongoose.activities.shared.logic.time.TimeInterval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
class HorizontalDayPositioner {

    private final Calendar calendar;
    private final Collection<HorizontalDayPositioned> horizontalDayPositionedCollection = new ArrayList<>();
    private long firstDisplayedEpochDay = -1;
    private long lastDisplayedEpochDay = -1;

    HorizontalDayPositioner(Calendar calendar) {
        this.calendar = calendar;
    }

    private void initFirstAndLastDisplayedEpochDays() {
        TimeInterval period = calendar.getPeriod().changeTimeUnit(TimeUnit.DAYS);
        firstDisplayedEpochDay = period.getIncludedStart();
        lastDisplayedEpochDay = period.getExcludedEnd() - 1;
    }

    void addHorizontalDayPositioned(HorizontalDayPositioned horizontalDayPositioned) {
        horizontalDayPositionedCollection.add(horizontalDayPositioned);
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
        for (HorizontalDayPositioned horizontalDayPositioned : horizontalDayPositionedCollection) {
            double x = (horizontalDayPositioned.getEpochDay() - firstDisplayedEpochDay) * dayWidth;
            double snapX = snap(x);
            horizontalDayPositioned.setXAndWidth(snapX, snap(x + dayWidth - 1) - snapX);
        }
    }

    private static double snap(double value) {
        return Math.round(value);
    }
}
