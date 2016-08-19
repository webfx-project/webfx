package naga.toolkit.cell.collators;

import naga.toolkit.spi.Toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class NodeCollatorRegistry {

    private static Map<String, NodeCollator> collators = new HashMap<>();

    private static NodeCollator hBoxCollator = Toolkit.get()::createHBox;
    private static NodeCollator vBoxCollator = Toolkit.get()::createVBox;
    private static NodeCollator flowPaneCollator = Toolkit.get()::createFlowPane;

    static {
        registerCollator("hBox", hBoxCollator());
        registerCollator("vBox", vBoxCollator());
        registerCollator("flowPane", flowPaneCollator());
    }

    public static void registerCollator(String name, NodeCollator collator) {
        collators.put(name.toLowerCase(), collator);
    }

    public static NodeCollator getCollator(String name) {
        return name == null ? null : collators.get(name.toLowerCase());
    }

    public static NodeCollator hBoxCollator() {
        return hBoxCollator;
    }

    public static NodeCollator vBoxCollator() {
        return vBoxCollator;
    }

    public static NodeCollator flowPaneCollator() {
        return flowPaneCollator;
    }
}
