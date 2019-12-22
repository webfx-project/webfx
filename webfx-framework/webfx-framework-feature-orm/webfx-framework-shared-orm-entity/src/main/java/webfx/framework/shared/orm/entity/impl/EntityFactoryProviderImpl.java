package webfx.framework.shared.orm.entity.impl;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityFactory;
import webfx.framework.shared.orm.entity.EntityFactoryProvider;

/**
 * @author Bruno Salmon
 */
public class EntityFactoryProviderImpl<E extends Entity> implements EntityFactoryProvider<E> {

    private final Class<E> entityClass;
    private final Object domainClassId;
    private final EntityFactory<E> entityFactory;

    public EntityFactoryProviderImpl(Class<E> entityClass, EntityFactory<E> entityFactory) {
        this(entityClass, entityClass.getSimpleName(), entityFactory);
    }

    public EntityFactoryProviderImpl(Class<E> entityClass, Object domainClassId, EntityFactory<E> entityFactory) {
        this.entityClass = entityClass;
        this.domainClassId = domainClassId;
        this.entityFactory = entityFactory;
    }

    @Override
    public Class<E> entityClass() {
        return entityClass;
    }

    @Override
    public Object domainClassId() {
        return domainClassId;
    }

    @Override
    public EntityFactory<E> entityFactory() {
        return entityFactory;
    }

/*
    private static <E extends Entity> Class<E> guessEntityClass(EntityFactory<E> entityFactory) {
        E entityInstance = entityFactory.createEntity(null, null);
        return (Class<E>) entityInstance.getClass().getInterfaces()[0];
    }
*/
}
