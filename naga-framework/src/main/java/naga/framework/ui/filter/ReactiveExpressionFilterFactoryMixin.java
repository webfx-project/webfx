package naga.framework.ui.filter;

import naga.framework.activity.activeproperty.HasActiveProperty;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.services.i18n.spi.HasI18nProvider;

/**
 * @author Bruno Salmon
 */
public interface ReactiveExpressionFilterFactoryMixin extends HasI18nProvider, HasDataSourceModel, HasActiveProperty {

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter() {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>());
    }

    default <E extends Entity> ReactiveExpressionFilter<E> createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter<>(jsonOrClass));
    }

    default <E extends Entity> ReactiveExpressionFilter<E> initializeReactiveExpressionFilter(ReactiveExpressionFilter<E> reactiveExpressionFilter) {
        return reactiveExpressionFilter
                .setDataSourceModel(getDataSourceModel())
                .setI18n(getI18n())
                .bindActivePropertyTo(activeProperty())
                .setPush(true) // Making server push notifications on by default
                ;
    }

}
