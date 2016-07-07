package naga.framework.ui.format;

import java.util.HashMap;
import java.util.Map;

/**
 * Temporary registry class for formatters. It would be better to register them within the domain model (TODO).
 *
 * @author Bruno Salmon
 */
public class FormatterRegistry {

    private static Map<String, Formatter> formatters = new HashMap<>();

    public static void registerFormatter(String formatName, Formatter formatter) {
        formatters.put(formatName, formatter);
    }

    public static Formatter getFormatter(String formatName) {
        return formatters.get(formatName);
    }
}
