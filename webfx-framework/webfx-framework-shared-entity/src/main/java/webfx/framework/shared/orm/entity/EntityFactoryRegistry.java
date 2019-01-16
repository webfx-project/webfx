package webfx.framework.shared.orm.entity;

import webfx.platform.shared.services.log.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class EntityFactoryRegistry {

    private static final Map<Object, EntityFactory> entityFactories = new HashMap<>();

    public static void registerProvidedEntityFactories() {
        StringBuilder sb = new StringBuilder();
        Collection<EntityFactoryProvider> factories = EntityFactoryProvider.getProvidedFactories();
        for (EntityFactoryProvider entityFactoryProvider : factories) {
            registerEntityFactory(entityFactoryProvider);
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(entityFactoryProvider.domainClassId());
        }
        Logger.log(factories.size() + " entity factories provided for: " + sb);
    }

    public static <E extends Entity> void registerEntityFactory(EntityFactoryProvider<E> entityFactoryProvider) {
        registerEntityFactory(entityFactoryProvider.entityClass(), entityFactoryProvider.domainClassId(), entityFactoryProvider.entityFactory());
    }

    public static <E extends Entity> void registerEntityFactory(Class<E> entityClass, Object domainClassId, EntityFactory<E> entityFactory) {
        //Logger.log("Registering " + domainClassId + " entity factory (creates " + entityFactory.createEntity(null, null).getClass().getName() + " instances for " + entityClass + ")");
        EntityDomainClassIdRegistry.registerEntityDomainClassId(entityClass, domainClassId);
        entityFactories.put(domainClassId, entityFactory);
    }

    public static <E extends Entity> EntityFactory<E> getEntityFactory(Class<E> entityClass) {
        return getEntityFactory(EntityDomainClassIdRegistry.getEntityDomainClassId(entityClass));
    }

    public static <E extends Entity> EntityFactory<E> getEntityFactory(Object domainClassId) {
        return entityFactories.get(domainClassId);
    }
}
