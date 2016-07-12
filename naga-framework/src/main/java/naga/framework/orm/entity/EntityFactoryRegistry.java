package naga.framework.orm.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class EntityFactoryRegistry {

    private static Map<Class<? extends Entity>, Object> entityDomainClassIds = new HashMap<>();
    private static Map<Object, EntityFactory> entityFactories = new HashMap<>();

    public static <E extends Entity> void registerEntityFactory(Class<E> entityClass, Object domainClassId, EntityFactory<E> entityFactory) {
        entityDomainClassIds.put(entityClass, domainClassId);
        entityFactories.put(domainClassId, entityFactory);
    }

    public static Object getEntityDomainClassId(Class<? extends Entity> entityClass) {
        return entityDomainClassIds.get(entityClass);
    }

    public static <E extends Entity> EntityFactory<E> getEntityFactory(Class<E> entityClass) {
        return getEntityFactory(getEntityDomainClassId(entityClass));

    }

    public static <E extends Entity> EntityFactory<E> getEntityFactory(Object domainClassId) {
        return entityFactories.get(domainClassId);
    }

}
