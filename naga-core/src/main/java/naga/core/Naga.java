package naga.core;

import java.util.HashMap;
import java.util.Map;

/*
 * @author Bruno Salmon
 */

public class Naga {

    private Map<String, String> messages = new HashMap<>();

    public Naga() {
        messages.put("appName", "Naga core 0.1.0 prototype");
    }


    public String getMessage(String key) {
        return messages.get(key);
    }
}
