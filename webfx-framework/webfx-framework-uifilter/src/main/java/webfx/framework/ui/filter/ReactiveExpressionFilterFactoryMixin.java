package webfx.framework.ui.filter;

import webfx.framework.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.orm.domainmodel.HasDataSourceModel;
import webfx.framework.orm.entity.Entity;

/**
 * @author Bruno Salmon
 */
public interface ReactiveExpressionFilterFactoryMixin extends HasDataSourceModel, HasActiveProperty {

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter() {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>());
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>(jsonOrClass));
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeReactiveExpressionFilter(ReactiveExpressionFilter<E> reactiveExpressionFilter) {
        return reactiveExpressionFilter
                .setDataSourceModel(getDataSourceModel())
                .bindActivePropertyTo(activeProperty())
                .setPush(true) // Making server push notifications on by default
                ;
    }

}
