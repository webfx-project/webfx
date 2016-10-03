package mongoose.activities.shared.logic.time;

import naga.platform.json.Json;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class DayTimeRange {

    private final String text;
    private final TimeRangeRule generalRule;
    private final List<TimeRangeRule> exceptionRules;

    public static DayTimeRange parse(String text) {
        return new DayTimeRange(text);
    }

    private DayTimeRange(String text) {
        this.text = text;
        if (!text.startsWith("{")) {
            generalRule = new TimeRangeRule(null, text);
            exceptionRules = null;
        } else {
            TimeRangeRule _generalRule = null;
            exceptionRules = new ArrayList<>();
            JsonObject json = Json.parseObject(text);
            JsonArray keys = json.keys();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.getString(i);
                String value = json.getString(key);
                DateTimeRange coverage = key.equals("*") ? null : DateTimeRange.parse(key);
                TimeRangeRule timeRangeRule = new TimeRangeRule(coverage, value);
                if (coverage == null)
                    _generalRule = timeRangeRule;
                else
                    exceptionRules.add(timeRangeRule);
            }
            generalRule = _generalRule;
        }
    }

    public TimeSeries getDayTimeSeries(long day, TimeUnit timeUnit) {
        return getRuleForDay(day, timeUnit).getDayTimeSeries();
    }

    public TimeInterval getDayTimeInterval(long day, TimeUnit timeUnit) {
        return getRuleForDay(day, timeUnit).getDayTimeInterval();
    }

    private TimeRangeRule getRuleForDay(long day, TimeUnit timeUnit) {
        if (exceptionRules != null) {
            day = TimeConverter.convertTime(day, timeUnit, TimeUnit.MINUTES);
            long nextDay = day + TimeConverter.oneDay(TimeUnit.MINUTES);
            for (TimeRangeRule exceptionRule : exceptionRules) {
                if (exceptionRule.getCoverage().containsInterval(day, nextDay, TimeUnit.MINUTES))
                    return exceptionRule;
            }
        }
        return generalRule;
    }

    private static class TimeRangeRule {
        final DateTimeRange coverage;
        final TimeSeries dayTimeSeries;
        TimeInterval dayTimeInterval;

        TimeRangeRule(DateTimeRange coverage, String text) {
            this.coverage = coverage;
            dayTimeSeries = TimeSeries.parse(text);
        }

        DateTimeRange getCoverage() {
            return coverage;
        }

        TimeSeries getDayTimeSeries() {
            return dayTimeSeries;
        }

        TimeInterval getDayTimeInterval() {
            if (dayTimeInterval == null)
                dayTimeInterval = dayTimeSeries.toInterval();
            return dayTimeInterval;
        }
    }
}
