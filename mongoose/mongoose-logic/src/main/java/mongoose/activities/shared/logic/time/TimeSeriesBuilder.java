package mongoose.activities.shared.logic.time;

import naga.commons.util.Objects;
import naga.commons.util.Strings;
import naga.commons.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static mongoose.activities.shared.logic.time.TimeConverter.parseTime;

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
        return new TimeSeries(Collections.toArray(series, TimeInterval[]::new), timeUnit);
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
                series.add(new TimeInterval(includedStart, excludedEnd, daysTimeUnit).changeTimeUnit(timeUnit));
                includedStart = day;
            }
            excludedEnd = day + oneDay;
        }
        if (includedStart != excludedEnd)
            series.add(new TimeInterval(includedStart, excludedEnd, daysTimeUnit).changeTimeUnit(timeUnit));
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
            long includedStart = parseTime(startText, timeUnit, false);
            long excludedEnd = parseTime(Objects.coalesce(endText, startText), timeUnit, true);
            if (lastInterval != null && includedStart == lastInterval.getExcludedEnd())
                series.set(series.size() - 1, lastInterval = new TimeInterval(lastInterval.getIncludedStart(), excludedEnd, timeUnit));
            else
                series.add(lastInterval = new TimeInterval(includedStart, excludedEnd, timeUnit));
        }
        return this;
    }

}
