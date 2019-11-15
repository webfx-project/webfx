package webfx.framework.client.orm.entity.filter.visual;

import webfx.framework.client.orm.entity.filter.ReactiveEntityFilter;
import webfx.framework.client.orm.entity.filter.ReactiveEntityFilterFactoryMixin;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ReactiveVisualFilterFactoryMixin extends ReactiveEntityFilterFactoryMixin {

    default <E extends Entity> ReactiveVisualFilter<E> createReactiveVisualFilter() {
        return createReactiveVisualFilter(null);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createReactiveVisualFilter(Object jsonOrClass) {
        return createReactiveVisualFilter(null, jsonOrClass);
    }

    default <E extends Entity> ReactiveVisualFilter<E> createReactiveVisualFilter(ReactiveEntityFilter<?> parentFilter) {
        return initializeReactiveVisualFilter(new ReactiveVisualFilter<>(parentFilter));
    }

    default <E extends Entity> ReactiveVisualFilter<E> createReactiveVisualFilter(ReactiveEntityFilter<?> parentFilter, Object jsonOrClass) {
        return initializeReactiveVisualFilter(new ReactiveVisualFilter<>(parentFilter, jsonOrClass));
    }

    default <E extends Entity> ReactiveVisualFilter<E> initializeReactiveVisualFilter(ReactiveVisualFilter<E> filter) {
        initializeReactiveEntityFilter(filter);
        return filter;
    }

}
