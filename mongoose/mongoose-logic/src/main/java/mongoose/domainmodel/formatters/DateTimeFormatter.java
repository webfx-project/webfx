package mongoose.domainmodel.formatters;

import naga.type.PrimType;
import naga.type.Type;
import naga.util.Dates;
import naga.framework.ui.formatter.Formatter;


/**
 * @author Bruno Salmon
 */
public class DateTimeFormatter implements Formatter {

    public static final DateTimeFormatter SINGLETON = new DateTimeFormatter();

    private DateTimeFormatter() {
    }

    @Override
    public Type getExpectedFormattedType() {
        return PrimType.DATE;
    }

    @Override
    public Object format(Object value) {
        return Dates.format(value, "dd/MM/yyyy HH:mm:ss");
    }
}
