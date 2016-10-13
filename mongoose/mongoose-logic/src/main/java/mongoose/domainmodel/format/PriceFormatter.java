package mongoose.domainmodel.format;

import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.framework.ui.format.Formatter;
import naga.commons.util.Numbers;

/**
 * @author Bruno Salmon
 */
public class PriceFormatter implements Formatter {

    public static final PriceFormatter SINGLETON = new PriceFormatter();

    private PriceFormatter() {
    }

    @Override
    public Type getExpectedFormattedType() {
        return PrimType.STRING;
    }

    @Override
    public Object format(Object value) {
        return format(value, true);
    }

    public Object format(Object value, boolean show00cents) {
        if (!Numbers.isNumber(value))
            return value;
        String cents = value.toString();
        switch (cents.length()) {
            case 1 : return show00cents || !cents.equals("0") ? "0.0" + cents : "0";
            case 2 : return "0." + cents;
            default: return cents.substring(0, cents.length() - 2) + (!show00cents && cents.endsWith("00") ? "" : "." + cents.substring(cents.length() - 2));
        }
    }
}
