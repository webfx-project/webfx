package mongoose.format;

import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.format.Formatter;
import naga.core.util.Numbers;

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
        if (!Numbers.isNumber(value))
            return value;
        String cents = value.toString();
        switch (cents.length()) {
            case 1 : return "0.0" + cents;
            case 2 : return "0." + cents;
            default: return cents.substring(0, cents.length() - 2) + "." + cents.substring(cents.length() - 2);
        }
    }
}
