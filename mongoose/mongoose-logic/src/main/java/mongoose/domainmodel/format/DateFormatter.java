package mongoose.domainmodel.format;

import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.util.Dates;
import naga.framework.ui.format.Formatter;


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
        return Dates.format(value, "dd/MM/yyyy HH:mm:ss");
    }
}
