package mongoose.shared.domainmodel.formatters;

import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.type.Type;
import webfx.platform.shared.util.Dates;
import webfx.framework.shared.util.formatter.Formatter;


/**
 * @author Bruno Salmon
 */
public final class DateTimeFormatter implements Formatter {

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
