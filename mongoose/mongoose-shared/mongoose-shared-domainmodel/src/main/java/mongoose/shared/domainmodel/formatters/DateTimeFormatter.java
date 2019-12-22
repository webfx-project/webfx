package mongoose.shared.domainmodel.formatters;

import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.framework.shared.orm.domainmodel.formatter.ValueParser;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.platform.shared.util.Dates;

import java.time.LocalDateTime;


/**
 * @author Bruno Salmon
 */
public final class DateTimeFormatter implements ValueFormatter, ValueParser {

    public static final DateTimeFormatter SINGLETON = new DateTimeFormatter();

    private DateTimeFormatter() {
    }

    @Override
    public Type getFormattedValueType() {
        return PrimType.STRING;
    }

    @Override
    public Object formatValue(Object value) {
        return Dates.format(value, "dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Object parseValue(Object value) {
        if (value == null)
            return null;
        String text = value.toString();
        int p;
        int dayOfMonth = Integer.parseInt(text.substring(0, p = text.indexOf('/')));
        int month = Integer.parseInt(text.substring(p + 1, p = text.indexOf('/', p + 1)));
        int year = Integer.parseInt(text.substring(p + 1, p = p + 5));
/*
        while (Character.isWhitespace(text.charAt(p)))
            p++;
*/
        int hours = Integer.parseInt(text.substring(p + 1, p = text.indexOf(':', p + 1)));
        int minutes = Integer.parseInt(text.substring(p + 1, p = text.indexOf(':', p + 1)));
        int seconds = Integer.parseInt(text.substring(p + 1));
        return LocalDateTime.of(year, month, dayOfMonth, hours, minutes, seconds);
    }
}
