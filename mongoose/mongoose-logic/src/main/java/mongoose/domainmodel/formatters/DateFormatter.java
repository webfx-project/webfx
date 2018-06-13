package mongoose.domainmodel.formatters;

import javafx.util.StringConverter;
import naga.type.PrimType;
import naga.type.Type;
import naga.util.Dates;
import naga.framework.ui.formatter.Formatter;

import java.time.LocalDate;


/**
 * @author Bruno Salmon
 */
public class DateFormatter implements Formatter {

    public static final DateFormatter SINGLETON = new DateFormatter();
    public static final StringConverter<LocalDate> LOCAL_DATE_STRING_CONVERTER =  new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate date) {
            return (String) DateFormatter.SINGLETON.format(date);
        }

        @Override
        public LocalDate fromString(String date) {
            if (date == null)
                return null;
            int p;
            int dayOfMonth = Integer.parseInt(date.substring(0, p = date.indexOf('/')));
            int month = Integer.parseInt(date.substring(p + 1, p = date.indexOf('/', p + 1)));
            int year = Integer.parseInt(date.substring(p + 1));
            return LocalDate.of(year, month, dayOfMonth);
        }
    };

    private DateFormatter() {
    }

    @Override
    public Type getOutputType() {
        return PrimType.STRING;
    }

    @Override
    public Object format(Object value) {
        return Dates.format(value, "dd/MM/yyyy");
    }
}
