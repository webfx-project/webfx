package mongooses.core.shared.domainmodel.formatters;

import webfx.fxkits.extra.type.PrimType;
import webfx.fxkits.extra.type.Type;
import webfx.platforms.core.util.Dates;
import webfx.framework.ui.util.formatter.Formatter;


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
