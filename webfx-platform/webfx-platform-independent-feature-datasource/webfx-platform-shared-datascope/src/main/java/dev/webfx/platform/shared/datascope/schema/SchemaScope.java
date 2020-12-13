package dev.webfx.platform.shared.datascope.schema;

import dev.webfx.platform.shared.datascope.KeyDataScope;
import dev.webfx.platform.shared.datascope.ScopeUtil;

/**
 * @author Bruno Salmon
 */
public interface SchemaScope extends KeyDataScope {

    String KEY = "schema";

    @Override
    default Object getKey() {
        return KEY;
    }

    @Override
    default boolean intersects(KeyDataScope otherScope) {
        return otherScope instanceof SchemaScope && intersects((SchemaScope) otherScope);
    }

    boolean intersects(SchemaScope schemaScope);

    ClassScope getClassScope(Object classId);

    static SchemaScopeBuilder builder() {
        return new SchemaScopeBuilder();
    }

    final class ClassScope {
        final Object classId;
        private Object[] fieldIds; // may be null (=> means any field), otherwise list of fields

        public ClassScope(Object classId, Object[] fieldIds) {
            this.classId = classId;
            this.fieldIds = fieldIds;
        }

        public boolean intersects(ClassScope classScope) {
            if (!classId.equals(classScope.classId))
                return false;
            if (fieldIds == null || classScope.fieldIds == null)
                return true;
            return ScopeUtil.arraysIntersect(fieldIds, classScope.fieldIds);
        }
    }
}
