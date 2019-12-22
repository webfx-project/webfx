package mongoose.shared.domainmodel.formatters;

import javafx.util.StringConverter;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;
import webfx.framework.shared.orm.domainmodel.formatter.ValueParser;
import webfx.extras.type.PrimType;
import webfx.extras.type.Type;
import webfx.platform.shared.util.Dates;

import java.time.LocalDate;


/**
 * @author Bruno Salmon
 */
public final class DateFormatter implements ValueFormatter, ValueParser {

    public static final DateFormatter SINGLETON = new DateFormatter();

    private DateFormatter() {
    }

    @Override
    public Type getFormattedValueType() {
        return PrimType.STRING;
    }

    @Override
    public Object formatValue(Object value) {
        return Dates.format(value, "dd/MM/yyyy");
    }

    @Override
    public Object parseValue(Object value) {
        if (value == null)
            return null;
        String text = value.toString();
        int p;
        int dayOfMonth = Integer.parseInt(text.substring(0, p = text.indexOf('/')));
        int month = Integer.parseInt(text.substring(p + 1, p = text.indexOf('/', p + 1)));
        int year = Integer.parseInt(text.substring(p + 1, p + 5));
        return LocalDate.of(year, month, dayOfMonth);
    }

    public StringConverter<LocalDate> toStringConverter() {
        return new StringConverter<LocalDate/*GWT*/>() {
            @Override
            public String toString(LocalDate date) {
                return (String) formatValue(date);
            }

            @Override
            public LocalDate fromString(String date) {
                return (LocalDate) parseValue(date);
            }
        };
    }
}
