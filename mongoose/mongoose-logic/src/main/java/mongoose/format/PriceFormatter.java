package mongoose.format;

import naga.core.util.Numbers;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class PriceFormatter implements Converter {

    public static final PriceFormatter SINGLETON = new PriceFormatter();

    private PriceFormatter() {
    }

    @Override
    public Object convert(Object value) {
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
