package io.fxgame.game2048.emul;

import webfx.platform.shared.util.Dates;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author Bruno Salmon
 */
public interface DateTimeFormatter {

    String format(LocalTime dateTime);

    default DateTimeFormatter withZone(ZoneId zoneId) {
        return this;
    }

    static DateTimeFormatter ofPattern(String pattern) {
        return dateTime -> Dates.format(dateTime, pattern);
    }
}
