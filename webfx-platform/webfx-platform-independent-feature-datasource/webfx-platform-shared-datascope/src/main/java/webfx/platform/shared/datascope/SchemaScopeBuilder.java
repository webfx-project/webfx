package webfx.platform.shared.datascope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SchemaScopeBuilder {

    private final Map<Object /*classId*/, SchemaScope.ClassScope> classScopes = new HashMap<>();

    public SchemaScopeBuilder addClass(Object classId) {
        return addField(classId, null);
    }

    public SchemaScopeBuilder addField(Object classId, Object fieldId) {
        SchemaScope.ClassScope classScope = classScopes.get(classId);
        if (classScope == null)
            classScopes.put(classId, classScope = new SchemaScope.ClassScope(classId, new ArrayList<>()));
        if (fieldId == null)
            classScope.fieldIds = null;
        else if (classScope.fieldIds != null)
            classScope.fieldIds.add(fieldId);
        return this;
    }

    public SchemaScope build() {
        return new SchemaScope(classScopes);
    }

}
