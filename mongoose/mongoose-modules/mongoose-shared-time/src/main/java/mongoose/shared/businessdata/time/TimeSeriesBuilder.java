package mongoose.shared.businessdata.time;

import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
class TimeSeriesBuilder {

    private final List<TimeInterval> series = new ArrayList<>();
    private final TimeUnit timeUnit;

    TimeSeriesBuilder(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    TimeSeries build() {
        return new TimeSeries(series, timeUnit);
    }

    TimeSeriesBuilder addDaysArrays(DaysArray daysArray) {
        return addDaysArrays(daysArray.getArray(), daysArray.getTimeUnit());
    }

    TimeSeriesBuilder addDaysArrays(long[] days, TimeUnit daysTimeUnit) {
        long includedStart = 0;
        long excludedEnd = 0;
        long oneDay = TimeConverter.oneDay(daysTimeUnit);
        for (long day : days) {
            if (includedStart == excludedEnd)
                includedStart = day;
            else if (day != excludedEnd) { // > 1 day => break of the consecutive dates
                addInterval(new TimeInterval(includedStart, excludedEnd, daysTimeUnit));
                includedStart = day;
            }
            excludedEnd = day + oneDay;
        }
        if (includedStart != excludedEnd)
            addInterval(new TimeInterval(includedStart, excludedEnd, daysTimeUnit));
        return this;
    }

    TimeSeriesBuilder addText(String text) {
        String[] commaTokens = Strings.split(text, ",");
        TimeInterval lastInterval = null;
        for (String commaToken : commaTokens) {
            String[] hyphenTokens = Strings.split(commaToken, "-");
            String startText = commaToken, endText = null;
            if (hyphenTokens.length == 2) {
                startText = hyphenTokens[0];
                endText = hyphenTokens[1];
            }
            long includedStart = TimeConverter.parseTime(startText, timeUnit, false);
            long excludedEnd = TimeConverter.parseTime(Objects.coalesce(endText, startText), timeUnit, true);
            if (lastInterval != null && includedStart == lastInterval.getExcludedEnd())
                series.set(series.size() - 1, lastInterval = new TimeInterval(lastInterval.getIncludedStart(), excludedEnd, timeUnit));
            else
                series.add(lastInterval = new TimeInterval(includedStart, excludedEnd, timeUnit));
        }
        return this;
    }

    TimeSeriesBuilder addInterval(TimeInterval interval) {
        interval = interval.changeTimeUnit(timeUnit);
        TimeInterval lastInterval = Collections.last(series);
        if (lastInterval == null || interval.getIncludedStart() > lastInterval.getExcludedEnd())
            series.add(interval);
        else if (interval.getExcludedEnd() > lastInterval.getExcludedEnd())
            series.set(series.size() - 1, new TimeInterval(lastInterval.getIncludedStart(), interval.getExcludedEnd(), timeUnit));
        return this;
    }

    TimeSeriesBuilder addSeries(TimeSeries series) {
        for (TimeInterval interval : series.getArray())
            addInterval(interval);
        return this;
    }
}