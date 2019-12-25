package webfx.framework.client.orm.reactive.dql.querypush;

import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.reactive.call.query.push.ReactiveQueryPushCall;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQueryAPI;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;

/**
 * @author Bruno Salmon
 */
public final class ReactiveDqlQueryPush<E> extends ReactiveDqlQuery<E> {

    public ReactiveDqlQueryPush(ReactiveDqlStatement<E> reactiveDqlStatement) {
        super(reactiveDqlStatement, new ReactiveQueryPushCall());
    }

    private ReactiveQueryPushCall getReactiveQueryPushCall() {
        return (ReactiveQueryPushCall) reactiveQueryCall;
    }

    @Override
    public ReactiveDqlQueryPush<E> setActiveParent(ReactiveDqlQueryAPI<?, ?> activeParent) {
        getReactiveQueryPushCall().setActiveParent(((ReactiveDqlQueryPush<?>) activeParent.getReactiveDqlQuery()).getReactiveQueryPushCall());
        return this;
    }

    /*==================================================================================================================
      =========================================== Fluent API upgrade ===================================================
      ================================================================================================================*/

    @Override
    public ReactiveDqlQueryPush<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        return (ReactiveDqlQueryPush<E>) super.bindActivePropertyTo(activeProperty);
    }

    @Override
    public ReactiveDqlQueryPush<E> unbindActiveProperty() {
        return (ReactiveDqlQueryPush<E>) super.unbindActiveProperty();
    }

    @Override
    public ReactiveDqlQueryPush<E> start() {
        return (ReactiveDqlQueryPush<E>) super.start();
    }

    @Override
    public ReactiveDqlQueryPush<E> stop() {
        return (ReactiveDqlQueryPush<E>) super.stop();
    }

    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlQueryPush<E> create(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return new ReactiveDqlQueryPush<>(reactiveDqlStatement);
    }

    /*==================================================================================================================
      ==================================== Conventional static factory API =============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlQueryPush<E> createReactiveChain() {
        return create(ReactiveDqlStatement.create());
    }

    public static <E> ReactiveDqlQueryPush<E> createReactiveChain(Object mixin) {
        return initMixin(create(ReactiveDqlStatement.create()), mixin);
    }

    public static <E> ReactiveDqlQuery<E> createMasterReactiveChain(Object mixin, Object pm) {
        return initMixin(create(ReactiveDqlStatement.createMaster(pm)), mixin);
    }
}
