package naga.framework.ui.filter;

import naga.framework.activity.activeproperty.HasActiveProperty;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.framework.orm.entity.Entity;

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
