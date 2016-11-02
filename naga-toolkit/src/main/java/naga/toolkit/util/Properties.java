package naga.toolkit.util;

import javafx.beans.property.Property;
import naga.commons.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class Properties {

    public static void runNowAndOnPropertiesChange(Consumer<Property> runnable, Property... properties) {
        runnable.accept(null);
        runOnPropertiesChange(runnable, properties);
    }

    public static void runOnPropertiesChange(Consumer<Property> runnable, Property... properties) {
        for (Property property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.accept(property));
    }
}
