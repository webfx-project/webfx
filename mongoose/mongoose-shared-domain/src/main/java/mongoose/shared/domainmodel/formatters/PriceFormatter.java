package mongoose.shared.domainmodel.formatters;

import webfx.framework.shared.util.formatter.Formatter;
import webfx.fxkit.extra.type.PrimType;
import webfx.fxkit.extra.type.Type;
import webfx.platform.shared.util.Numbers;

/**
 * @author Bruno Salmon
 */
public class PriceFormatter implements Formatter {

    public static final PriceFormatter INSTANCE = new PriceFormatter();

    private final String currencySymbol;

    public PriceFormatter() {
        this(null);
    }

    public PriceFormatter(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public Type getOutputType() {
        return PrimType.STRING;
    }

    @Override
    public Object format(Object value) {
        return currencySymbol != null ? formatWithCurrency(value, currencySymbol) : format(value, true);
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

    public static String formatWithoutCurrency(Object value) {
        return formatWithCurrency(value, "");
    }

    public static String formatWithCurrency(Object value, String currencySymbol) {
        String price = (String) PriceFormatter.INSTANCE.format(value, currencySymbol == null);
        return currencySymbol == null || currencySymbol.isEmpty() ? price : currencySymbol.startsWith(" ") ? price + currencySymbol : currencySymbol + price;
    }
}
