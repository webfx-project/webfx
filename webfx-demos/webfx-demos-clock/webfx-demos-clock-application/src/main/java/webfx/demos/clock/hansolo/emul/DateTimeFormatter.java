package webfx.demos.clock.hansolo.emul;

import webfx.platform.shared.util.Dates;

import java.time.ZonedDateTime;

/**
 * @author Bruno Salmon
 */
public interface DateTimeFormatter {

    String format(ZonedDateTime dateTime);

    static DateTimeFormatter ofPattern(String pattern) {
        return dateTime -> Dates.format(dateTime, pattern);
    }
}
