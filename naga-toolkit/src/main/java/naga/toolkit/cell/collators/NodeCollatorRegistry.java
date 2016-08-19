package naga.toolkit.cell.collators;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class NodeCollatorRegistry {

    private static Map<String, NodeCollator> collators = new HashMap<>();

    static {
        registerCollator("hBox", NodeCollator.hBoxCollator());
        registerCollator("vBox", NodeCollator.vBoxCollator());
    }

    public static void registerCollator(String name, NodeCollator collator) {
        collators.put(name.toLowerCase(), collator);
    }

    public static NodeCollator getCollator(String name) {
        return name == null ? null : collators.get(name.toLowerCase());
    }
}
