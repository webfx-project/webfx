package webfx.framework.client.orm.entity.filter.reactive_call.querypush;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import webfx.framework.client.orm.entity.filter.reactive_call.query.ReactiveQuery;
import webfx.framework.client.orm.entity.filter.reactive_call.SwitchableReactiveCall;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;

/**
 * @author Bruno Salmon
 */
public class ReactiveQueryOptionalPush extends SwitchableReactiveCall<QueryArgument, QueryResult> {

    private final ReactiveQuery reactiveQuery;
    private final ReactiveQueryPush reactiveQueryPush;

    public ReactiveQueryOptionalPush(ReactiveQuery reactiveQuery, ReactiveQueryPush reactiveQueryPush) {
        this.reactiveQuery = reactiveQuery;
        this.reactiveQueryPush = reactiveQueryPush;
    }

    public ReactiveQuery getReactiveQuery() {
        return reactiveQuery;
    }

    public ReactiveQueryPush getReactiveQueryPush() {
        return reactiveQueryPush;
    }

    private final BooleanProperty pushProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            updateDelegate();
        }
    };

    public BooleanProperty pushProperty() {
        return pushProperty;
    }

    public boolean isPush() {
        return pushProperty.get();
    }

    public void setPush(boolean push) {
        pushProperty.set(push);
    }

    private void updateDelegate() {
        setDelegate(isPush() ? reactiveQueryPush : reactiveQuery);
    }

    @Override
    protected void onStarted() {
        updateDelegate();
        super.onStarted();
    }
}
