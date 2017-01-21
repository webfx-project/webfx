package naga.framework.activity.presentationlogic;

import naga.commons.util.function.Factory;
import naga.framework.activity.DomainActivityContextMixin;
import naga.framework.ui.filter.ReactiveExpressionFilter;

/**
 * @author Bruno Salmon
 */
public abstract class DomainPresentationLogicActivityImpl<PM>
        extends PresentationLogicActivityBase<DomainPresentationLogicActivityContextFinal<PM>, PM>
        implements DomainActivityContextMixin<DomainPresentationLogicActivityContextFinal<PM>>  {

    public DomainPresentationLogicActivityImpl() {
    }

    public DomainPresentationLogicActivityImpl(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    /** Helpers **/

    protected ReactiveExpressionFilter createReactiveExpressionFilter() {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter());
    }

    protected ReactiveExpressionFilter createReactiveExpressionFilter(Object jsonOrClass) {
        return initializeReactiveExpressionFilter(new ReactiveExpressionFilter(jsonOrClass));
    }

    private ReactiveExpressionFilter initializeReactiveExpressionFilter(ReactiveExpressionFilter reactiveExpressionFilter) {
        reactiveExpressionFilter.activePropertyProperty().bind(activeProperty());
        return reactiveExpressionFilter
                .setDataSourceModel(getDataSourceModel())
                .setI18n(getI18n())
                ;
    }

}
