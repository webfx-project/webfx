package mongoose.format;

import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.format.Formatter;
import naga.core.util.Numbers;

import java.time.LocalDateTime;


/**
 * @author Bruno Salmon
 */
public class DateFormatter implements Formatter {

    public static final DateFormatter SINGLETON = new DateFormatter();

    private DateFormatter() {
    }

    @Override
    public Type getExpectedFormattedType() {
        return PrimType.DATE;
    }

    @Override
    public Object format(Object value) {
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
