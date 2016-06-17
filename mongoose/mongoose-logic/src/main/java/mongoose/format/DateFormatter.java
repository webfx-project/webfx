package mongoose.format;

import naga.core.util.Numbers;
import naga.core.util.function.Converter;

import java.time.LocalDateTime;


/**
 * @author Bruno Salmon
 */
public class DateFormatter implements Converter {

    public static final DateFormatter SINGLETON = new DateFormatter();

    private DateFormatter() {
    }

    @Override
    public Object convert(Object value) {
        LocalDateTime date = null;
        if (value instanceof LocalDateTime)
            date = (LocalDateTime) value;
        else if (value instanceof String)
            date = LocalDateTime.parse(value.toString());
        else if (Numbers.isNumber(value))
            date = LocalDateTime.ofEpochSecond(Numbers.longValue(value) / 1000, 0, null);
        if (date != null) {
            // GWT DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            StringBuilder sb = new StringBuilder();
            append2d(date.getDayOfMonth(), sb);
            append2d(date.getMonthValue(), sb.append('/'));
            sb.append('/').append(date.getYear()).append(' ');
            append2d(date.getHour(), sb);
            append2d(date.getMinute(), sb.append(':'));
            append2d(date.getSecond(), sb.append(':'));
            value = sb.toString();
        }
        return value;
    }

    private static void append2d(int i, StringBuilder sb) {
        if (i < 10)
            sb.append('0');
        sb.append(i);
    }
}
