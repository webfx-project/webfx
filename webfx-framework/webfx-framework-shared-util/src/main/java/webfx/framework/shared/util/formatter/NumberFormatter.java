package webfx.framework.shared.util.formatter;

import webfx.extras.type.PrimType;
import webfx.extras.type.Type;

public class NumberFormatter implements Formatter, Parser {

    public static final NumberFormatter SINGLETON = new NumberFormatter();

    private NumberFormatter() {
    }

    @Override
    public Type getOutputType() {
        return PrimType.STRING;
    }

    @Override
    public Object format(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public Object parse(Object value) {
        if (value == null)
            return null;
        String s = value.toString();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
