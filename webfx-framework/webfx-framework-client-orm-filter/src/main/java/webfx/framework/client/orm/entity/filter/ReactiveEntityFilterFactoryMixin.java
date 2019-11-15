package webfx.framework.client.orm.entity.filter;

import webfx.framework.client.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ReactiveEntityFilterFactoryMixin extends HasDataSourceModel, HasActiveProperty {

    default <E extends Entity> ReactiveEntityFilter<E> createReactiveEntityFilter() {
        return createReactiveEntityFilter(null);
    }

    default <E extends Entity> ReactiveEntityFilter<E> createReactiveEntityFilter(Object jsonOrClass) {
        return createReactiveEntityFilter(null, jsonOrClass);
    }

    default <E extends Entity> ReactiveEntityFilter<E> createReactiveEntityFilter(ReactiveEntityFilter<?> parentFilter) {
        return initializeReactiveEntityFilter(new ReactiveEntityFilter<>(parentFilter));
    }

    default <E extends Entity> ReactiveEntityFilter<E> createReactiveEntityFilter(ReactiveEntityFilter<?> parentFilter, Object jsonOrClass) {
        return initializeReactiveEntityFilter(new ReactiveEntityFilter<>(parentFilter, jsonOrClass));
    }

    default <E extends Entity> ReactiveEntityFilter<E> initializeReactiveEntityFilter(ReactiveEntityFilter<E> filter) {
        if (filter.getParentFilter() == null)
            filter.bindActivePropertyTo(activeProperty());
        return filter.setDataSourceModel(getDataSourceModel());
    }
    
}
