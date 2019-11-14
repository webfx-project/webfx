package webfx.framework.client.ui.filter;

import webfx.framework.client.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ReactiveExpressionFilterFactoryMixin extends HasDataSourceModel, HasActiveProperty {

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter() {
        return createReactiveExpressionFilter(null);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter(Object jsonOrClass) {
        return createReactiveExpressionFilter(null, jsonOrClass);
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter(ReactiveExpressionFilter<?> parentFilter) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>(parentFilter));
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter(ReactiveExpressionFilter<?> parentFilter, Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>(parentFilter, jsonOrClass));
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeReactiveExpressionFilter(ReactiveExpressionFilter<E> reactiveExpressionFilter) {
        if (reactiveExpressionFilter.getParentFilter() == null)
            reactiveExpressionFilter.bindActivePropertyTo(activeProperty());
        return reactiveExpressionFilter
                .setDataSourceModel(getDataSourceModel())
                ;
    }

}
