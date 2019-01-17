package mongoose.shared.businessdata.time;

import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class DayTimeRange {

    private final TimeRangeRule generalRule;
    private final List<TimeRangeRule> exceptionRules;
    private String text;

    public static DayTimeRange parse(String text) {
        return text == null ? null : new DayTimeRange(text);
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

    public DayTimeRange(TimeRangeRule generalRule, List<TimeRangeRule> exceptionRules) {
        this.generalRule = generalRule;
        this.exceptionRules = exceptionRules;
    }

    public String getText() {
        if (text == null) {
            text = generalRule.getDayTimeSeries().toText();
            if (!Collections.isEmpty(exceptionRules)) {
                StringBuilder sb = new StringBuilder("{'*': '").append(text).append('\'');
                Collections.forEach(exceptionRules, rule -> {
                    sb.append(", '").append(rule.getCoverage().getText()).append("': '");
                    rule.getDayTimeSeries().toText(sb).append('\'');
                });
                text = sb.append('}').toString();
            }
        }
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    public TimeSeries getDayTimeSeries(long day, TimeUnit timeUnit) {
        return getRuleForDay(day, timeUnit).getDayTimeSeries();
    }

    public TimeInterval getDayTimeInterval(long day, TimeUnit timeUnit) {
        return getRuleForDay(day, timeUnit).getDayTimeInterval();
    }

    public TimeRangeRule getGeneralRule() {
        return generalRule;
    }

    public List<TimeRangeRule> getExceptionRules() {
        return exceptionRules;
    }

    public TimeRangeRule getRuleForDay(long day) {
        return getRuleForDay(day, TimeUnit.DAYS);
    }

    public TimeRangeRule getRuleForDay(long day, TimeUnit timeUnit) {
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

    public DayTimeRange changeGeneralRule(String newGeneralDayTimeSeriesText) {
        return changeGeneralRule(new TimeRangeRule(null, newGeneralDayTimeSeriesText));
    }

    public DayTimeRange changeGeneralRule(TimeRangeRule newGeneralRule) {
        return new DayTimeRange(newGeneralRule, exceptionRules);
    }

    public DayTimeRange addExceptionRuleForDay(long day, String exceptionDayTimeSeriesText) {
        return addExceptionRuleForDay(day, TimeUnit.DAYS, exceptionDayTimeSeriesText);
    }

    public DayTimeRange addExceptionRuleForDay(long day, TimeUnit timeUnit, String exceptionDayTimeSeriesText) {
        day = TimeConverter.convertTime(day, timeUnit, TimeUnit.MINUTES);
        long nextDay = day + TimeConverter.oneDay(TimeUnit.MINUTES);
        TimeRangeRule exceptionRule = new TimeRangeRule(new DateTimeRange(new TimeInterval(day, nextDay, TimeUnit.MINUTES)), exceptionDayTimeSeriesText);
        return removeExceptionRuleForDay(day, timeUnit).addExceptionRule(exceptionRule);
    }

    public DayTimeRange addExceptionRule(TimeRangeRule exceptionRule) {
        List<TimeRangeRule> newExceptionRules = new ArrayList<>();
        if (exceptionRules != null)
            newExceptionRules.addAll(exceptionRules);
        newExceptionRules.add(exceptionRule);
        return new DayTimeRange(generalRule, newExceptionRules);
    }

    public DayTimeRange removeExceptionRuleForDay(long day) {
        return removeExceptionRuleForDay(day, TimeUnit.DAYS);
    }

    public DayTimeRange removeExceptionRuleForDay(long day, TimeUnit timeUnit) {
        TimeRangeRule ruleForDay = getRuleForDay(day, timeUnit);
        if (ruleForDay == generalRule)
            return this;
        List<TimeRangeRule> newExceptionRules = new ArrayList<>(exceptionRules);
        newExceptionRules.remove(ruleForDay);
        return new DayTimeRange(generalRule, newExceptionRules);
    }

    public static class TimeRangeRule {
        private final DateTimeRange coverage;
        private final TimeSeries dayTimeSeries;
        private TimeInterval dayTimeInterval;

        public TimeRangeRule(DateTimeRange coverage, String text) {
            this(coverage, TimeSeries.parse(text));
        }

        public TimeRangeRule(DateTimeRange coverage, TimeSeries dayTimeSeries) {
            this.coverage = coverage;
            this.dayTimeSeries = dayTimeSeries;
        }

        public DateTimeRange getCoverage() {
            return coverage;
        }

        public TimeSeries getDayTimeSeries() {
            return dayTimeSeries;
        }

        public TimeInterval getDayTimeInterval() {
            if (dayTimeInterval == null)
                dayTimeInterval = dayTimeSeries.toInterval();
            return dayTimeInterval;
        }
    }
}