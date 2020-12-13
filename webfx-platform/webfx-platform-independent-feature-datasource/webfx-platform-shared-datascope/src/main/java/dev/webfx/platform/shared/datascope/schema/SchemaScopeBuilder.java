package dev.webfx.platform.shared.datascope.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class SchemaScopeBuilder {

    private final Map<Object /*classId*/, List<Object>> classFields = new HashMap<>();

    public SchemaScopeBuilder addClass(Object classId) {
        return addField(classId, null);
    }

    public SchemaScopeBuilder addField(Object classId, Object fieldId) {
        List<Object> fields = classFields.get(classId);
        if (fields == null || fieldId == null)
            classFields.put(classId, fields = fieldId == null ? null : new ArrayList<>());
        if (fields != null)
            fields.add(fieldId);
        return this;
    }

    public SchemaScope build() {
        return new SchemaScopeImpl(classFields.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new SchemaScope.ClassScope(e.getKey(), e.getValue() == null ? null : e.getValue().toArray())
                )));
    }

}
