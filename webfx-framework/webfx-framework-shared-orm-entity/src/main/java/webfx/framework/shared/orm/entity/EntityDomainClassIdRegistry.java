package webfx.framework.shared.orm.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class EntityDomainClassIdRegistry {

    private static final Map<Class<? extends Entity>, Object> entityDomainClassIds = new HashMap<>();

    public static <E extends Entity> void registerEntityDomainClassId(Class<E> entityClass, Object domainClassId) {
        entityDomainClassIds.put(entityClass, domainClassId);
    }

    public static Object getEntityDomainClassId(Class<? extends Entity> entityClass) {
        return entityDomainClassIds.get(entityClass);
    }
}
