package naga.framework.session.impl;

import naga.util.uuid.Uuid;
import naga.framework.session.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class MemorySession implements Session {

    private final String id = Uuid.randomUuid();
    private Map<String, Object> objects = new HashMap<>();

    @Override
    public String id() {
        return id;
    }

    @Override
    public Session put(String key, Object obj) {
        objects.put(key, obj);
        return this;
    }

    @Override
    public <T> T get(String key) {
        return (T) objects.get(key);
    }

    @Override
    public <T> T remove(String key) {
        return (T) objects.remove(key);
    }
}
