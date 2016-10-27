package naga.toolkit.properties.util;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public class Properties {

    public static void runNowAndOnPropertiesChange(Runnable runnable, Property... properties) {
        runnable.run();
        runOnPropertiesChange(runnable, properties);
    }

    public static void runOnPropertiesChange(Runnable runnable, Property... properties) {
        for (Property property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.run());
    }
}
