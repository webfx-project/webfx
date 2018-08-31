package mongoose.domainmodel.formatters;

import webfx.type.PrimType;
import webfx.type.Type;
import webfx.util.Dates;
import webfx.framework.ui.formatter.Formatter;


/**
 * @author Bruno Salmon
 */
public class DateTimeFormatter implements Formatter {

    public static final DateTimeFormatter SINGLETON = new DateTimeFormatter();

    private DateTimeFormatter() {
    }

    @Override
    public Type getOutputType() {
        return PrimType.STRING;
    }

    @Override
    public Object format(Object value) {
        return Dates.format(value, "dd/MM/yyyy HH:mm:ss");
    }
}
