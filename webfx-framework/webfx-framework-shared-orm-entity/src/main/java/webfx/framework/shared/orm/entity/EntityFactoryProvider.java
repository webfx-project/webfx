package webfx.framework.shared.orm.entity;

import webfx.platform.shared.util.collection.Collections;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public interface EntityFactoryProvider<E extends Entity> {

    Class<E> entityClass();

    Object domainClassId();

    EntityFactory<E> entityFactory();

    static Collection<EntityFactoryProvider> getProvidedFactories() {
        return Collections.listOf(ServiceLoader.load(EntityFactoryProvider.class));
    }

}
