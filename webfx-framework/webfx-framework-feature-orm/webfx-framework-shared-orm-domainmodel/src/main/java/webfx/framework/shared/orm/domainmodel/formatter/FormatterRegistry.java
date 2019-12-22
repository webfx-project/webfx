package webfx.framework.shared.orm.domainmodel.formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * Temporary registry class for formatters. It would be better to register them within the domain model (TODO).
 *
 * @author Bruno Salmon
 */
public final class FormatterRegistry {

    private final static Map<String, ValueFormatter> formatters = new HashMap<>();

    public static void registerFormatter(String formatName, ValueFormatter formatter) {
        formatters.put(formatName, formatter);
    }

    public static ValueFormatter getFormatter(String formatName) {
        return formatters.get(formatName);
    }
}
