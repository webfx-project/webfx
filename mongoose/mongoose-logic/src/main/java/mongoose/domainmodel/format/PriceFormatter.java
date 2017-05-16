package mongoose.domainmodel.format;

import mongoose.entities.Event;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.util.Numbers;
import naga.framework.orm.entity.EntityId;
import naga.framework.ui.format.Formatter;

/**
 * @author Bruno Salmon
 */
public class PriceFormatter implements Formatter {

    public static final PriceFormatter INSTANCE = new PriceFormatter();

    private final String currencySymbol;

    public PriceFormatter() {
        this((String) null);
    }

    public PriceFormatter(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public PriceFormatter(Event event) {
        this(getEventCurrencySymbol(event));
    }

    @Override
    public Type getExpectedFormattedType() {
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

    public static String formatWithCurrency(Object value, Event event) {
        return formatWithCurrency(value, getEventCurrencySymbol(event));
    }

    public static String getEventCurrencySymbol(Event event) {
        // Temporary hard coded
        EntityId organizationId = event == null ? null : event.getOrganizationId();
        if (organizationId == null)
            return null;
        boolean isKMCF = Numbers.toInteger(organizationId.getPrimaryKey()) == 2;
        return isKMCF ? " €" : "£";
    }

    public static String formatWithCurrency(Object value, String currencySymbol) {
        String price = (String) PriceFormatter.INSTANCE.format(value, currencySymbol == null);
        return currencySymbol == null ? price : currencySymbol.startsWith(" ") ? price + currencySymbol : currencySymbol + price;
    }
}
